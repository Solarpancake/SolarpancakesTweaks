package de.solarpancake.tweaks.menu;

import de.solarpancake.tweaks.Solarpancakestweaks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Solarpancakestweaks.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<BlenderMenu>> BLENDER_MENU =
            MENUS.register("blender", () -> new MenuType<>(BlenderMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
