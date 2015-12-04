package de.arnohaase.javastuff.configparser;


import java.io.IOException;
import java.util.Properties;

public class ConfigMain {
    public static void main(String[] args) throws IOException {
        final DurationConfig config = new DurationConfig("/timeouts.properties");
        System.out.println(config.getDurationMillis("roundtrip"));
        System.out.println(config.getDurationMillis("complex"));
    }
}

class DurationConfig {
    final Properties props = new Properties();

    public DurationConfig(String filename) throws IOException {
        props.load(getClass().getResourceAsStream(filename));
    }

    public long getDurationMillis(String key) {
        return eval (new ConfigParser(props.getProperty(key)).parseExpression());
    }

    long eval (Expr expr) {
        if (expr instanceof LongExpr)     return ((LongExpr) expr).value;
        if (expr instanceof PropExpr)     return getDurationMillis (((PropExpr)expr).propName);
        if (expr instanceof DurationExpr) return evalDuration ((DurationExpr) expr);
        if (expr instanceof BinaryExpr)   return evalBinary ((BinaryExpr) expr);
        throw new IllegalArgumentException ("unsupported expression " + expr);
    }

    private long evalBinary (BinaryExpr expr) {
        switch (expr.operator) {
            case "+": return eval(expr.left) + eval(expr.right);
            case "-": return eval(expr.left) - eval(expr.right);
            case "*": return eval(expr.left) * eval(expr.right);
            case "/": return eval(expr.left) / eval(expr.right);
            default: throw new IllegalStateException ("unsupported operator " + expr.operator);
        }
    }

    private long evalDuration (DurationExpr expr) {
        return expr.timeUnit.toMillis (eval (expr.value));
    }
}

