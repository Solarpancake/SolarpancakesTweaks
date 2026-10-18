package de.solarpancake.tweaks.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

// Places a random block from the hotbar, so the hotbar works as a block palette
public class ShuffleShovelItem extends Item {
    public ShuffleShovelItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        for (int i = 0; i <= 2; i++) {
            builder.accept(Component.translatable("tooltip.shuffle_shovel.tooltips." + i));
        }
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }

        List<Integer> paletteSlots = getPaletteSlots(player.getInventory());
        if (paletteSlots.isEmpty()) {
            return InteractionResult.PASS;
        }

        // The block is picked on the server only, the client just swings and gets the result synced
        Level level = context.getLevel();
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        BlockHitResult hitResult = new BlockHitResult(context.getClickLocation(), context.getClickedFace(),
                context.getClickedPos(), context.isInside());

        // Try the slots in random order, so an item that can't go here (e.g. seeds on stone) doesn't block the others
        Collections.shuffle(paletteSlots);
        for (int slot : paletteSlots) {
            ItemStack stack = player.getInventory().getItem(slot);
            BlockPlaceContext placeContext = new BlockPlaceContext(level, player, context.getHand(), stack, hitResult);
            if (((BlockItem) stack.getItem()).place(placeContext).consumesAction()) {
                playPlaceSoundForPlayer(player, level, placeContext.getClickedPos());
                return InteractionResult.SUCCESS_SERVER;
            }
        }
        return InteractionResult.FAIL;
    }

    private static List<Integer> getPaletteSlots(Inventory inventory) {
        List<Integer> slots = new ArrayList<>();
        for (int i = 0; i < Inventory.getSelectionSize(); i++) {
            if (inventory.getItem(i).getItem() instanceof BlockItem) {
                slots.add(i);
            }
        }
        return slots;
    }

    // BlockItem.place plays the sound for everyone except the placing player (their client would normally play it itself)
    private static void playPlaceSoundForPlayer(Player player, Level level, BlockPos pos) {
        if (player instanceof ServerPlayer serverPlayer) {
            BlockState state = level.getBlockState(pos);
            SoundType soundType = state.getSoundType(level, pos, player);
            serverPlayer.connection.send(new ClientboundSoundPacket(
                    BuiltInRegistries.SOUND_EVENT.wrapAsHolder(soundType.getPlaceSound()), SoundSource.BLOCKS,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F, level.getRandom().nextLong()));
        }
    }
}
