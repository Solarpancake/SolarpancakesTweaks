package de.solarpancake.tweaks;

import de.solarpancake.tweaks.client.blender.BlenderScreen;
import de.solarpancake.tweaks.menu.ModMenuTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

/** Client-only setup: menu screens. */
@Mod(value = Solarpancakestweaks.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Solarpancakestweaks.MODID, value = Dist.CLIENT)
public class SolarpancakestweaksClient {
    @SubscribeEvent
    static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.BLENDER_MENU.get(), BlenderScreen::new);
    }
}
