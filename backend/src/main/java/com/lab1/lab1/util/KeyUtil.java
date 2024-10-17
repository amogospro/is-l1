package com.lab1.lab1.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class KeyUtil {
    public static byte[] getSecretKey() {
        Properties properties = new Properties();
        try (InputStream input = KeyUtil.class.getResourceAsStream("/application.properties")) {
            properties.load(input);
            return properties.getProperty("SECRET_KEY").getBytes();
        } catch (IOException e) {
            return null;
        }
    }
}
