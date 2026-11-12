package de.solarpancake.tweaks.client.drunk;

import de.solarpancake.tweaks.Solarpancakestweaks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Pose;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

/** Client side of tripping: keeps the local player in the crawling pose while lying on the ground. */
@EventBusSubscriber(modid = Solarpancakestweaks.MODID, value = Dist.CLIENT)
public final class DrunkClient {
    private static int tripTicksLeft;

    private DrunkClient() {
    }

    public static void startTrip(int ticks) {
        tripTicksLeft = ticks;
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            player.setForcedPose(Pose.SWIMMING);
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (tripTicksLeft <= 0) {
            return;
        }
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            tripTicksLeft = 0;
            return;
        }
        if (--tripTicksLeft == 0) {
            player.setForcedPose(null);
        }
    }
}
