package de.arnohaase.javastuff.configparser;


class IncParser {
    private final String text;
    private int offs = 0;

    IncParser(String text) {
        this.text = text;
    }

    Expression parsePrimitiveExpression() {
        if (Character.isDigit(nextChar())) {
            return new LongExpression(parseLong());
        }
        throw new IllegalStateException("syntax error at offs " + offs);
    }

    char nextChar() {
        consumeWhitespace();
        return text.charAt(offs);
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
