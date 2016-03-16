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
        return eval (new TimeoutParser(props.getProperty(key)).parseExpression());
    }

    long eval (Expression expr) {
        if (expr instanceof LongExpression)     return ((LongExpression) expr).value;
        if (expr instanceof PropExpression)     return getDurationMillis (((PropExpression)expr).propName);
        if (expr instanceof DurationExpression) return evalDuration ((DurationExpression) expr);
        if (expr instanceof BinaryExpression)   return evalBinary ((BinaryExpression) expr);
        throw new IllegalArgumentException ("unsupported expression " + expr);
    }

    private long evalBinary (BinaryExpression expr) {
        switch (expr.operator) {
            case "+": return eval(expr.left) + eval(expr.right);
            case "-": return eval(expr.left) - eval(expr.right);
            case "*": return eval(expr.left) * eval(expr.right);
            case "/": return eval(expr.left) / eval(expr.right);
            default: throw new IllegalStateException ("unsupported operator " + expr.operator);
        }
    }

    private long evalDuration (DurationExpression expr) {
        return expr.timeUnit.toMillis (eval (expr.value));
    }
}

