package de.solarpancake.tweaks.compat.curios;

import de.solarpancake.tweaks.item.ModItems;
import de.solarpancake.tweaks.item.custom.MagnetItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.Optional;

class CuriosIntegration {
    static void registerMagnet() {
        CuriosApi.registerCurio(ModItems.MAGNET.get(), new ICurioItem() {
            @Override
            public void curioTick(SlotContext slotContext, ItemStack stack) {
                if (MagnetItem.isActive(stack) && slotContext.entity() instanceof Player player
                        && player.level() instanceof ServerLevel level) {
                    MagnetItem.pullItems(player, level);
                }
            }
        });
    }

    static Optional<ItemStack> findMagnet(Player player) {
        return CuriosApi.getCuriosInventory(player)
                .flatMap(inventory -> inventory.findFirstCurio(stack -> stack.getItem() instanceof MagnetItem))
                .map(SlotResult::stack);
    }
}
