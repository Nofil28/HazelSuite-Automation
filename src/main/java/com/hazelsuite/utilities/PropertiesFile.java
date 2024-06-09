package com.hazelsuite.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesFile {

    static String absolutePathToProp = Paths.get(System.getProperty("user.dir"), "src/main/java/com/hazelsuite/configuration/config.properties").toString();

    public static String readKey(String key) {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(absolutePathToProp)) {
            prop.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return prop.getProperty(key);
    }
}
