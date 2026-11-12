package de.solarpancake.tweaks.recipe;

import de.solarpancake.tweaks.Solarpancakestweaks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Solarpancakestweaks.MODID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, Solarpancakestweaks.MODID);
    public static final DeferredRegister<RecipeBookCategory> BOOK_CATEGORIES =
            DeferredRegister.create(Registries.RECIPE_BOOK_CATEGORY, Solarpancakestweaks.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BlenderRecipe>> BLENDING_SERIALIZER =
            SERIALIZERS.register("blending", () -> new RecipeSerializer<>(BlenderRecipe.MAP_CODEC, BlenderRecipe.STREAM_CODEC));
    public static final DeferredHolder<RecipeType<?>, RecipeType<BlenderRecipe>> BLENDING_TYPE =
            TYPES.register("blending", () -> RecipeType.simple(Identifier.fromNamespaceAndPath(Solarpancakestweaks.MODID, "blending")));
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> BLENDER_BOOK_CATEGORY =
            BOOK_CATEGORIES.register("blender", RecipeBookCategory::new);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
        BOOK_CATEGORIES.register(eventBus);
    }
}
