package de.solarpancake.tweaks.item.custom;

import de.solarpancake.tweaks.client.calculator.CalculatorClientHooks;
import de.solarpancake.tweaks.component.CalculatorData;
import de.solarpancake.tweaks.component.ModDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

/**
 * Pocket calculator.
 * <p>
 * Right click: opens the calculator GUI (client only, see {@code client.calculator.CalculatorScreen}).
 * Left click on a block: the block's coordinates become the next calculator input
 * (client detects the click in {@code CalculatorClientHooks}, server stores it via {@code CaptureCalculatorPosPayload}).
 * <p>
 * Input + history are stored on the stack in {@link ModDataComponents#CALCULATOR_DATA}; the screen sends them back
 * with {@code SaveCalculatorPayload} when it closes.
 */
public class CalculatorItem extends Item {
    public CalculatorItem(Properties properties) {
        super(properties);
    }

    public static CalculatorData getData(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.CALCULATOR_DATA.get(), CalculatorData.EMPTY);
    }

    public static void setData(ItemStack stack, CalculatorData data) {
        stack.set(ModDataComponents.CALCULATOR_DATA.get(), data.sanitized());
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        for (int i = 0; i <= 1; i++) {
            builder.accept(Component.translatable("tooltip.calculator.tooltips." + i));
        }
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            CalculatorClientHooks.openScreen(hand, getData(player.getItemInHand(hand)));
        }
        return InteractionResult.SUCCESS;
    }

    // Left click is used to capture coordinates, never break blocks with it.
    @Override
    public boolean canDestroyBlock(ItemStack itemStack, BlockState state, Level level, BlockPos pos, LivingEntity user) {
        return false;
    }
}
