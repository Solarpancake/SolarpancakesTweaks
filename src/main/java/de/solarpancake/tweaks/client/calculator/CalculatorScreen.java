package de.solarpancake.tweaks.client.calculator;

import de.solarpancake.tweaks.client.calculator.CalculatorButton.Style;
import de.solarpancake.tweaks.component.CalculatorData.Mode;
import de.solarpancake.tweaks.component.CalculatorData;
import de.solarpancake.tweaks.item.custom.CalculatorItem;
import de.solarpancake.tweaks.network.SaveCalculatorPayload;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * Calculator GUI: input bar on top, num pad bottom left, history (newest first) bottom right,
 * mode button (standard / coords) top right.
 * <p>
 * Coord mode: the input can only start with a coordinate, which comes from left clicking a block (or a
 * coordinate history entry). The keys then only add operations after it, e.g. {@code (800, 64, -160) ÷ 8}.
 * <p>
 * Works on a local copy ({@link #data}) while open. Every change goes through {@link #setData}.
 * When the screen is closed (ESC, anything) {@link #removed()} sends the final state to the
 * server, which writes it back onto the item stack. So typed input + history persist between openings.
 * <p>
 * Keyboard: digits, {@code + - * / x ( ) . ,}, Enter or {@code =} to calculate, Backspace, Delete/C to clear.
 * Clicking a history entry puts its result into the input.
 */
public class CalculatorScreen extends Screen {
    /* ---------- layout (all relative to the panel's top-left corner) ---------- */
    private static final int PANEL_W = 224;
    private static final int PANEL_H = 146;
    private static final int PAD = 7;

    private static final int MODE_W = 48;
    private static final int MODE_H = 12;
    private static final int MODE_Y = 3;
    private static final int CLEAR_W = 30;
    private static final int COPY_W = 9;
    private static final long COPIED_FEEDBACK_MS = 1000;

    private static final int INPUT_Y = 17;
    private static final int INPUT_H = 18;

    private static final int KEY_W = 22;
    private static final int KEY_H = 18;
    private static final int KEY_GAP = 2;
    private static final int PAD_Y = 41;
    private static final int PAD_W = 4 * KEY_W + 3 * KEY_GAP; // 94
    private static final int PAD_H = 5 * KEY_H + 4 * KEY_GAP; // 98

    private static final int HISTORY_X = PAD + PAD_W + 6;
    private static final int HISTORY_W = PANEL_W - PAD - HISTORY_X;
    private static final int ROW_H = 10;
    private static final int VISIBLE_ROWS = (PAD_H - 4) / ROW_H; // 9

    /* ---------- colours ---------- */
    private static final int PANEL_BG = 0xFFC6C6C6;
    private static final int PANEL_LIGHT = 0xFFFFFFFF;
    private static final int PANEL_DARK = 0xFF555555;
    private static final int OUTLINE = 0xFF000000;
    private static final int INSET_DARK = 0xFF373737;
    private static final int SCREEN_BG = 0xFF1C1C1C;
    private static final int LABEL = 0xFF404040;
    private static final int TEXT = 0xFFFFFFFF;
    private static final int TEXT_DIM = 0xFF9A9A9A;
    private static final int TEXT_ERROR = 0xFFFF5555;
    private static final int ROW_HOVER = 0xFF3A3A3A;
    private static final int SCROLLBAR = 0xFF7A7A7A;
    private static final int COPY_ICON = 0xFFB0B0B0;
    private static final int COPY_ICON_HOVER = 0xFFFFFFFF;

    private static final int MAX_INPUT_CHARS = 100;
    private static final String TIMES = "×";
    private static final String DIVIDE = "÷";
    private static final String OPERATORS = "+-" + TIMES + DIVIDE;

    private final InteractionHand hand;
    private final CalculatorData initialData;
    private CalculatorData data;

    private int left;
    private int top;
    private int scrollRows;
    /** True right after "=", the next digit then starts a new calculation instead of appending. */
    private boolean showingResult;
    /** Lang key of the last error, shown in the input bar until the next key press. */
    private @Nullable String errorKey;
    private @Nullable CalculatorButton modeButton;
    /** History index whose coords were copied last, and when (for the "Copied!" tooltip). */
    private int copiedIndex = -1;
    private long copiedAt;

    public CalculatorScreen(InteractionHand hand, CalculatorData data) {
        super(Component.translatable("gui.solarpancakestweaks.calculator.title"));
        this.hand = hand;
        this.initialData = data;
        this.data = data;
    }

    @Override
    protected void init() {
        super.init();
        left = (width - PANEL_W) / 2;
        top = (height - PANEL_H) / 2;
        clampScroll();

        modeButton = addRenderableWidget(new CalculatorButton(left + PANEL_W - PAD - MODE_W, top + MODE_Y, MODE_W, MODE_H,
                "", Style.OPERATOR, this::toggleMode));
        updateModeButton();

        CalculatorButton clearButton = addRenderableWidget(new CalculatorButton(
                left + PANEL_W - PAD - MODE_W - 2 - CLEAR_W, top + MODE_Y, CLEAR_W, MODE_H,
                Component.translatable("gui.solarpancakestweaks.calculator.clear").getString(), Style.ACTION, this::clearHistory));
        clearButton.setTooltip(Tooltip.create(Component.translatable("gui.solarpancakestweaks.calculator.clear.tooltip")));

        // Row by row, left to right.
        String[][] labels = {
                {"C", "(", ")", DIVIDE},
                {"7", "8", "9", TIMES},
                {"4", "5", "6", "-"},
                {"1", "2", "3", "+"},
                {"←", "0", ".", "="}
        };
        for (int row = 0; row < labels.length; row++) {
            for (int col = 0; col < 4; col++) {
                String label = labels[row][col];
                int x = left + PAD + col * (KEY_W + KEY_GAP);
                int y = top + PAD_Y + row * (KEY_H + KEY_GAP);
                addRenderableWidget(new CalculatorButton(x, y, KEY_W, KEY_H, label, styleFor(label), () -> press(label)));
            }
        }
    }

    private static Style styleFor(String label) {
        return switch (label) {
            case "=" -> Style.EQUALS;
            case "C", "←" -> Style.ACTION;
            case "+", "-", TIMES, DIVIDE, "(", ")" -> Style.OPERATOR;
            default -> Style.NUMBER;
        };
    }

    /* ---------- calculator logic ---------- */

    /** Single entry point for keys, used by both the buttons and the keyboard. */
    private void press(String key) {
        errorKey = null;
        if (isCoordMode()) {
            // Coord mode: nothing to type until a block was left clicked, and a coordinate result is never replaced.
            if (input().isEmpty()) {
                return;
            }
            if (showingResult && (Character.isDigit(key.charAt(0)) || key.equals(".") || key.equals("("))) {
                return;
            }
        }
        switch (key) {
            case "C" -> clear();
            case "←" -> backspace();
            case "=" -> calculate();
            case "." -> appendDot();
            case "(" -> openParenthesis();
            case ")" -> closeParenthesis();
            case "+", "-", TIMES, DIVIDE -> appendOperator(key);
            default -> appendDigit(key);
        }
    }

    private String input() {
        return data.input();
    }

    private boolean isCoordMode() {
        return data.mode() == Mode.COORD;
    }

    private void toggleMode() {
        errorKey = null;
        showingResult = false;
        scrollRows = 0;
        copiedIndex = -1;
        setData(data.withMode(isCoordMode() ? Mode.STANDARD : Mode.COORD));
        updateModeButton();
    }

    private void clearHistory() {
        scrollRows = 0;
        copiedIndex = -1;
        setData(data.withClearedHistory());
    }

    /** The history of the current mode: calculations in standard mode, saved coordinates in coord mode. */
    private List<String> shownHistory() {
        return isCoordMode() ? data.coordHistory() : data.history();
    }

    private void updateModeButton() {
        if (modeButton == null) {
            return;
        }
        String mode = isCoordMode() ? "coord" : "standard";
        modeButton.setLabel(Component.translatable("gui.solarpancakestweaks.calculator.mode." + mode).getString());
        modeButton.setTooltip(Tooltip.create(Component.translatable("gui.solarpancakestweaks.calculator.mode." + mode + ".tooltip")));
    }

    /** Length of the coordinate at the start of the input in coord mode, e.g. 11 for "(1, 64, -3)×8". */
    private int coordinateLength() {
        String in = input();
        return in.startsWith("(") ? in.indexOf(')') + 1 : 0;
    }

    private void setInput(String input, boolean result) {
        setData(data.withInput(input));
        showingResult = result;
    }

    private char lastChar() {
        String in = input();
        return in.isEmpty() ? 0 : in.charAt(in.length() - 1);
    }

    private boolean canAppend() {
        return input().length() < MAX_INPUT_CHARS;
    }

    private void clear() {
        setInput("", false);
    }

    private void backspace() {
        String in = input();
        if (in.isEmpty()) {
            return;
        }
        // the clicked coordinate is removed as a whole, never char by char
        if (isCoordMode() && in.length() <= coordinateLength()) {
            setInput("", false);
        } else {
            setInput(in.substring(0, in.length() - 1), false);
        }
    }

    private void appendDigit(String digit) {
        if (showingResult) {
            setInput(digit, false);
        } else if (canAppend()) {
            // after ")" a number means multiply: (2+3)4 -> (2+3)×4
            setInput(input() + (lastChar() == ')' ? TIMES : "") + digit, false);
        }
    }

    private void appendDot() {
        if (showingResult) {
            setInput("0.", false);
            return;
        }
        if (!canAppend() || currentNumber().contains(".")) {
            return;
        }
        String prefix = lastChar() == ')' ? TIMES + "0" : Character.isDigit(lastChar()) ? "" : "0";
        setInput(input() + prefix + ".", false);
    }

    private void appendOperator(String op) {
        String in = input();
        char last = lastChar();
        if (in.isEmpty() || last == '(') {
            // only a leading minus makes sense here
            if (op.equals("-") && canAppend()) {
                setInput(in + "-", false);
            }
            return;
        }
        if (OPERATORS.indexOf(last) >= 0) {
            boolean minusAfterTimesOrDivide = op.equals("-") && (last == TIMES.charAt(0) || last == DIVIDE.charAt(0));
            if (minusAfterTimesOrDivide) {
                if (canAppend()) {
                    setInput(in + op, false); // 3×-2
                }
                return;
            }
            // swap the operator(s) at the end: "3×-" + "+" -> "3+"
            String base = in;
            while (!base.isEmpty() && OPERATORS.indexOf(base.charAt(base.length() - 1)) >= 0) {
                base = base.substring(0, base.length() - 1);
            }
            if (!base.isEmpty() && !base.endsWith("(")) {
                setInput(base + op, false);
            }
            return;
        }
        if (canAppend()) {
            setInput(in + op, false);
        }
    }

    private void openParenthesis() {
        if (showingResult) {
            setInput("(", false);
        } else if (canAppend()) {
            char last = lastChar();
            boolean multiply = Character.isDigit(last) || last == ')' || last == '.';
            setInput(input() + (multiply ? TIMES : "") + "(", false);
        }
    }

    private void closeParenthesis() {
        char last = lastChar();
        if (canAppend() && openParentheses() > 0 && (Character.isDigit(last) || last == ')' || last == '.')) {
            setInput(input() + ")", false);
        }
    }

    private int openParentheses() {
        int open = 0;
        for (char c : input().toCharArray()) {
            if (c == '(') open++;
            else if (c == ')') open--;
        }
        return open;
    }

    /** The number currently being typed at the end of the input (used to block a 2nd decimal point). */
    private String currentNumber() {
        String in = input();
        int i = in.length();
        while (i > 0 && (Character.isDigit(in.charAt(i - 1)) || in.charAt(i - 1) == '.')) {
            i--;
        }
        return in.substring(i);
    }

    private void calculate() {
        String expression = input().strip();
        if (expression.isEmpty() || showingResult) {
            return;
        }
        try {
            String result = CalculatorEngine.evaluate(expression);
            CalculatorData updated = data.withInput(result);
            // "5 =" or a plain clicked coordinate: nothing was calculated, so nothing goes into the history
            if (!result.equals(expression)) {
                if (isCoordMode()) {
                    // coord mode: the resulting coordinate is saved like a clicked one
                    updated = CalculatorEngine.isCoordinate(result) ? updated.withCoordHistoryEntry(result) : updated;
                } else {
                    updated = updated.withHistoryEntry(expression + " = " + result);
                }
            }
            setData(updated);
            showingResult = true;
            scrollRows = 0;
        } catch (CalculatorEngine.CalcException e) {
            errorKey = "gui.solarpancakestweaks.calculator.error." + e.getMessage();
        }
    }

    /**
     * History click. Coord mode: the saved coordinate replaces the input.
     * Standard mode: the result is appended after an operator / "(", otherwise it replaces the input.
     */
    private void useHistoryResult(String entry) {
        if (isCoordMode()) {
            setInput(entry, true);
            return;
        }
        String result = resultOf(entry);
        if (CalculatorEngine.isCoordinate(result)) {
            return; // leftover from older versions, can't be used in standard mode
        }
        char last = lastChar();
        boolean append = !showingResult && (OPERATORS.indexOf(last) >= 0 || last == '(');
        if (append) {
            String value = result.startsWith("-") ? "(" + result + ")" : result;
            if (input().length() + value.length() <= MAX_INPUT_CHARS) {
                setInput(input() + value, false);
            }
        } else {
            setInput(result, true);
        }
    }

    private static String resultOf(String entry) {
        int i = entry.lastIndexOf(" = ");
        return i < 0 ? entry : entry.substring(i + 3);
    }

    /** "(12, -3, 45.5)" -> "12 -3 45.5", the format Minecraft commands use. */
    private static String minecraftFormat(String coord) {
        return coord.replace("(", "").replace(")", "").replace(",", "").strip();
    }

    private static String expressionOf(String entry) {
        int i = entry.lastIndexOf(" = ");
        return i < 0 ? "" : entry.substring(0, i);
    }

    /* ---------- input ---------- */

    @Override
    public boolean keyPressed(KeyEvent event) {
        switch (event.key()) {
            case 257, 335 -> { // Enter, numpad Enter
                press("=");
                return true;
            }
            case 259 -> { // Backspace
                press("←");
                return true;
            }
            case 261 -> { // Delete
                press("C");
                return true;
            }
            default -> {
                return super.keyPressed(event);
            }
        }
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        String key = switch (event.codepoint()) {
            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> String.valueOf((char) event.codepoint());
            case '.', ',' -> ".";
            case '+' -> "+";
            case '-' -> "-";
            case '*', 'x', 'X' -> TIMES;
            case '/', ':' -> DIVIDE;
            case '(' -> "(";
            case ')' -> ")";
            case '=' -> "=";
            case 'c', 'C' -> "C";
            default -> null;
        };
        if (key == null) {
            return super.charTyped(event);
        }
        press(key);
        return true;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (super.mouseClicked(event, doubleClick)) {
            return true;
        }
        if (event.button() == 0) {
            int index = historyIndexAt(event.x(), event.y());
            if (index >= 0 && isCoordMode() && isOverCopyIcon(event.x())) {
                minecraft.keyboardHandler.setClipboard(minecraftFormat(data.coordHistory().get(index)));
                copiedIndex = index;
                copiedAt = Util.getMillis();
                CalculatorButton.playButtonClickSound(minecraft.getSoundManager());
                return true;
            }
            if (index >= 0) {
                errorKey = null;
                useHistoryResult(shownHistory().get(index));
                CalculatorButton.playButtonClickSound(minecraft.getSoundManager());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
        if (isOverHistory(x, y) && scrollY != 0) {
            scrollRows -= (int) Math.signum(scrollY);
            clampScroll();
            return true;
        }
        return super.mouseScrolled(x, y, scrollX, scrollY);
    }

    /* ---------- history helpers ---------- */

    private void clampScroll() {
        scrollRows = Mth.clamp(scrollRows, 0, Math.max(0, shownHistory().size() - VISIBLE_ROWS));
    }

    private boolean isOverHistory(double x, double y) {
        int hx = left + HISTORY_X;
        int hy = top + PAD_Y;
        return x >= hx && x < hx + HISTORY_W && y >= hy && y < hy + PAD_H;
    }

    /** Index into {@code data.history()} under the mouse, or -1. Row 0 (top) is the newest entry. */
    private int historyIndexAt(double x, double y) {
        if (!isOverHistory(x, y)) {
            return -1;
        }
        int row = (int) (y - (top + PAD_Y + 2)) / ROW_H;
        if (y < top + PAD_Y + 2 || row >= VISIBLE_ROWS) {
            return -1;
        }
        int index = shownHistory().size() - 1 - (scrollRows + row);
        return index >= 0 ? index : -1;
    }

    /** Right edge of the row text area (leaves room for the scrollbar). */
    private int historyTextRight() {
        return left + HISTORY_X + HISTORY_W - (shownHistory().size() > VISIBLE_ROWS ? 6 : 3);
    }

    /** Coord mode: the copy icon sits at the right end of each row. */
    private boolean isOverCopyIcon(double x) {
        int right = historyTextRight();
        return x >= right - COPY_W && x < right + 1;
    }

    /* ---------- rendering ---------- */

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        int x0 = left;
        int y0 = top;
        int x1 = left + PANEL_W;
        int y1 = top + PANEL_H;

        // raised panel: black outline, white top/left, grey bottom/right
        graphics.fill(x0 + 1, y0, x1 - 1, y1, OUTLINE);
        graphics.fill(x0, y0 + 1, x1, y1 - 1, OUTLINE);
        graphics.fill(x0 + 1, y0 + 1, x1 - 1, y1 - 1, PANEL_DARK);
        graphics.fill(x0 + 1, y0 + 1, x1 - 3, y1 - 3, PANEL_LIGHT);
        graphics.fill(x0 + 3, y0 + 3, x1 - 3, y1 - 3, PANEL_BG);

        inset(graphics, left + PAD, top + INPUT_Y, PANEL_W - 2 * PAD, INPUT_H);
        inset(graphics, left + HISTORY_X, top + PAD_Y, HISTORY_W, PAD_H);
    }

    /** Sunken box (dark top/left edge, white bottom/right edge) with a dark "screen" inside. */
    private static void inset(GuiGraphicsExtractor graphics, int x, int y, int w, int h) {
        graphics.fill(x - 1, y - 1, x + w + 1, y + h + 1, PANEL_LIGHT);
        graphics.fill(x - 1, y - 1, x + w, y + h, INSET_DARK);
        graphics.fill(x, y, x + w, y + h, SCREEN_BG);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a); // buttons
        graphics.text(font, title, left + PAD + 1, top + 6, LABEL, false);
        extractInputBar(graphics);
        extractHistory(graphics, mouseX, mouseY);
    }

    private void extractInputBar(GuiGraphicsExtractor graphics) {
        int x = left + PAD;
        int y = top + INPUT_Y;
        int w = PANEL_W - 2 * PAD;
        int textY = y + (INPUT_H - 8) / 2;
        int right = x + w - 4;

        if (errorKey != null) {
            Component error = Component.translatable(errorKey);
            graphics.text(font, error, right - font.width(error), textY, TEXT_ERROR, false);
            return;
        }

        if (isCoordMode() && input().isEmpty()) {
            Component hint = Component.translatable("gui.solarpancakestweaks.calculator.coord_hint");
            graphics.text(font, hint, right - font.width(hint), textY, TEXT_DIM, false);
            return;
        }

        // blinking cursor while typing, right-aligned text, overflow cut off on the left
        boolean cursor = !showingResult && (Util.getMillis() / 400) % 2 == 0;
        int cursorW = font.width("_");
        String shown = font.plainSubstrByWidth(input(), w - 8 - cursorW, true);
        int textX = right - cursorW - font.width(shown);
        graphics.text(font, shown, textX, textY, TEXT, false);
        if (cursor) {
            graphics.text(font, "_", right - cursorW, textY, TEXT, false);
        }
    }

    private void extractHistory(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        int x = left + HISTORY_X;
        int y = top + PAD_Y;
        List<String> history = shownHistory();

        if (history.isEmpty()) {
            Component empty = Component.translatable("gui.solarpancakestweaks.calculator.history_empty");
            graphics.text(font, empty, x + (HISTORY_W - font.width(empty)) / 2, y + PAD_H / 2 - 4, TEXT_DIM, false);
            return;
        }

        boolean scrollbar = history.size() > VISIBLE_ROWS;
        int textLeft = x + 3;
        int textRight = historyTextRight();
        int hovered = historyIndexAt(mouseX, mouseY);

        for (int row = 0; row < VISIBLE_ROWS; row++) {
            int index = history.size() - 1 - (scrollRows + row);
            if (index < 0) {
                break;
            }
            String entry = history.get(index);
            int rowY = y + 2 + row * ROW_H;
            if (index == hovered) {
                graphics.fill(x + 1, rowY - 1, textRight + 2, rowY + ROW_H - 1, ROW_HOVER);
            }

            if (isCoordMode()) {
                extractCoordRow(graphics, entry, textLeft, textRight, rowY, index == hovered && isOverCopyIcon(mouseX));
                continue;
            }

            // "= result" right-aligned in white, expression left in grey, cut with "..." if needed
            String result = font.plainSubstrByWidth("= " + resultOf(entry), textRight - textLeft);
            int resultX = textRight - font.width(result);
            graphics.text(font, result, resultX, rowY, TEXT, false);

            int room = resultX - 4 - textLeft;
            String expression = expressionOf(entry);
            if (room > 0 && !expression.isEmpty()) {
                if (font.width(expression) > room) {
                    expression = font.plainSubstrByWidth(expression, room - font.width("...")) + "...";
                }
                graphics.text(font, expression, textLeft, rowY, TEXT_DIM, false);
            }
        }

        if (scrollbar) {
            int trackTop = y + 2;
            int trackH = PAD_H - 4;
            int maxScroll = history.size() - VISIBLE_ROWS;
            int thumbH = Math.max(8, trackH * VISIBLE_ROWS / history.size());
            int thumbY = trackTop + (trackH - thumbH) * scrollRows / maxScroll;
            graphics.fill(x + HISTORY_W - 3, thumbY, x + HISTORY_W - 1, thumbY + thumbH, SCROLLBAR);
        }

        if (hovered < 0) {
            return;
        }
        if (isCoordMode() && isOverCopyIcon(mouseX)) {
            boolean copied = hovered == copiedIndex && Util.getMillis() - copiedAt < COPIED_FEEDBACK_MS;
            graphics.setComponentTooltipForNextFrame(font, List.of(
                    copied ? Component.translatable("gui.solarpancakestweaks.calculator.copied")
                            : Component.translatable("gui.solarpancakestweaks.calculator.copy"),
                    Component.literal(minecraftFormat(history.get(hovered))).withStyle(ChatFormatting.GRAY)
            ), mouseX, mouseY);
            return;
        }
        String hint = isCoordMode() ? "gui.solarpancakestweaks.calculator.use_coord" : "gui.solarpancakestweaks.calculator.use_result";
        graphics.setComponentTooltipForNextFrame(font, List.of(
                Component.literal(history.get(hovered)),
                Component.translatable(hint).withStyle(ChatFormatting.GRAY)
        ), mouseX, mouseY);
    }

    /** Coord mode row: "x y z" on the left, copy icon on the right. */
    private void extractCoordRow(GuiGraphicsExtractor graphics, String coord, int textLeft, int textRight, int rowY, boolean iconHovered) {
        int iconX = textRight - COPY_W + 1;
        int room = iconX - 3 - textLeft;
        String text = minecraftFormat(coord);
        if (font.width(text) > room) {
            text = font.plainSubstrByWidth(text, room - font.width("...")) + "...";
        }
        graphics.text(font, text, textLeft, rowY, TEXT, false);

        // copy icon: two overlapping sheets (back one offset up-right)
        int color = iconHovered ? COPY_ICON_HOVER : COPY_ICON;
        int ix = iconX;
        int iy = rowY;
        graphics.outline(ix + 2, iy, 6, 6, color);
        graphics.fill(ix, iy + 2, ix + 6, iy + 8, SCREEN_BG);
        graphics.outline(ix, iy + 2, 6, 6, color);
    }

    /** Lightens (positive) or darkens (negative) every RGB channel of an ARGB colour. */
    static int shade(int argb, int amount) {
        int r = Mth.clamp(((argb >> 16) & 0xFF) + amount, 0, 255);
        int g = Mth.clamp(((argb >> 8) & 0xFF) + amount, 0, 255);
        int b = Mth.clamp((argb & 0xFF) + amount, 0, 255);
        return (argb & 0xFF000000) | (r << 16) | (g << 8) | b;
    }

    /* ---------- state ---------- */

    /** Use this for every change made in the GUI (typing, "=", clearing, ...). */
    protected void setData(CalculatorData newData) {
        this.data = newData.sanitized();
        clampScroll();
    }

    protected CalculatorData getData() {
        return data;
    }

    @Override
    public void removed() {
        if (!data.equals(initialData)) {
            ClientPacketDistributor.sendToServer(new SaveCalculatorPayload(hand, data));
            // Also update the client's copy right away, so a left click straight after closing already sees the new mode.
            ItemStack stack = minecraft.player != null ? minecraft.player.getItemInHand(hand) : ItemStack.EMPTY;
            if (stack.getItem() instanceof CalculatorItem) {
                CalculatorItem.setData(stack, data);
            }
        }
        super.removed();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    // Draw the world dimmed behind the panel (like inventories) instead of the menu background.
    @Override
    public boolean isInGameUi() {
        return true;
    }
}
