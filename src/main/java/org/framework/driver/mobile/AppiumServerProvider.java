package org.framework.driver.mobile;

import org.framework.config.ConfigManager;
import org.framework.utils.LogManager;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;

public final class AppiumServerProvider {

    private static final Logger LOG =
            LogManager.getLogger(AppiumServerProvider.class);

    private static final ConfigManager CONFIG =
            ConfigManager.getInstance();

    private AppiumServerProvider() {}

    /**
     * Returns Appium server URL based on configuration.
     * Local  -> framework-managed Appium
     * Remote -> user-provided Appium URL
     */
    public static URL getServerUrl(String deviceId) {

        String mode = CONFIG.getString("appium.mode").toLowerCase();

        switch (mode) {

            case "local":
                LOG.info("Using LOCAL Appium server for device: {}", deviceId);
                return AppiumServiceManager.start(deviceId);

            case "remote":
                LOG.info("Using REMOTE Appium server (user provided)");
                return getRemoteUrl();

            default:
                throw new IllegalArgumentException(
                        "Invalid appium.mode value: " + mode +
                                " (expected local | remote)"
                );
        }
    }

    private static URL getRemoteUrl() {
        try {
            return new URL(CONFIG.getString("appium.server.url"));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(
                    "Invalid appium.server.url", e
            );
        }
    }
}