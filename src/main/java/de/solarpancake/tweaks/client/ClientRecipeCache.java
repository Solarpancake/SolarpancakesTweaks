package de.solarpancake.tweaks.client;

import de.solarpancake.tweaks.Solarpancakestweaks;
import de.solarpancake.tweaks.recipe.BlenderRecipe;
import de.solarpancake.tweaks.recipe.ModRecipes;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;

import java.util.List;

/**
 * Client copy of the blending recipes (sent by the server, see {@code recipe.RecipeSync}), used by the JEI plugin.
 */
@EventBusSubscriber(modid = Solarpancakestweaks.MODID, value = Dist.CLIENT)
public final class ClientRecipeCache {
    private static List<RecipeHolder<BlenderRecipe>> blending = List.of();

    private ClientRecipeCache() {
    }

    public static List<RecipeHolder<BlenderRecipe>> blending() {
        return blending;
    }

    // HIGHEST: JEI starts up on this same event, the list must be filled before that.
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRecipesReceived(RecipesReceivedEvent event) {
        blending = List.copyOf(event.getRecipeMap().byType(ModRecipes.BLENDING_TYPE.get()));
    }

    @SubscribeEvent
    public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        blending = List.of();
    }
}
