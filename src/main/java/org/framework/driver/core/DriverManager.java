package org.framework.driver.core;

import org.framework.utils.LogManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class DriverManager {

    private static final Logger LOG =
            LogManager.getLogger(DriverManager.class);

    /*
     * Thread-safe WebDriver storage
     */
    private static final ThreadLocal<WebDriver> DRIVER =
            new ThreadLocal<>();

    /*
     * Thread-safe browser storage
     */
    private static final ThreadLocal<String> BROWSER =
            new ThreadLocal<>();

    /*
     * Thread-safe device storage
     */
    private static final ThreadLocal<String> DEVICE_ID =
            new ThreadLocal<>();

    /*
     * Thread-safe platform storage
     */
    private static final ThreadLocal<String> PLATFORM =
            new ThreadLocal<>();

    private DriverManager() {
        // utility class
    }

    // =========================================================
    // DRIVER METHODS
    // =========================================================

    public static void setDriver(WebDriver driver) {

        DRIVER.set(driver);
    }

    public static WebDriver getDriver() {

        WebDriver driver = DRIVER.get();

        if (driver == null) {

            throw new IllegalStateException(
                    "Driver is NULL for thread: "
                            + Thread.currentThread().getId()
            );
        }

        return driver;
    }

    public static boolean hasDriver() {

        return DRIVER.get() != null;
    }

    // =========================================================
    // BROWSER METHODS
    // =========================================================

    public static void setBrowser(String browser) {

        BROWSER.set(browser);
    }

    public static String getBrowser() {

        return BROWSER.get();
    }

    // =========================================================
    // DEVICE METHODS
    // =========================================================

    public static void setDeviceId(String deviceId) {

        DEVICE_ID.set(deviceId);
    }

    public static String getDeviceId() {

        return DEVICE_ID.get();
    }

    // =========================================================
    // PLATFORM METHODS
    // =========================================================

    public static void setPlatform(String platform) {

        PLATFORM.set(platform);
    }

    public static String getPlatform() {

        return PLATFORM.get();
    }

    // =========================================================
    // SCREENSHOT METHODS
    // =========================================================

    public static String captureScreenshot(
            String screenshotName
    ) {

        WebDriver driver = getDriver();

        if (driver == null) {

            LOG.warn(
                    "Cannot capture screenshot. Driver is null."
            );

            return null;
        }

        try {

            File source =
                    ((TakesScreenshot) driver)
                            .getScreenshotAs(OutputType.FILE);

            String directory =
                    System.getProperty("user.dir")
                            + "/test-output/screenshots/";

            Files.createDirectories(
                    Paths.get(directory)
            );

            String filePath =
                    directory
                            + screenshotName
                            + "_"
                            + System.currentTimeMillis()
                            + ".png";

            Path destination =
                    Paths.get(filePath);

            Files.copy(
                    source.toPath(),
                    destination
            );

            LOG.info(
                    "Screenshot saved at: {}",
                    filePath
            );

            return filePath;

        } catch (IOException e) {

            LOG.error(
                    "Failed to capture screenshot",
                    e
            );

            return null;
        }
    }

    // =========================================================
    // QUIT DRIVER
    // =========================================================

    public static void quit() {

        WebDriver driver = DRIVER.get();

        try {

            if (driver != null) {

                LOG.info(
                        "Closing driver for thread: {}",
                        Thread.currentThread().getId()
                );

                driver.quit();

                LOG.info(
                        "Driver closed successfully"
                );
            }

        } catch (Exception e) {

            LOG.error(
                    "Error while quitting driver",
                    e
            );

        } finally {

            removeAll();
        }
    }

    // =========================================================
    // CLEANUP
    // =========================================================

    public static void removeAll() {

        DRIVER.remove();

        DEVICE_ID.remove();

        BROWSER.remove();

        PLATFORM.remove();

        LOG.info(
                "ThreadLocal resources cleaned successfully"
        );
    }
}