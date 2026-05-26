package org.framework.driver.mobile;

import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.framework.config.ConfigManager;

import java.net.MalformedURLException;
import java.net.URL;

public final class AppiumLocalBuilder {

    private static final ConfigManager configManager = ConfigManager.getInstance();
    private AppiumLocalBuilder() {}

    // =========================
    // Appium Server URL
    // =========================
    public static URL getServerUrl() {
        try {
            return new URL(configManager.getString("appium.server"));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Appium server URL", e);
        }
    }

    // =========================
    // Android Capabilities
    // =========================
    public static UiAutomator2Options androidCaps() {

        return new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2")
                .setDeviceName(configManager.getString("device.name"))
                .setAppPackage(configManager.getString("android.appPackage"))
                .setAppActivity(configManager.getString("android.appActivity"))
                .setNoReset(true)
                .autoGrantPermissions();
    }

    // =========================
    // iOS Capabilities
    // =========================
    public static XCUITestOptions iosCaps() {

        return new XCUITestOptions()
                .setPlatformName("iOS")
                .setAutomationName("XCUITest")
                .setDeviceName(configManager.getString("device.name"))
                .setBundleId(configManager.getString("ios.bundleId"))
                .setNoReset(true);
    }
}