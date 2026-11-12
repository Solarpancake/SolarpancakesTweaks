package de.solarpancake.tweaks.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;

public class BlenderRecipe extends SingleItemRecipe {
    public static final MapCodec<BlenderRecipe> MAP_CODEC = simpleMapCodec(BlenderRecipe::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, BlenderRecipe> STREAM_CODEC = simpleStreamCodec(BlenderRecipe::new);

    public BlenderRecipe(Recipe.CommonInfo commonInfo, Ingredient ingredient, ItemStackTemplate result) {
        super(commonInfo, ingredient, result);
    }

    /** The result, public for recipe viewers (JEI). */
    public ItemStackTemplate getResult() {
        return result();
    }

    @Override
    public RecipeType<BlenderRecipe> getType() {
        return ModRecipes.BLENDING_TYPE.get();
    }

    @Override
    public RecipeSerializer<BlenderRecipe> getSerializer() {
        return ModRecipes.BLENDING_SERIALIZER.get();
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return ModRecipes.BLENDER_BOOK_CATEGORY.get();
    }
}
