package org.framework.test;

import io.qameta.allure.Allure;
import org.framework.config.ConfigManager;

public final class EnvironmentReporter {

    private static final ConfigManager CONFIG =
            ConfigManager.getInstance();

    private EnvironmentReporter() {}

    public static void publish() {

        Allure.label("platform", CONFIG.getString("platform"));
        Allure.label("browser", CONFIG.getString("browser","chrome"));

        Allure.label("os", System.getProperty("os.name"));
        Allure.label("os.version", System.getProperty("os.version"));
        Allure.label("java.version", System.getProperty("java.version"));
    }
}