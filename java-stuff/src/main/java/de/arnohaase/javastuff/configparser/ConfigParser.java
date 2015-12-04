package de.arnohaase.javastuff.configparser;


import java.util.concurrent.TimeUnit;

public class ConfigParser {
    private final String text;
    private int offs = 0;

    public ConfigParser(String text) {
        this.text = text;
    }

    public Expr parseExpression() {
        return parseAdditiveExpression();
    }

    Expr parseAdditiveExpression() {
        final Expr left = parseMultiplicativeExpression();
        if (lookahead("+")) {
            return new BinaryExpr("+", left, parseMultiplicativeExpression());
        }
        if (lookahead("-")) {
            return new BinaryExpr("-", left, parseMultiplicativeExpression());
        }
        return left;
    }

    Expr parseMultiplicativeExpression() {
        final Expr left = parsePrimitiveExpressionWithTimeUnit();
        if (lookahead("*")) {
            return new BinaryExpr("*", left, parsePrimitiveExpressionWithTimeUnit());
        }
        if (lookahead("/")) {
            return new BinaryExpr("/", left, parsePrimitiveExpressionWithTimeUnit());
        }
        return left;
    }

    Expr parsePrimitiveExpressionWithTimeUnit() {
        final Expr value = parsePrimitiveExpression();
        if (lookahead("seconds") || lookahead("s")) {
            return new DurationExpr(value, TimeUnit.SECONDS);
        }
        if (lookahead("milliseconds") || lookahead("millis") || lookahead("ms")) {
            return new DurationExpr(value, TimeUnit.MILLISECONDS);
        }
        if (lookahead("minutes") || lookahead("min")) {
            return new DurationExpr(value, TimeUnit.MINUTES);
        }
        return value;
    }

    Expr parsePrimitiveExpression() {
        if (Character.isDigit(nextChar())) {
            return new LongExpr(parseLong());
        }
        if (lookahead("(")) {
            final Expr result = parseExpression();
            consume(")");
            return result;
        }
        if (lookahead("${")) {
            final int start = offs;
            while (!eof() && text.charAt(offs)!='}') {
                offs += 1;
            }
            final Expr result = new PropExpr(text.substring(start, offs));
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

interface Expr {
}

class BinaryExpr implements Expr {
    public final String operator;
    public final Expr left;
    public final Expr right;

    public BinaryExpr(String operator, Expr left, Expr right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override public String toString() {
        return "(" + left + ")" + operator + "(" + right + ")";
    }
}

class DurationExpr implements Expr {
    public final Expr value;
    public final TimeUnit timeUnit;

    public DurationExpr(Expr value, TimeUnit timeUnit) {
        this.value = value;
        this.timeUnit = timeUnit;
    }

    @Override public String toString() {
        return value + " " + timeUnit;
    }
}

class LongExpr implements Expr {
    public final long value;

    public LongExpr(long value) {
        this.value = value;
    }

    @Override public String toString() {
        return String.valueOf(value);
    }
}

class PropExpr implements Expr {
    public final String propName;

    public PropExpr(String propName) {
        this.propName = propName;
    }

    @Override public String toString() {
        return "${" + propName + "}";
    }
}
