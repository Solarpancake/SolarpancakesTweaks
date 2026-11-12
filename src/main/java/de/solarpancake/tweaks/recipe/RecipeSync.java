package de.solarpancake.tweaks.recipe;

import de.solarpancake.tweaks.Solarpancakestweaks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

/**
 * Since 1.21.2 the client doesn't get recipes by default. This asks the server to also send the blending recipes,
 * so recipe viewers like JEI can show them (also on dedicated servers).
 */
@EventBusSubscriber(modid = Solarpancakestweaks.MODID)
public final class RecipeSync {
    private RecipeSync() {
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        event.sendRecipes(ModRecipes.BLENDING_TYPE.get());
    }
}
