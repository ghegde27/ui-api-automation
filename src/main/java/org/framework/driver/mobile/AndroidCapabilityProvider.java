package org.framework.driver.mobile;

import io.appium.java_client.remote.options.BaseOptions;
import org.framework.config.ConfigManager;
import org.openqa.selenium.Capabilities;

public final class AndroidCapabilityProvider {

    private static final ConfigManager CONFIG =
            ConfigManager.getInstance();

    private AndroidCapabilityProvider() {
        // utility class
    }

    public static Capabilities build(String deviceId) {

        if (deviceId == null || deviceId.isBlank()) {
            throw new IllegalArgumentException(
                    "deviceId is mandatory for Android execution"
            );
        }

        BaseOptions options = new BaseOptions();

        // =========================
        // Mandatory platform caps
        // =========================
        options.amend("platformName", "Android");
        options.amend("appium:automationName", "UiAutomator2");
        options.amend("appium:deviceName", deviceId);
        options.amend("appium:udid", deviceId);
        options.amend("appium:autoLaunch", true);

        // =========================
        // App / Browser
        // =========================
        String app = CONFIG.getString("appium.app", "");

        // =========================
        // Decide execution mode
        // =========================
        boolean isBrowser =
                "browser".equalsIgnoreCase(
                        CONFIG.getString("appium.app", "")
                );

        if (isBrowser) {
            // -------- Mobile Web --------
            options.amend("browserName", "Chrome");

        } else {


            // =========================
            // Android specific
            // =========================
            amendIfPresent(options, "appium:appPackage", "appium.appPackage");
            amendIfPresent(options, "appium:appActivity", "appium.appActivity");
            amendIfPresent(options, "appium:appWaitActivity", "appium.appWaitActivity");

            // =========================
            // Stability & permissions
            // =========================
            amendIfPresent(options, "appium:newCommandTimeout", "appium.newCommandTimeout");
            amendIfPresent(options, "appium:autoGrantPermissions", "appium.autoGrantPermissions");
            amendIfPresent(options, "appium:noReset", "appium.noReset");
            amendIfPresent(options, "appium:skipDeviceInitialization", "appium.skipDeviceInit");

            // =========================
            // Remote / cloud metadata
            // =========================
            if (isRemote()) {
                amendIfPresent(options, "appium:platformVersion", "appium.platformVersion");
                amendIfPresent(options, "appium:build", "cloud.build");
                amendIfPresent(options, "appium:name", "cloud.testName");
            }


        }
        return options;
    }

    // =========================
    // Helpers
    // =========================
    private static boolean isRemote() {
        return "remote".equalsIgnoreCase(
                CONFIG.getString("appium.mode", "local")
        );
    }

    private static void amendIfPresent(BaseOptions options,
                                       String capName,
                                       String configKey) {

        String value = CONFIG.getString(configKey, "").trim();
        if (!value.isEmpty()) {
            options.amend(capName, value);
        }
    }
}