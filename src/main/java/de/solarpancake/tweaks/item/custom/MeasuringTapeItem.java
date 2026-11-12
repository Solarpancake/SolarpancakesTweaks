package de.solarpancake.tweaks.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

/**
 * Measuring tape.
 * <p>
 * Right click a block = start, right click again = end (a live preview follows the hovered block in between).
 * Finished boxes stay visible; the next right click starts a fresh measurement without removing the old ones.
 * As soon as the tape is no longer in the main hand, every measurement is discarded.
 * <p>
 * All of this is purely client-side (nothing is synced or saved), so the item itself does almost nothing:
 * input, state and rendering live in {@code client.measuringtape.MeasuringTapeClient}.
 */
public class MeasuringTapeItem extends Item {
    public MeasuringTapeItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        for (int i = 0; i <= 3; i++) {
            builder.accept(Component.translatable("tooltip.measuring_tape.tooltips." + i));
        }
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
    }

    // Never break blocks with the tape (also stops creative insta-break).
    @Override
    public boolean canDestroyBlock(ItemStack itemStack, BlockState state, Level level, BlockPos pos, LivingEntity user) {
        return false;
    }
}
