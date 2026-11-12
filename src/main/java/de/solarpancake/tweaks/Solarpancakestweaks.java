package de.solarpancake.tweaks;

import com.mojang.logging.LogUtils;
import de.solarpancake.tweaks.attachment.ModAttachments;
import de.solarpancake.tweaks.block.ModBlocks;
import de.solarpancake.tweaks.block.entity.ModBlockEntities;
import de.solarpancake.tweaks.compat.curios.CuriosCompat;
import de.solarpancake.tweaks.component.ModDataComponents;
import de.solarpancake.tweaks.creativemodetab.ModCreativeModeTabs;
import de.solarpancake.tweaks.effect.ModEffects;
import de.solarpancake.tweaks.item.ModItems;
import de.solarpancake.tweaks.menu.ModMenuTypes;
import de.solarpancake.tweaks.recipe.ModRecipes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(Solarpancakestweaks.MODID)
public class Solarpancakestweaks {
    public static final String MODID = "solarpancakestweaks";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Solarpancakestweaks(IEventBus modEventBus) {
        modEventBus.addListener(this::commonSetup);

        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModEffects.register(modEventBus);
        ModAttachments.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModDataComponents.register(modEventBus);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        CuriosCompat.init();
    }
}
