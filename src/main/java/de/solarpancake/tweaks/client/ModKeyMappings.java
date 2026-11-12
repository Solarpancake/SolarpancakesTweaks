package de.solarpancake.tweaks.client;

import com.mojang.blaze3d.platform.InputConstants;
import de.solarpancake.tweaks.Solarpancakestweaks;
import de.solarpancake.tweaks.network.ToggleMagnetPayload;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = Solarpancakestweaks.MODID, value = Dist.CLIENT)
public class ModKeyMappings {
    public static final KeyMapping.Category CATEGORY =
            new KeyMapping.Category(Identifier.fromNamespaceAndPath(Solarpancakestweaks.MODID, "main"));

    public static final KeyMapping TOGGLE_MAGNET = new KeyMapping("key.solarpancakestweaks.toggle_magnet",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_M, CATEGORY);

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.registerCategory(CATEGORY);
        event.register(TOGGLE_MAGNET);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        while (TOGGLE_MAGNET.consumeClick()) {
            ClientPacketDistributor.sendToServer(ToggleMagnetPayload.INSTANCE);
        }
    }
}
