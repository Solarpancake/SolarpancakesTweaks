package de.solarpancake.tweaks.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;

/**
 * Everything the calculator remembers, stored on the item stack as a data component.
 * Because it lives on the stack, it survives closing the GUI, relogging, and travels with the item.
 *
 * @param input        what is currently typed in (the "display")
 * @param history      standard mode: finished calculations, newest last (e.g. "3+4 = 7")
 * @param coordHistory coord mode: saved coordinates (clicked blocks + results), newest last (e.g. "(1, 64, -3)")
 * @param mode         standard calculator, or coordinate mode (left click a block = input its coords)
 */
public record CalculatorData(String input, List<String> history, List<String> coordHistory, Mode mode) {
    public static final int MAX_INPUT_LENGTH = 256;
    public static final int MAX_HISTORY = 20;
    public static final int MAX_HISTORY_ENTRY_LENGTH = 512;

    public static final CalculatorData EMPTY = new CalculatorData("", List.of(), List.of(), Mode.STANDARD);

    public enum Mode {
        STANDARD("standard"),
        COORD("coord");

        public static final Codec<Mode> CODEC = Codec.STRING.xmap(Mode::byName, Mode::getSerializedName);
        public static final StreamCodec<ByteBuf, Mode> STREAM_CODEC =
                ByteBufCodecs.BOOL.map(coord -> coord ? COORD : STANDARD, mode -> mode == COORD);

        private final String name;

        Mode(String name) {
            this.name = name;
        }

        public String getSerializedName() {
            return name;
        }

        public static Mode byName(String name) {
            return COORD.name.equals(name) ? COORD : STANDARD;
        }
    }

    public static final Codec<CalculatorData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf("input", "").forGetter(CalculatorData::input),
            Codec.STRING.listOf().optionalFieldOf("history", List.of()).forGetter(CalculatorData::history),
            Codec.STRING.listOf().optionalFieldOf("coord_history", List.of()).forGetter(CalculatorData::coordHistory),
            Mode.CODEC.optionalFieldOf("mode", Mode.STANDARD).forGetter(CalculatorData::mode)
    ).apply(instance, CalculatorData::new));

    public static final StreamCodec<ByteBuf, CalculatorData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.stringUtf8(MAX_INPUT_LENGTH), CalculatorData::input,
            ByteBufCodecs.stringUtf8(MAX_HISTORY_ENTRY_LENGTH).apply(ByteBufCodecs.list(MAX_HISTORY)), CalculatorData::history,
            ByteBufCodecs.stringUtf8(MAX_HISTORY_ENTRY_LENGTH).apply(ByteBufCodecs.list(MAX_HISTORY)), CalculatorData::coordHistory,
            Mode.STREAM_CODEC, CalculatorData::mode,
            CalculatorData::new);

    public CalculatorData {
        // Always keep only the newest MAX_HISTORY entries, also for data loaded from disk,
        // so the network codec (which allows at most MAX_HISTORY) can never fail.
        history = newest(history);
        coordHistory = newest(coordHistory);
    }

    private static List<String> newest(List<String> list) {
        return List.copyOf(list.size() > MAX_HISTORY ? list.subList(list.size() - MAX_HISTORY, list.size()) : list);
    }

    private static List<String> appended(List<String> list, String entry) {
        List<String> result = new ArrayList<>(list);
        result.add(entry);
        return result;
    }

    public CalculatorData withInput(String newInput) {
        return new CalculatorData(newInput, history, coordHistory, mode).sanitized();
    }

    /** Switches the mode and clears the input (an expression from the other mode makes no sense there). */
    public CalculatorData withMode(Mode newMode) {
        return new CalculatorData("", history, coordHistory, newMode);
    }

    /** Standard mode: appends a finished calculation, dropping the oldest entries above {@link #MAX_HISTORY}. */
    public CalculatorData withHistoryEntry(String entry) {
        return new CalculatorData(input, appended(history, entry), coordHistory, mode).sanitized();
    }

    /** Coord mode: saves a coordinate (skipped if it equals the newest one), max {@link #MAX_HISTORY}. */
    public CalculatorData withCoordHistoryEntry(String coord) {
        if (!coordHistory.isEmpty() && coordHistory.getLast().equals(coord)) {
            return this;
        }
        return new CalculatorData(input, history, appended(coordHistory, coord), mode).sanitized();
    }

    /** Clears the history of the current mode only. */
    public CalculatorData withClearedHistory() {
        return mode == Mode.COORD
                ? new CalculatorData(input, history, List.of(), mode)
                : new CalculatorData(input, List.of(), coordHistory, mode);
    }

    /** Text form of a block position, as the calculator reads it: {@code (x, y, z)}. */
    public static String coordinateToken(BlockPos pos) {
        return "(" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")";
    }

    /** Left click on a block in coord mode: the clicked coordinate replaces the input and is saved in the coord history. */
    public CalculatorData withCoordinateInput(BlockPos pos) {
        String token = coordinateToken(pos);
        return withInput(token).withCoordHistoryEntry(token);
    }

    /** Clamps sizes - used on everything coming from the client before it is stored. */
    public CalculatorData sanitized() {
        String safeInput = input.length() > MAX_INPUT_LENGTH ? input.substring(0, MAX_INPUT_LENGTH) : input;
        return new CalculatorData(safeInput, clampEntries(history), clampEntries(coordHistory), mode);
    }

    private static List<String> clampEntries(List<String> list) {
        return list.stream()
                .map(s -> s.length() > MAX_HISTORY_ENTRY_LENGTH ? s.substring(0, MAX_HISTORY_ENTRY_LENGTH) : s)
                .toList();
    }
}
