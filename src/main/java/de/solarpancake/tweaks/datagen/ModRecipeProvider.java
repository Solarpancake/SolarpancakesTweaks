package de.solarpancake.tweaks.datagen;

import de.solarpancake.tweaks.Solarpancakestweaks;
import de.solarpancake.tweaks.block.ModBlocks;
import de.solarpancake.tweaks.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    public static class Runner extends RecipeProvider.Runner {

        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new ModRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "SolarpancakesTweaks Recipes";
        }
    }
    @Override
    protected void buildRecipes() {
        shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ICED_CRYSTAL_BLOCK.get())
                .pattern("AA")
                .pattern("AA")
                .define('A', ModItems.ICED_CRYSTAL.get())
                .unlockedBy(getHasName(ModItems.ICED_CRYSTAL.get()), has(ModItems.ICED_CRYSTAL))
                .group("iced_crystal")
                .save(output, "solarpancakestweaks:iced_crystal_from_iced_crystal_block");


        shapeless(RecipeCategory.FOOD, ModItems.GREEN_APPLE.get())
                .requires(Items.APPLE)
                .requires(Items.GREEN_DYE)
                .unlockedBy(getHasName(Items.APPLE), has(Items.APPLE))
                .save(output, "solarpancakestweaks:green_apple_from_apple");

    }
}
