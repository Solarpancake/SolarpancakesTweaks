package de.solarpancake.tweaks.effect;

import de.solarpancake.tweaks.Solarpancakestweaks;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Solarpancakestweaks.MODID)
public final class DrunkEvents {
    private DrunkEvents() {
    }

    // Runs every tick (not only while drunk), so a tripped player always gets back up, even after drinking milk.
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            DrunkEffect.tickTrip(player);
        }
    }
}
