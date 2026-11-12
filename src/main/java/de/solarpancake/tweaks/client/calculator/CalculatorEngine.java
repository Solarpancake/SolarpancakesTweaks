package de.solarpancake.tweaks.client.calculator;

import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.UnaryOperator;

/**
 * Plain calculator math, no Minecraft classes involved (so it can be tested on its own).
 * <p>
 * Supports {@code + - * /}, parentheses, decimals and unary minus, with normal precedence
 * ({@code 2+3*4 = 14}). Uses {@link BigDecimal}, so {@code 0.1+0.2} really is {@code 0.3}.
 * The display symbols {@code ×} and {@code ÷} are accepted as well.
 * <p>
 * Coordinates: {@code (x, y, z)} is a coordinate value. Multiplying or dividing it by a number scales
 * X and Z only (Y stays, like the Nether ↔ Overworld conversion): {@code (800, 64, -160) ÷ 8 = (100, 64, -20)}.
 * Every other operation with a coordinate is invalid.
 */
public final class CalculatorEngine {
    private static final MathContext MATH = MathContext.DECIMAL64; // 16 significant digits
    private static final int MAX_PLAIN_LENGTH = 16;
    // Results beyond 1E999 count as "too large" (BigDecimal itself would go much further).
    private static final int MAX_EXPONENT = 999;
    /** Whether × and ÷ also scale Y. False = Nether/Overworld style (only X and Z). */
    private static final boolean SCALE_Y = false;

    private CalculatorEngine() {
    }

    /** Thrown for anything that can't be calculated. {@link #getMessage()} is a lang key suffix. */
    public static final class CalcException extends Exception {
        public CalcException(String reason) {
            super(reason, null, false, false);
        }
    }

    /** A number, or a coordinate when {@code y}/{@code z} are set. */
    private record Value(BigDecimal x, @Nullable BigDecimal y, @Nullable BigDecimal z) {
        static Value of(BigDecimal number) {
            return new Value(number, null, null);
        }

        boolean isCoord() {
            return y != null;
        }

        /** Applies {@code op} to X and Z (and Y if {@link #SCALE_Y}). */
        Value scaled(UnaryOperator<BigDecimal> op) {
            return new Value(op.apply(x), SCALE_Y ? op.apply(y) : y, op.apply(z));
        }
    }

    /** Evaluates the expression and returns the formatted result (a number or {@code (x, y, z)}). */
    public static String evaluate(String expression) throws CalcException {
        if (expression.isBlank()) {
            throw new CalcException("invalid");
        }
        try {
            Parser parser = new Parser(expression);
            Value result = parser.parseExpression();
            parser.skipSpaces();
            if (!parser.atEnd()) {
                throw new CalcException("invalid");
            }
            if (!result.isCoord()) {
                return format(checkRange(result.x()));
            }
            return "(" + format(checkRange(result.x())) + ", " + format(checkRange(result.y()))
                    + ", " + format(checkRange(result.z())) + ")";
        } catch (ArithmeticException e) {
            // exponent overflow inside BigDecimal, e.g. 1E999999999 * 1E999999999
            throw new CalcException("too_large");
        }
    }

    /** True if the text is a coordinate result/token, e.g. {@code (12.5, 64, -3)}. */
    public static boolean isCoordinate(String text) {
        return text.startsWith("(") && text.endsWith(")") && text.indexOf(',') > 0;
    }

    private static BigDecimal checkRange(BigDecimal value) throws CalcException {
        if (value.signum() != 0 && Math.abs((long) value.precision() - value.scale() - 1) > MAX_EXPONENT) {
            throw new CalcException("too_large");
        }
        return value;
    }

    /** Turns a number into display text: no trailing zeros, scientific notation only when it gets too long. */
    public static String format(BigDecimal value) {
        BigDecimal rounded = value.round(MATH);
        if (rounded.signum() == 0) {
            return "0";
        }
        BigDecimal stripped = rounded.stripTrailingZeros();
        String plain = stripped.toPlainString();
        if (plain.replace("-", "").replace(".", "").length() <= MAX_PLAIN_LENGTH) {
            return plain;
        }
        // e.g. 1.2345E+20 -> 1.2345E20
        return stripped.toString().replace("E+", "E");
    }

    /** Recursive descent parser: expression -> term (+|- term)*, term -> factor (*|/ factor)*. */
    private static final class Parser {
        private final String text;
        private int pos;

        Parser(String text) {
            this.text = text;
        }

        Value parseExpression() throws CalcException {
            Value value = parseTerm();
            while (true) {
                skipSpaces();
                if (eat('+')) {
                    value = Value.of(number(value).add(number(parseTerm()), MATH));
                } else if (eat('-') || eat('−')) {
                    value = Value.of(number(value).subtract(number(parseTerm()), MATH));
                } else {
                    return value;
                }
            }
        }

        private Value parseTerm() throws CalcException {
            Value value = parseFactor();
            while (true) {
                skipSpaces();
                if (eat('*') || eat('×')) {
                    Value other = parseFactor();
                    if (value.isCoord() && other.isCoord()) {
                        throw new CalcException("invalid");
                    }
                    if (value.isCoord()) {
                        BigDecimal factor = other.x();
                        value = value.scaled(v -> v.multiply(factor, MATH));
                    } else if (other.isCoord()) {
                        BigDecimal factor = value.x();
                        value = other.scaled(v -> v.multiply(factor, MATH));
                    } else {
                        value = Value.of(value.x().multiply(other.x(), MATH));
                    }
                } else if (eat('/') || eat('÷')) {
                    BigDecimal divisor = number(parseFactor()); // dividing BY a coordinate makes no sense
                    if (divisor.signum() == 0) {
                        throw new CalcException("division_by_zero");
                    }
                    value = value.isCoord()
                            ? value.scaled(v -> v.divide(divisor, MATH))
                            : Value.of(value.x().divide(divisor, MATH));
                } else {
                    return value;
                }
            }
        }

        private Value parseFactor() throws CalcException {
            skipSpaces();
            if (eat('-') || eat('−')) {
                return Value.of(number(parseFactor()).negate());
            }
            if (eat('+')) {
                return Value.of(number(parseFactor()));
            }
            if (eat('(')) {
                Value first = parseExpression();
                skipSpaces();
                if (eat(',')) {
                    // coordinate (x, y, z)
                    BigDecimal y = number(parseExpression());
                    skipSpaces();
                    if (!eat(',')) {
                        throw new CalcException("invalid");
                    }
                    BigDecimal z = number(parseExpression());
                    skipSpaces();
                    if (!eat(')')) {
                        throw new CalcException("invalid");
                    }
                    return new Value(number(first), y, z);
                }
                // A missing ")" at the very end is forgiven, like most calculators do: "2*(3+4" = 14
                if (!eat(')') && !atEnd()) {
                    throw new CalcException("invalid");
                }
                return first;
            }
            return Value.of(parseNumber());
        }

        /** Unwraps a plain number; coordinates are not allowed in this spot. */
        private static BigDecimal number(Value value) throws CalcException {
            if (value.isCoord()) {
                throw new CalcException("invalid");
            }
            return value.x();
        }

        private BigDecimal parseNumber() throws CalcException {
            int start = pos;
            boolean dot = false;
            while (!atEnd()) {
                char c = text.charAt(pos);
                if (c >= '0' && c <= '9') {
                    pos++;
                } else if (c == '.' && !dot) {
                    dot = true;
                    pos++;
                } else {
                    break;
                }
            }
            // optional exponent, so results like 1.5E20 can be calculated with again
            if (pos > start && !atEnd() && (text.charAt(pos) == 'E' || text.charAt(pos) == 'e')) {
                int save = pos;
                pos++;
                if (!atEnd() && (text.charAt(pos) == '-' || text.charAt(pos) == '+')) {
                    pos++;
                }
                int digitsStart = pos;
                while (!atEnd() && Character.isDigit(text.charAt(pos))) {
                    pos++;
                }
                if (pos == digitsStart) {
                    pos = save;
                }
            }
            String number = text.substring(start, pos);
            if (number.isEmpty() || number.equals(".")) {
                throw new CalcException("invalid");
            }
            try {
                return new BigDecimal(number.startsWith(".") ? "0" + number : number);
            } catch (NumberFormatException e) {
                throw new CalcException("invalid");
            }
        }

        void skipSpaces() {
            while (!atEnd() && text.charAt(pos) == ' ') {
                pos++;
            }
        }

        boolean atEnd() {
            return pos >= text.length();
        }

        private boolean eat(char c) {
            if (!atEnd() && text.charAt(pos) == c) {
                pos++;
                return true;
            }
            return false;
        }
    }
}
