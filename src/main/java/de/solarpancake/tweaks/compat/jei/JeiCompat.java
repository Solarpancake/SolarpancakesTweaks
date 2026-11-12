package de.solarpancake.tweaks.compat.jei;

import de.solarpancake.tweaks.Solarpancakestweaks;
import de.solarpancake.tweaks.block.ModBlocks;
import de.solarpancake.tweaks.block.entity.BlenderBlockEntity;
import de.solarpancake.tweaks.client.ClientRecipeCache;
import de.solarpancake.tweaks.client.blender.BlenderScreen;
import de.solarpancake.tweaks.menu.BlenderMenu;
import de.solarpancake.tweaks.menu.ModMenuTypes;
import de.solarpancake.tweaks.recipe.BlenderRecipe;
import de.solarpancake.tweaks.recipe.ModRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

/**
 * JEI integration (only loaded when JEI is installed).
 * <ul>
 *     <li>"Blender" recipe page with all blending recipes</li>
 *     <li>the blender block as its crafting station (R/U on the blender shows them)</li>
 *     <li>click the pipes in the blender GUI to open the page</li>
 *     <li>"+" button moves the ingredients from your inventory into the blender</li>
 * </ul>
 */
@JeiPlugin
public class JeiCompat implements IModPlugin {
    public static final Supplier<IRecipeHolderType<BlenderRecipe>> BLENDING =
            IRecipeHolderType.createDeferred(ModRecipes.BLENDING_TYPE::get);

    private static final int PLAYER_INVENTORY_SLOTS = 36;

    @Override
    public Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(Solarpancakestweaks.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new BlenderRecipeCategory(registration.getJeiHelpers().getGuiHelper(), BLENDING.get()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(BLENDING.get(), ClientRecipeCache.blending());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(BLENDING.get(), ModBlocks.BLENDER_BLOCK.get());
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        // the pipe area between the slots
        registration.addRecipeClickArea(BlenderScreen.class, 61, 34, 54, 18, BLENDING.get());
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(BlenderMenu.class, ModMenuTypes.BLENDER_MENU.get(), BLENDING.get(),
                BlenderBlockEntity.CAN_SLOT, 3, BlenderBlockEntity.SLOT_COUNT, PLAYER_INVENTORY_SLOTS);
    }
}
