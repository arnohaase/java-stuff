package de.arnohaase.javastuff.configparser;

import java.util.concurrent.TimeUnit;


public class TimeoutParser {
    private final String text;
    private int offs = 0;

    public TimeoutParser(String text) {
        this.text = text;
    }

    public Expression parseExpression() {
        return parseAdditiveExpression();
    }

    Expression parseAdditiveExpression() {
        final Expression left = parseMultiplicativeExpression();
        if (lookahead("+")) {
            return new BinaryExpression("+", left, parseMultiplicativeExpression());
        }
        if (lookahead("-")) {
            return new BinaryExpression("-", left, parseMultiplicativeExpression());
        }
        return left;
    }

    Expression parseMultiplicativeExpression() {
        final Expression left = parsePrimitiveExpressionWithTimeUnit();
        if (lookahead("*")) {
            return new BinaryExpression("*", left, parsePrimitiveExpressionWithTimeUnit());
        }
        if (lookahead("/")) {
            return new BinaryExpression("/", left, parsePrimitiveExpressionWithTimeUnit());
        }
        return left;
    }

    Expression parsePrimitiveExpressionWithTimeUnit() {
        final Expression value = parsePrimitiveExpression();
        if (lookahead("seconds") || lookahead("s")) {
            return new DurationExpression(value, TimeUnit.SECONDS);
        }
        if (lookahead("milliseconds") || lookahead("millis") || lookahead("ms")) {
            return new DurationExpression(value, TimeUnit.MILLISECONDS);
        }
        if (lookahead("minutes") || lookahead("min")) {
            return new DurationExpression(value, TimeUnit.MINUTES);
        }
        return value;
    }

    Expression parsePrimitiveExpression() {
        if (Character.isDigit(nextChar())) {
            return new LongExpression(parseLong());
        }
        if (lookahead("(")) {
            final Expression result = parseExpression();
            consume(")");
            return result;
        }
        if (lookahead("${")) {
            final int start = offs;
            while (!eof() && text.charAt(offs)!='}') {
                offs += 1;
            }
            final Expression result = new PropExpression(text.substring(start, offs));
            consume("}");
            return result;
        }
        throw new IllegalStateException("syntax error at offs " + offs);
    }

    char nextChar() {
        consumeWhitespace();
        return text.charAt(offs);
    }

    void consume(String s) {
        consumeWhitespace();
        if (!lookahead(s)) {
            throw new IllegalStateException ("syntax error at offs " + offs + ": expected " + s);
        }
    }

    boolean lookahead(String s) {
        consumeWhitespace();
        if (text.length() < offs + s.length()) {
            return false;
        }

        for (int i=0; i<s.length(); i++) {
            if (s.charAt(i) != text.charAt(offs+i)) {
                return false;
            }
        }

        offs += s.length();
        return true;
    }

    long parseLong() {
        int start = offs;
        while (! eof() && Character.isDigit(text.charAt(offs))) {
            offs += 1;
        }
        return Long.parseLong(text.substring(start, offs));
    }

    void consumeWhitespace() {
        while (! eof() && Character.isWhitespace(text.charAt(offs))) {
            offs += 1;
        }
    }

    boolean eof() {
        return offs >= text.length();
    }
}

interface Expression {}

class BinaryExpression implements Expression {
    public final String operator;
    public final Expression left;
    public final Expression right;

    public BinaryExpression(String operator, Expression left, Expression right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override public String toString() {
        return "(" + left + ")" + operator + "(" + right + ")";
    }
}

class DurationExpression implements Expression {
    public final Expression value;
    public final TimeUnit timeUnit;

    public DurationExpression(Expression value, TimeUnit timeUnit) {
        this.value = value;
        this.timeUnit = timeUnit;
    }

    @Override public String toString() {
        return value + " " + timeUnit;
    }
}

class LongExpression implements Expression {
    public final long value;

    public LongExpression(long value) {
        this.value = value;
    }

    @Override public String toString() {
        return String.valueOf(value);
    }
}

class PropExpression implements Expression {
    public final String propName;

    public PropExpression(String propName) {
        this.propName = propName;
    }

    @Override public String toString() {
        return "${" + propName + "}";
    }
}
