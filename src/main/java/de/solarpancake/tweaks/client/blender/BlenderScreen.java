package de.solarpancake.tweaks.client.blender;

import de.solarpancake.tweaks.Solarpancakestweaks;
import de.solarpancake.tweaks.menu.BlenderMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

/**
 * Blender GUI. The texture has the empty pipes drawn in; while blending, the filled (icy blue) copy of the pipe
 * area stored at u=176, v=0 is drawn over them: the side pipes fill towards the middle, the middle one downwards.
 */
public class BlenderScreen extends AbstractContainerScreen<BlenderMenu> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(Solarpancakestweaks.MODID, "textures/gui/container/blender.png");

    // pipe area in the GUI (x 61..114, y 34..51) and where its filled copy sits in the texture
    private static final int PIPES_X = 61;
    private static final int PIPES_Y = 34;
    private static final int FILLED_U = 176;
    private static final int FILLED_V = 0;

    private static final int SIDE_PIPE_LENGTH = 25; // left: x 61..85, right: x 90..114
    private static final int SIDE_PIPE_Y = 42;
    private static final int SIDE_PIPE_H = 4;
    private static final int SHAFT_X = 86;          // middle: x 86..89, y 34..46
    private static final int SHAFT_W = 4;
    private static final int SHAFT_H = 13;
    private static final int HEAD_X = 83;           // arrow head: x 83..92, y 47..51
    private static final int HEAD_W = 10;
    private static final int HEAD_H = 5;

    public BlenderScreen(BlenderMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);

        float progress = menu.getProgress();
        if (progress <= 0) {
            return;
        }
        int side = Math.round(progress * SIDE_PIPE_LENGTH);
        int down = Math.round(progress * (SHAFT_H + HEAD_H));

        blitFilled(graphics, PIPES_X, SIDE_PIPE_Y, side, SIDE_PIPE_H);                                  // can -> middle
        blitFilled(graphics, PIPES_X + 2 * SIDE_PIPE_LENGTH + SHAFT_W - side, SIDE_PIPE_Y, side, SIDE_PIPE_H); // ice -> middle
        blitFilled(graphics, SHAFT_X, PIPES_Y, SHAFT_W, Math.min(down, SHAFT_H));                        // ingredient -> down
        blitFilled(graphics, HEAD_X, PIPES_Y + SHAFT_H, HEAD_W, down - SHAFT_H);                         // arrow head
    }

    /** Draws part of the filled pipe copy at the same spot of the GUI. */
    private void blitFilled(GuiGraphicsExtractor graphics, int x, int y, int w, int h) {
        if (w <= 0 || h <= 0) {
            return;
        }
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos + x, topPos + y,
                FILLED_U + (x - PIPES_X), FILLED_V + (y - PIPES_Y), w, h, 256, 256);
    }
}
