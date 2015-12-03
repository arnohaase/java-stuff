package de.arnohaase.javastuff.configparser;


import java.util.Properties;

public class ConfigMain {
    public static void main(String[] args) {
        final Properties props = new Properties();
        props.setProperty("x", "3 * (1s + 2 minutes) + ${abc}");
        props.setProperty("abc", "10 seconds");

        final DurationConfig config = new DurationConfig(props);
        System.out.println(config.getDurationMillis("abc"));
        System.out.println(config.getDurationMillis("x"));
    }
}

class DurationConfig {
    final Properties props;

    public DurationConfig(Properties props) {
        this.props = props;
    }

    public long getDurationMillis(String key) {
        return new ConfigParser(props.getProperty(key)).parseExpression().eval(props);
    }
}
