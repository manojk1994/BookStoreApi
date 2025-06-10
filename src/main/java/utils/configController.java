package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class configController {
    private static final Properties properties = new Properties();

    /**
     * Loads environment-specific properties from classpath resource.
     * Expects a system property 'env' (e.g., 'UATB', 'PROD') to select the correct file,
     * defaulting to 'UATB'.
     */
    public static void setUp() {
        String env = System.getProperty("env", "UATB");
        String configFile = String.format("application-%s.properties", env);
        try (InputStream input = configController.class
                .getClassLoader()
                .getResourceAsStream(configFile)) {
            if (input == null) {
                throw new RuntimeException("Unable to find properties file: " + configFile);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: " + configFile, e);
        }
    }

    /**
     * Retrieves a property value by key after setup has been called.
     */
    public static String get(String key) {
        return properties.getProperty(key);
    }

}