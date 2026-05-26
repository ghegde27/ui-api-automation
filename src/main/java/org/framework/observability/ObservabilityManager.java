package org.framework.observability;

import org.framework.driver.core.DriverManager;
import org.framework.utils.LogManager;

import org.openqa.selenium.*;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;

import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class ObservabilityManager {

    private static final Logger LOG =
            LogManager.getLogger(ObservabilityManager.class);

    private ObservabilityManager() {}

    // =====================================================
    // SCREENSHOT
    // =====================================================

    public static String captureScreenshot(
            String testName
    ) {

        return DriverManager.captureScreenshot(testName);
    }

    // =====================================================
    // BROWSER LOGS
    // =====================================================

    public static void captureBrowserLogs() {

        try {

            WebDriver driver =
                    DriverManager.getDriver();

            LogEntries logs =
                    driver.manage().logs().get("browser");

            for (LogEntry entry : logs) {

                LOG.error(
                        "BROWSER_CONSOLE => {}",
                        entry.getMessage()
                );
            }

        } catch (Exception e) {

            LOG.warn(
                    "Unable to fetch browser logs",
                    e
            );
        }
    }
}