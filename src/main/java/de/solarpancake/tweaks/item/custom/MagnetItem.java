package de.solarpancake.tweaks.item.custom;

import de.solarpancake.tweaks.compat.curios.CuriosCompat;
import de.solarpancake.tweaks.component.ModDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class MagnetItem extends Item {
    public static final double RADIUS = 12.0;
    private static final double PULL_SPEED = 0.02;

    public MagnetItem(Properties properties) {
        super(properties);
    }

    public static boolean isActive(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.MAGNET_ACTIVE.get(), false);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            toggle(player, player.getItemInHand(hand));
        }
        return InteractionResult.SUCCESS;
    }

    public static void toggle(Player player, ItemStack stack) {
        boolean active = !isActive(stack);
        stack.set(ModDataComponents.MAGNET_ACTIVE.get(), active);
        player.level().playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS,
                0.5f, active ? 1.4f : 0.7f);
        player.sendOverlayMessage(Component.translatable(active ? "item.solarpancakestweaks.magnet.on" : "item.solarpancakestweaks.magnet.off"));
    }

    public static Optional<ItemStack> findMagnet(Player player) {
        Optional<ItemStack> curio = CuriosCompat.findMagnet(player);
        if (curio.isPresent()) {
            return curio;
        }
        for (InteractionHand hand : InteractionHand.values()) {
            if (player.getItemInHand(hand).getItem() instanceof MagnetItem) {
                return Optional.of(player.getItemInHand(hand));
            }
        }
        for (ItemStack stack : player.getInventory()) {
            if (stack.getItem() instanceof MagnetItem) {
                return Optional.of(stack);
            }
        }
        return Optional.empty();
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        if (isActive(stack) && owner instanceof Player player) {
            pullItems(player, level);
        }
    }

    public static void pullItems(Player player, ServerLevel level) {
        if (player.isSpectator()) {
            return;
        }

        Vec3 target = player.position().add(0, player.getBbHeight() / 2, 0);
        AABB area = player.getBoundingBox().inflate(RADIUS);

        for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, area,
                item -> item.isAlive() && !item.hasPickUpDelay() && item.distanceToSqr(target) <= RADIUS * RADIUS)) {
            Vec3 pull = target.subtract(item.position()).normalize().scale(PULL_SPEED);
            item.setDeltaMovement(pull);
            item.hurtMarked = true; // sync the new motion to clients right away
        }
    }

    // Enchantment glint while switched on
    @Override
    public boolean isFoil(ItemStack stack) {
        return isActive(stack) || super.isFoil(stack);
    }
}
