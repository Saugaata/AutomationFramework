package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {

    Properties properties;

    public Configuration() {
        loadProperties();
    }

    private void loadProperties() {
        properties = new Properties();

        try {
            FileInputStream input = new FileInputStream(System.getProperty("user.dir") + "/config.properties");
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }
}
