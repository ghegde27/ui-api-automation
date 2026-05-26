package org.framework.driver.mobile;

import org.framework.config.ConfigManager;
import org.openqa.selenium.remote.DesiredCapabilities;

public final class IOSCapabilityProvider {

    private static final ConfigManager CONFIG =
            ConfigManager.getInstance();

    private IOSCapabilityProvider() {}

    public static DesiredCapabilities build(String deviceId) {

        if (deviceId == null || deviceId.isBlank()) {
            throw new IllegalArgumentException(
                    "deviceId is mandatory for iOS execution"
            );
        }

        DesiredCapabilities caps = new DesiredCapabilities();

        // =========================
        // Mandatory iOS caps
        // =========================
        caps.setCapability("platformName", "iOS");
        caps.setCapability("deviceName", deviceId);
        caps.setCapability("udid", deviceId);
        caps.setCapability("automationName", "XCUITest");

        // =========================
        // App / Browser
        // =========================
        String app = CONFIG.getString("appium.app");
        if (!"browser".equalsIgnoreCase(app)) {
            caps.setCapability("app", app);
        } else {
            caps.setCapability("browserName", "Safari");
        }

        // =========================
        // iOS specific
        // =========================
        setIfPresent(caps, "bundleId", "appium.bundleId");
        setIfPresent(caps, "useNewWDA", "appium.useNewWDA");
        setIfPresent(caps, "wdaLocalPort", "appium.wdaLocalPort");
        setIfPresent(caps, "autoAcceptAlerts", "appium.autoAcceptAlerts");

        // =========================
        // Stability
        // =========================
        setIfPresent(caps, "newCommandTimeout", "appium.newCommandTimeout");

        return caps;
    }

    private static void setIfPresent(DesiredCapabilities caps,
                                     String cap,
                                     String key) {
        try {
            String value = CONFIG.getString(key);
            if (value != null && !value.isBlank()) {
                caps.setCapability(cap, value);
            }
        } catch (Exception ignored) {}
    }
}