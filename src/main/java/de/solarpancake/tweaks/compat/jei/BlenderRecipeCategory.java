package de.solarpancake.tweaks.compat.jei;

import de.solarpancake.tweaks.Solarpancakestweaks;
import de.solarpancake.tweaks.block.ModBlocks;
import de.solarpancake.tweaks.block.entity.BlenderBlockEntity;
import de.solarpancake.tweaks.item.ModItems;
import de.solarpancake.tweaks.recipe.BlenderRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeHolder;

/**
 * JEI page for the blender. Uses the middle part of the blender GUI texture (slots + pipes) as background, with
 * the pipes filling up like in the real GUI.
 */
public class BlenderRecipeCategory extends AbstractRecipeCategory<RecipeHolder<BlenderRecipe>> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(Solarpancakestweaks.MODID, "textures/gui/container/blender.png");

    // cut-out of the GUI texture: x 43..133, y 16..77 (all positions below are relative to it)
    private static final int CUT_X = 43;
    private static final int CUT_Y = 16;
    private static final int WIDTH = 91;
    private static final int HEIGHT = 62;

    private final IDrawableStatic background;
    private final IDrawableAnimated leftPipe;
    private final IDrawableAnimated rightPipe;
    private final IDrawableAnimated middlePipe;

    public BlenderRecipeCategory(IGuiHelper guiHelper, IRecipeHolderType<BlenderRecipe> type) {
        super(type, Component.translatable("block.solarpancakestweaks.blender_block"),
                guiHelper.createDrawableItemLike(ModBlocks.BLENDER_BLOCK.get()), WIDTH, HEIGHT);
        this.background = guiHelper.createDrawable(TEXTURE, CUT_X, CUT_Y, WIDTH, HEIGHT);

        // the filled pipe copy sits at u=176 in the texture (see BlenderScreen)
        int time = BlenderBlockEntity.BLEND_TIME;
        this.leftPipe = guiHelper.createAnimatedDrawable(
                guiHelper.createDrawable(TEXTURE, 176, 8, 25, 4), time, IDrawableAnimated.StartDirection.LEFT, false);
        this.rightPipe = guiHelper.createAnimatedDrawable(
                guiHelper.createDrawable(TEXTURE, 176 + 29, 8, 25, 4), time, IDrawableAnimated.StartDirection.RIGHT, false);
        this.middlePipe = guiHelper.createAnimatedDrawable(
                guiHelper.createDrawable(TEXTURE, 176 + 25, 0, 4, 18), time, IDrawableAnimated.StartDirection.TOP, false);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<BlenderRecipe> recipe, IFocusGroup focuses) {
        // inputs in the same order as the blender's slots (can, ingredient, ice), so JEI's "+" transfer lines up
        builder.addInputSlot(44 - CUT_X, 36 - CUT_Y).add(ModItems.EMPTY_CAN.get());
        builder.addInputSlot(80 - CUT_X, 17 - CUT_Y).add(recipe.value().input());
        builder.addInputSlot(116 - CUT_X, 36 - CUT_Y).add(ModItems.CRUSHED_ICED_CRYSTAL.get());
        builder.addOutputSlot(80 - CUT_X, 57 - CUT_Y).add(recipe.value().getResult());
    }

    @Override
    public void draw(RecipeHolder<BlenderRecipe> recipe, IRecipeSlotsView slots, GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
        background.draw(graphics);
        leftPipe.draw(graphics, 61 - CUT_X, 42 - CUT_Y);
        rightPipe.draw(graphics, 90 - CUT_X, 42 - CUT_Y);
        middlePipe.draw(graphics, 86 - CUT_X, 34 - CUT_Y);

        String time = (BlenderBlockEntity.BLEND_TIME / 20) + "s";
        graphics.text(Minecraft.getInstance().font, time, 0, HEIGHT - 9, 0xFF808080, false);
    }
}
