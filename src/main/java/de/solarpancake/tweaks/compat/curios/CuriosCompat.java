package de.solarpancake.tweaks.compat.curios;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

import java.util.Optional;

public class CuriosCompat {
    public static final String CURIOS_ID = "curios";

    public static boolean isLoaded() {
        return ModList.get().isLoaded(CURIOS_ID);
    }

    public static void init() {
        if (isLoaded()) {
            CuriosIntegration.registerMagnet();
        }
    }

    public static Optional<ItemStack> findMagnet(Player player) {
        return isLoaded() ? CuriosIntegration.findMagnet(player) : Optional.empty();
    }
}
