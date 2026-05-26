package org.framework.driver.mobile;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.framework.config.ConfigManager;
import org.framework.utils.LogManager;
import org.slf4j.Logger;

import java.io.File;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

public final class AppiumServiceManager {

    private static final Logger LOG =
            LogManager.getLogger(AppiumServiceManager.class);

    private static final ConfigManager CONFIG =
            ConfigManager.getInstance();

    private static final ConcurrentHashMap<String, AppiumDriverLocalService> SERVICES =
            new ConcurrentHashMap<>();

    private AppiumServiceManager() {}

    // =========================
    // Start Appium
    // =========================
    public static URL start(String deviceId) {

        String mode = CONFIG.getString("appium.mode", "local");

        if ("remote".equalsIgnoreCase(mode)) {
            LOG.info("Using remote Appium server (no local service started)");
            return getRemoteUrl();
        }

        return SERVICES.computeIfAbsent(deviceId, id -> {
            LOG.info("🚀 Starting Appium server for device {}", id);

            AppiumDriverLocalService service =
                    AppiumDriverLocalService.buildDefaultService();

            service.start();

            LOG.info("✅ Appium started for device {} at {}",
                    id, service.getUrl());

            return service;
        }).getUrl();
    }

    // =========================
    // Stop Appium
    // =========================
    public static void stop(String deviceId) {

        String mode = CONFIG.getString("appium.mode", "local");

        if ("remote".equalsIgnoreCase(mode)) {
            LOG.info("Remote Appium mode — no local service to stop");
            return;
        }

        AppiumDriverLocalService service = SERVICES.remove(deviceId);

        if (service != null && service.isRunning()) {
            LOG.info("🛑 Stopping Appium server for device {}", deviceId);
            service.stop();
        }

        DeviceAllocator.release(deviceId);
    }

    // =========================
    // Helpers
    // =========================
    private static URL getRemoteUrl() {
        try {
            return new URL(CONFIG.getString("appium.server.url"));
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Invalid appium.server.url for remote mode", e
            );
        }
    }
}