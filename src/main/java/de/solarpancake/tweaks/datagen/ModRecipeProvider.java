package de.solarpancake.tweaks.datagen;

import de.solarpancake.tweaks.Solarpancakestweaks;
import de.solarpancake.tweaks.block.ModBlocks;
import de.solarpancake.tweaks.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.references.ItemIds;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
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


        /* shaped recipe */

        shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ICED_CRYSTAL_BLOCK.get())
                .pattern("AA")
                .pattern("AA")
                .define('A', ModItems.ICED_CRYSTAL.get())
                .unlockedBy(getHasName(ModItems.ICED_CRYSTAL.get()), has(ModItems.ICED_CRYSTAL))
                .group("iced_crystal")
                .save(output, "solarpancakestweaks:iced_crystal_from_iced_crystal_block");

        shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ICED_CRYSTAL_BRICKS.get())
                .pattern("AA")
                .pattern("AA")
                .define('A', ModItems.ICED_CRYSTAL.get())
                .unlockedBy(getHasName(ModItems.ICED_CRYSTAL.get()), has(ModItems.ICED_CRYSTAL))
                .group("iced_crystal")
                .save(output, "solarpancakestweaks:iced_crystal_bricks_from_iced_crystal");

        shaped(RecipeCategory.MISC, ModItems.ICE_DETECTOR.get())
                .pattern("  B")
                .pattern(" A ")
                .pattern("A  ")
                .define('A', Items.STICK)
                .define('B', ModItems.ICED_CRYSTAL.get())
                .unlockedBy(getHasName(ModItems.ICED_CRYSTAL.get()), has(ModItems.ICED_CRYSTAL))
                .group("iced_crystal")
                .save(output, "solarpancakestweaks:ice_detector_craft");

        shaped(RecipeCategory.COMBAT, ModItems.ICED_CRYSTAL_HELMET.get())
                .pattern("AAA")
                .pattern("A A")
                .define('A', ModItems.ICED_CRYSTAL.get())
                .unlockedBy(getHasName(ModItems.ICED_CRYSTAL.get()), has(ModItems.ICED_CRYSTAL))
                .group("iced_crystal")
                .save(output, "solarpancakestweaks:iced_crystal_helmet_from_iced_crystal");

        shaped(RecipeCategory.COMBAT, ModItems.ICED_CRYSTAL_CHESTPLATE.get())
                .pattern("A A")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModItems.ICED_CRYSTAL.get())
                .unlockedBy(getHasName(ModItems.ICED_CRYSTAL.get()), has(ModItems.ICED_CRYSTAL))
                .group("iced_crystal")
                .save(output, "solarpancakestweaks:iced_crystal_chestplate_from_iced_crystal");

        shaped(RecipeCategory.COMBAT, ModItems.ICED_CRYSTAL_LEGGINGS.get())
                .pattern("AAA")
                .pattern("A A")
                .pattern("A A")
                .define('A', ModItems.ICED_CRYSTAL.get())
                .unlockedBy(getHasName(ModItems.ICED_CRYSTAL.get()), has(ModItems.ICED_CRYSTAL))
                .group("iced_crystal")
                .save(output, "solarpancakestweaks:iced_crystal_leggings_from_iced_crystal");

        shaped(RecipeCategory.COMBAT, ModItems.ICED_CRYSTAL_BOOTS.get())
                .pattern("A A")
                .pattern("A A")
                .define('A', ModItems.ICED_CRYSTAL.get())
                .unlockedBy(getHasName(ModItems.ICED_CRYSTAL.get()), has(ModItems.ICED_CRYSTAL))
                .group("iced_crystal")
                .save(output, "solarpancakestweaks:iced_crystal_boots_from_iced_crystal");

        shaped(RecipeCategory.COMBAT, ModItems.ICED_CRYSTAL_SWORD.get())
                .pattern("A")
                .pattern("A")
                .pattern("B")
                .define('A', ModItems.ICED_CRYSTAL.get())
                .define('B', ModItems.ICED_CRYSTAL_ROD.get())
                .unlockedBy(getHasName(ModItems.ICED_CRYSTAL.get()), has(ModItems.ICED_CRYSTAL))
                .group("iced_crystal")
                .save(output, "solarpancakestweaks:iced_crystal_sword_from_iced_crystal");

        shaped(RecipeCategory.COMBAT, ModItems.ICED_CRYSTAL_AXE.get())
                .pattern(" AA")
                .pattern(" BA")
                .pattern(" B ")
                .define('A', ModItems.ICED_CRYSTAL.get())
                .define('B', ModItems.ICED_CRYSTAL_ROD.get())
                .unlockedBy(getHasName(ModItems.ICED_CRYSTAL.get()), has(ModItems.ICED_CRYSTAL))
                .group("iced_crystal")
                .save(output, "solarpancakestweaks:iced_crystal_axe_from_iced_crystal");

        shaped(RecipeCategory.COMBAT, ModItems.ICED_CRYSTAL_PICKAXE.get())
                .pattern("AAA")
                .pattern(" B ")
                .pattern(" B ")
                .define('A', ModItems.ICED_CRYSTAL.get())
                .define('B', ModItems.ICED_CRYSTAL_ROD.get())
                .unlockedBy(getHasName(ModItems.ICED_CRYSTAL.get()), has(ModItems.ICED_CRYSTAL))
                .group("iced_crystal")
                .save(output, "solarpancakestweaks:iced_crystal_pickaxe_from_iced_crystal");

        shaped(RecipeCategory.COMBAT, ModItems.ICED_CRYSTAL_SHOVEL.get())
                .pattern("A")
                .pattern("B")
                .pattern("B")
                .define('A', ModItems.ICED_CRYSTAL.get())
                .define('B', ModItems.ICED_CRYSTAL_ROD.get())
                .unlockedBy(getHasName(ModItems.ICED_CRYSTAL.get()), has(ModItems.ICED_CRYSTAL))
                .group("iced_crystal")
                .save(output, "solarpancakestweaks:iced_crystal_shovel_from_iced_crystal");

        shaped(RecipeCategory.COMBAT, ModItems.ICED_CRYSTAL_HOE.get())
                .pattern(" AA")
                .pattern(" B ")
                .pattern(" B ")
                .define('A', ModItems.ICED_CRYSTAL.get())
                .define('B', ModItems.ICED_CRYSTAL_ROD.get())
                .unlockedBy(getHasName(ModItems.ICED_CRYSTAL.get()), has(ModItems.ICED_CRYSTAL))
                .group("iced_crystal")
                .save(output, "solarpancakestweaks:iced_crystal_hoe_from_iced_crystal");

        shaped(RecipeCategory.COMBAT, ModItems.ICED_CRYSTAL_SPEAR.get())
                .pattern("  A")
                .pattern(" B ")
                .pattern("B  ")
                .define('A', ModItems.ICED_CRYSTAL.get())
                .define('B', ModItems.ICED_CRYSTAL_ROD.get())
                .unlockedBy(getHasName(ModItems.ICED_CRYSTAL.get()), has(ModItems.ICED_CRYSTAL))
                .group("iced_crystal")
                .save(output, "solarpancakestweaks:iced_crystal_spear_from_iced_crystal");


        /* shapeless */

        shapeless(RecipeCategory.FOOD, ModItems.GREEN_APPLE.get())
                .requires(Items.APPLE)
                .requires(Items.DYE.green())
                .unlockedBy(getHasName(Items.APPLE), has(Items.APPLE))
                .save(output, "solarpancakestweaks:green_apple_from_apple");

        
        /* special/custom */

        stairBuilder(ModBlocks.ICED_CRYSTAL_BRICKS_STAIRS.get(), Ingredient.of(ModBlocks.ICED_CRYSTAL_BRICKS))
                .unlockedBy(getHasName(ModBlocks.ICED_CRYSTAL_BRICKS.get()), has(ModBlocks.ICED_CRYSTAL_BRICKS))
                .group("iced_crystal")
                .save(output);

        slab(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ICED_CRYSTAL_BRICKS_SLAB.get(), ModBlocks.ICED_CRYSTAL_BRICKS.get());


    }
}
