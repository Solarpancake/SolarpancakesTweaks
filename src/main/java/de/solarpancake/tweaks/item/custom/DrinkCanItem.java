package de.solarpancake.tweaks.item.custom;

import de.solarpancake.tweaks.effect.DrunkEffect;
import de.solarpancake.tweaks.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

/**
 * A filled can that can be drunk {@link #SIPS} times. Each sip applies the item's food like a normal drink and
 * raises the drunk level by one ({@link DrunkEffect#addSip});
 * the sips are tracked with the durability bar, and after the last sip it turns into an empty can.
 * <p>
 * Register with {@code properties.durability(DrinkCanItem.SIPS)} (which also makes it non-stackable).
 */
public class DrinkCanItem extends Item {
    public static final int SIPS = 5;

    public DrinkCanItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        // Let vanilla do one normal "drink" (food, effects, sound, stats) on a copy, so the real can isn't used up.
        super.finishUsingItem(stack.copyWithCount(1), level, entity);
        if (!level.isClientSide()) {
            DrunkEffect.addSip(entity); // drunk level +1, timer reset
        }

        if (entity instanceof Player player && player.hasInfiniteMaterials()) {
            return stack; // creative: endless can
        }
        int sipsTaken = stack.getDamageValue() + 1;
        if (sipsTaken >= stack.getMaxDamage()) {
            ItemStack empty = new ItemStack(ModItems.EMPTY_CAN.get());
            if (stack.getCount() <= 1) {
                return empty;
            }
            // only if someone managed to stack filled cans: give the empty one separately
            stack.shrink(1);
            if (!level.isClientSide() && entity instanceof Player player && !player.getInventory().add(empty)) {
                player.drop(empty, false);
            }
            return stack;
        }
        stack.setDamageValue(sipsTaken);
        return stack;
    }

    public static int sipsLeft(ItemStack stack) {
        return stack.getMaxDamage() - stack.getDamageValue();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
        builder.accept(Component.translatable("tooltip.drink_can.sips", sipsLeft(stack), stack.getMaxDamage()));
        super.appendHoverText(stack, context, display, builder, flag);
    }
}
