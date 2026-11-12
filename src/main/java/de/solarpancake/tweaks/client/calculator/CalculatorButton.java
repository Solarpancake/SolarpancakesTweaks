package de.solarpancake.tweaks.client.calculator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;

/**
 * Flat, bevelled calculator key (AE2-ish look), drawn with plain fills so no texture is needed.
 */
public class CalculatorButton extends AbstractButton {
    /** Colour scheme per key type: base fill + label colour. */
    public enum Style {
        NUMBER(0xFFE2E2E2, 0xFF303030),
        OPERATOR(0xFFB4B4B4, 0xFF303030),
        ACTION(0xFFB4B4B4, 0xFFA01818),
        EQUALS(0xFF3F76E4, 0xFFFFFFFF);

        final int base;
        final int text;

        Style(int base, int text) {
            this.base = base;
            this.text = text;
        }
    }

    private static final int OUTLINE = 0xFF2B2B2B;

    private String label;
    private final Style style;
    private final Runnable action;

    public CalculatorButton(int x, int y, int width, int height, String label, Style style, Runnable action) {
        super(x, y, width, height, Component.literal(label));
        this.label = label;
        this.style = style;
        this.action = action;
    }

    public void setLabel(String label) {
        this.label = label;
        setMessage(Component.literal(label));
    }

    @Override
    public void onPress(InputWithModifiers input) {
        action.run();
    }

    // Clicking a key must not focus it, otherwise Enter/Space would press that key again instead of "=".
    @Override
    public boolean shouldTakeFocusAfterInteraction() {
        return false;
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        int x0 = getX();
        int y0 = getY();
        int x1 = x0 + width;
        int y1 = y0 + height;
        int base = isHoveredOrFocused() ? CalculatorScreen.shade(style.base, 0x18) : style.base;

        graphics.fill(x0, y0, x1, y1, OUTLINE);
        graphics.fill(x0 + 1, y0 + 1, x1 - 1, y1 - 1, CalculatorScreen.shade(base, -0x40));   // bottom/right shadow
        graphics.fill(x0 + 1, y0 + 1, x1 - 2, y1 - 2, CalculatorScreen.shade(base, 0x30));    // top/left highlight
        graphics.fill(x0 + 2, y0 + 2, x1 - 2, y1 - 2, base);

        Font font = Minecraft.getInstance().font;
        int textX = x0 + (width - font.width(label) + 1) / 2;
        int textY = y0 + (height - 8) / 2 + 1;
        graphics.text(font, label, textX, textY, style.text, false);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        defaultButtonNarrationText(output);
    }
}
