package de.solarpancake.tweaks.attachment;

import de.solarpancake.tweaks.Solarpancakestweaks;
import net.minecraft.core.BlockPos;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Solarpancakestweaks.MODID);

    public static final Supplier<AttachmentType<BlockPos>> CAT_TREE_BASE = ATTACHMENT_TYPES.register("cat_tree_base",
            () -> AttachmentType.builder(() -> BlockPos.ZERO)
                    .serialize(BlockPos.CODEC.fieldOf("pos"))
                    .build());

    /** Drunk tripping: ticks until the player can trip again (lies on the ground for the first part). Not saved. */
    public static final Supplier<AttachmentType<Integer>> TRIP_TICKS = ATTACHMENT_TYPES.register("trip_ticks",
            () -> AttachmentType.builder(() -> 0).build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
