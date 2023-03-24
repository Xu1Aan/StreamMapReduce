package com.xuan.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Xu1Aan
 * @Date: 2022/07/12/14:32
 * @Description:
 */
public abstract class Config {
    static Properties properties;
    static {
        try (InputStream in = Config.class.getResourceAsStream("/application.properties")) {
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    public static String getTopic() {
        String value = properties.getProperty("kafka.topic");
        if(value == null) {
            return "demo";
        } else {
            return value;
        }
    }
    public static long getSize() {
        String value = properties.getProperty("kafka.size");
        if(value == null) {
            return 10;
        } else {
            return Long.parseLong(value);
        }
    }

    public static int getDelay() {
        String value = properties.getProperty("kafka.delay");
        if(value == null) {
            return 5;
        } else {
            return Integer.parseInt(value);
        }
    }

    public static int getMachineNumbers() {
        String value = properties.getProperty("kafka.delay");
        if(value == null) {
            return 2;
        } else {
            return Integer.parseInt(value);
        }
    }
}
