package de.solarpancake.tweaks.client.measuringtape;

import de.solarpancake.tweaks.Solarpancakestweaks;
import de.solarpancake.tweaks.item.custom.MeasuringTapeItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.gizmos.TextGizmo;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Client-only state, input handling and rendering for the {@link MeasuringTapeItem}.
 *
 * <ul>
 *     <li>Input: right click only. 1st click sets the start, 2nd click finishes the box, 3rd starts a new one, ...
 *     The click is cancelled, so nothing gets placed/used and no packet goes to the server.</li>
 *     <li>Preview: while a start is set, a live box is drawn from the start to the block you're looking at.</li>
 *     <li>State: {@link #measurements} (finished boxes) + {@link #pendingStart} (in progress).</li>
 *     <li>Lifetime: {@link #onClientTick} clears everything once the tape leaves the main hand.</li>
 *     <li>Rendering: vanilla gizmos, emitted every client tick (the tick runs inside Minecraft's per-tick gizmo collector).</li>
 * </ul>
 */
@EventBusSubscriber(modid = Solarpancakestweaks.MODID, value = Dist.CLIENT)
public final class MeasuringTapeClient {
    private static final int BOX_COLOR = 0xFFFFD23F;
    private static final int PREVIEW_COLOR = 0xFF4FC3F7;
    private static final int TEXT_COLOR = 0xFFFFFFFF;
    // Draw outlines slightly outside the block faces so they don't z-fight (flicker) with them.
    private static final double OUTLINE_PADDING = 0.01;
    // How far edge labels float outside the box edges.
    private static final double EDGE_LABEL_OFFSET = 0.2;

    private static final List<Measurement> measurements = new ArrayList<>();
    private static @Nullable BlockPos pendingStart;

    private MeasuringTapeClient() {
    }

    /* ---------- input ---------- */

    @SubscribeEvent
    public static void onInteraction(InputEvent.InteractionKeyMappingTriggered event) {
        if (!event.isUseItem() || event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || !isHoldingTape(player)) {
            return;
        }

        // Always swallow the right click while holding the tape, even when not aiming at a block.
        event.setCanceled(true);
        event.setSwingHand(false);

        BlockPos target = getTargetedBlock(mc);
        if (target != null) {
            clickBlock(target);
        }
    }

    private static void clickBlock(BlockPos pos) {
        if (pendingStart == null) {
            pendingStart = pos.immutable();
        } else {
            measurements.add(new Measurement(pendingStart, pos.immutable()));
            pendingStart = null;
        }
    }

    /* ---------- lifetime + rendering ---------- */

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || !isHoldingTape(player)) {
            clear();
            return;
        }
        render(mc);
    }

    private static void render(Minecraft mc) {
        for (Measurement m : measurements) {
            renderMeasurement(m, BOX_COLOR);
        }
        if (pendingStart != null) {
            BlockPos hovered = getTargetedBlock(mc);
            if (hovered != null) {
                // live preview from the start to the block currently looked at
                renderMeasurement(new Measurement(pendingStart, hovered), PREVIEW_COLOR);
            } else {
                Gizmos.cuboid(pendingStart, (float) OUTLINE_PADDING, GizmoStyle.stroke(PREVIEW_COLOR));
            }
        }
    }

    private static void renderMeasurement(Measurement m, int color) {
        AABB box = m.box();
        Gizmos.cuboid(box.inflate(OUTLINE_PADDING), GizmoStyle.stroke(color));
        renderEdgeLabels(m, box);

        // summary above the box
        String label = m.sizeX() + " x " + m.sizeY() + " x " + m.sizeZ() + "  (" + m.volume() + ")";
        Gizmos.billboardText(label, box.getBottomCenter().add(0, box.getYsize() + 0.6, 0),
                TextGizmo.Style.forColorAndCentered(TEXT_COLOR)).setAlwaysOnTop();
    }

    /**
     * Puts the length on all 12 edges, at each edge's midpoint, nudged slightly outwards so the text
     * floats next to the line instead of on it. Not "always on top", so edges hidden behind terrain
     * don't clutter the view.
     */
    private static void renderEdgeLabels(Measurement m, AABB box) {
        double[] xs = {box.minX - EDGE_LABEL_OFFSET, box.maxX + EDGE_LABEL_OFFSET};
        double[] ys = {box.minY - EDGE_LABEL_OFFSET, box.maxY + EDGE_LABEL_OFFSET};
        double[] zs = {box.minZ - EDGE_LABEL_OFFSET, box.maxZ + EDGE_LABEL_OFFSET};
        Vec3 center = box.getCenter();
        TextGizmo.Style style = TextGizmo.Style.forColorAndCentered(TEXT_COLOR);

        String lenX = String.valueOf(m.sizeX());
        String lenY = String.valueOf(m.sizeY());
        String lenZ = String.valueOf(m.sizeZ());

        for (double a : ys) {
            for (double b : zs) {
                Gizmos.billboardText(lenX, new Vec3(center.x, a, b), style); // edges along X
            }
        }
        for (double a : xs) {
            for (double b : zs) {
                Gizmos.billboardText(lenY, new Vec3(a, center.y, b), style); // edges along Y
            }
        }
        for (double a : xs) {
            for (double b : ys) {
                Gizmos.billboardText(lenZ, new Vec3(a, b, center.z), style); // edges along Z
            }
        }
    }

    /* ---------- helpers ---------- */

    private static @Nullable BlockPos getTargetedBlock(Minecraft mc) {
        if (mc.hitResult instanceof BlockHitResult hit && hit.getType() == HitResult.Type.BLOCK) {
            return hit.getBlockPos();
        }
        return null;
    }

    private static boolean isHoldingTape(LocalPlayer player) {
        return player.getMainHandItem().getItem() instanceof MeasuringTapeItem;
    }

    public static void clear() {
        measurements.clear();
        pendingStart = null;
    }

    public static List<Measurement> getMeasurements() {
        return List.copyOf(measurements);
    }
}
