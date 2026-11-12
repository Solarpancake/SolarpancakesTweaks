package de.solarpancake.tweaks.client.calculator;

import de.solarpancake.tweaks.Solarpancakestweaks;
import de.solarpancake.tweaks.component.CalculatorData;
import de.solarpancake.tweaks.item.custom.CalculatorItem;
import de.solarpancake.tweaks.network.CaptureCalculatorPosPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

/**
 * Client-side glue for the {@link CalculatorItem}. Only ever called/loaded on the physical client.
 */
@EventBusSubscriber(modid = Solarpancakestweaks.MODID, value = Dist.CLIENT)
public final class CalculatorClientHooks {
    private CalculatorClientHooks() {
    }

    public static void openScreen(InteractionHand hand, CalculatorData data) {
        Minecraft.getInstance().gui.setScreen(new CalculatorScreen(hand, data));
    }

    /** Left click on a block with the calculator (in coord mode) in the main hand -> send coords to the server. */
    @SubscribeEvent
    public static void onInteraction(InputEvent.InteractionKeyMappingTriggered event) {
        if (!event.isAttack() || event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || !(player.getMainHandItem().getItem() instanceof CalculatorItem)) {
            return;
        }

        // Never punch/break with the calculator; only coord mode actually captures the block.
        event.setCanceled(true);
        boolean coordMode = CalculatorItem.getData(player.getMainHandItem()).mode() == CalculatorData.Mode.COORD;
        if (coordMode && mc.hitResult instanceof BlockHitResult hit && hit.getType() == HitResult.Type.BLOCK) {
            event.setSwingHand(true);
            ClientPacketDistributor.sendToServer(new CaptureCalculatorPosPayload(hit.getBlockPos()));
        } else {
            event.setSwingHand(false);
        }
    }
}
