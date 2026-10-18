package de.solarpancake.tweaks;

import de.solarpancake.tweaks.screen.BlenderScreen;
import de.solarpancake.tweaks.screen.ModMenuTypes;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;


@Mod(value = Solarpancakestweaks.MODID, dist = Dist.CLIENT)

@EventBusSubscriber(modid = Solarpancakestweaks.MODID, value = Dist.CLIENT)
public class SolarpancakestweaksClient {
    public SolarpancakestweaksClient(ModContainer container) {

        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        Solarpancakestweaks.LOGGER.info("HELLO FROM CLIENT SETUP");
        Solarpancakestweaks.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

    @SubscribeEvent
    static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.BLENDER_MENU.get(), BlenderScreen::new);
    }
}
