package org.framework.driver.factory;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import org.framework.config.ConfigManager;
import org.framework.driver.core.DriverManager;
import org.framework.driver.mobile.AndroidCapabilityProvider;
import org.framework.driver.mobile.AppiumServerProvider;
import org.framework.driver.mobile.IOSCapabilityProvider;
import org.framework.utils.LogManager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import org.openqa.selenium.safari.SafariDriver;

import org.slf4j.Logger;

import java.time.Duration;

public final class DriverFactory {

    private static final Logger LOG =
            LogManager.getLogger(DriverFactory.class);

    private static final ConfigManager CONFIG =
            ConfigManager.getInstance();

    private DriverFactory() {
    }

    // =========================================================
    // MAIN FACTORY METHOD
    // =========================================================

    public static void create(String deviceId) {

        String platform =
                CONFIG.getString(
                        "platform",
                        "web"
                ).toLowerCase();

        LOG.info("-------------------------------------------------");
        LOG.info("🧩 DriverFactory invoked");
        LOG.info("Platform  : {}", platform);
        LOG.info("Thread ID : {}", Thread.currentThread().getId());
        LOG.info("-------------------------------------------------");

        switch (platform) {

            case "web":

                String browser =
                        CONFIG.getString(
                                "browser",
                                "chrome"
                        );

                createBrowser(browser);

                break;

            case "android":

                createAndroid(deviceId);

                break;

            case "ios":

                createIOS(deviceId);

                break;

            default:

                throw new IllegalArgumentException(
                        "Unsupported platform: "
                                + platform
                );
        }
    }

    // =========================================================
    // WEB DRIVER CREATION
    // =========================================================

    public static void createBrowser(
            String browser
    ) {

        LOG.info("🌐 Creating browser driver: {}", browser);

        WebDriver driver;

        switch (browser.toLowerCase()) {

            case "chrome":

                ChromeOptions chromeOptions =
                        new ChromeOptions();

                chromeOptions.addArguments(
                        "--remote-allow-origins=*"
                );

                chromeOptions.addArguments(
                        "--disable-notifications"
                );

                if (isHeadless()) {

                    chromeOptions.addArguments(
                            "--headless=new"
                    );
                }

                driver = new ChromeDriver(
                        chromeOptions
                );

                break;

            case "firefox":

                FirefoxOptions firefoxOptions =
                        new FirefoxOptions();

                if (isHeadless()) {

                    firefoxOptions.addArguments(
                            "-headless"
                    );
                }

                driver = new FirefoxDriver(
                        firefoxOptions
                );

                break;

            case "edge":

                EdgeOptions edgeOptions =
                        new EdgeOptions();

                if (isHeadless()) {

                    edgeOptions.addArguments(
                            "--headless=new"
                    );
                }

                driver = new EdgeDriver(
                        edgeOptions
                );

                break;

            case "safari":

                driver = new SafariDriver();

                break;

            default:

                throw new IllegalArgumentException(
                        "Unsupported browser: "
                                + browser
                );
        }

        configureDriver(driver);

        DriverManager.setDriver(driver);

        DriverManager.setBrowser(browser);

        DriverManager.setPlatform("web");

        LOG.info(
                "✅ Browser driver created successfully: {}",
                browser
        );
    }

    // =========================================================
    // ANDROID DRIVER CREATION
    // =========================================================

    public static void createAndroid(
            String deviceId
    ) {

        validateDeviceId(deviceId, "Android");

        LOG.info(
                "📱 Creating Android driver for device {}",
                deviceId
        );

        try {

            var caps =
                    AndroidCapabilityProvider.build(
                            deviceId
                    );

            var serverUrl =
                    AppiumServerProvider.getServerUrl(
                            deviceId
                    );

            WebDriver driver =
                    new AndroidDriver(
                            serverUrl,
                            caps
                    );

            configureDriver(driver);

            DriverManager.setDriver(driver);

            DriverManager.setDeviceId(deviceId);

            DriverManager.setPlatform("android");

            LOG.info(
                    "✅ Android driver created successfully"
            );

        } catch (Exception e) {

            LOG.error(
                    "❌ Failed to create Android driver",
                    e
            );

            throw new RuntimeException(e);
        }
    }

    // =========================================================
    // IOS DRIVER CREATION
    // =========================================================

    public static void createIOS(
            String deviceId
    ) {

        validateDeviceId(deviceId, "iOS");

        LOG.info(
                "📱 Creating iOS driver for device {}",
                deviceId
        );

        try {

            var caps =
                    IOSCapabilityProvider.build(
                            deviceId
                    );

            var serverUrl =
                    AppiumServerProvider.getServerUrl(
                            deviceId
                    );

            WebDriver driver =
                    new IOSDriver(
                            serverUrl,
                            caps
                    );

            configureDriver(driver);

            DriverManager.setDriver(driver);

            DriverManager.setDeviceId(deviceId);

            DriverManager.setPlatform("ios");

            LOG.info(
                    "✅ iOS driver created successfully"
            );

        } catch (Exception e) {

            LOG.error(
                    "❌ Failed to create iOS driver",
                    e
            );

            throw new RuntimeException(e);
        }
    }

    // =========================================================
    // COMMON DRIVER CONFIGURATION
    // =========================================================

    private static void configureDriver(
            WebDriver driver
    ) {

        int implicitWait =
                CONFIG.getInt(
                        "implicit.wait",
                        10
                );

        int pageLoadTimeout =
                CONFIG.getInt(
                        "page.load.timeout",
                        60
                );

        driver.manage()
                .timeouts()
                .implicitlyWait(
                        Duration.ofSeconds(
                                implicitWait
                        )
                );

        driver.manage()
                .timeouts()
                .pageLoadTimeout(
                        Duration.ofSeconds(
                                pageLoadTimeout
                        )
                );

        driver.manage()
                .window()
                .maximize();
    }

    // =========================================================
    // HEADLESS CONFIG
    // =========================================================

    private static boolean isHeadless() {

        return CONFIG.getBoolean(
                "headless",
                false
        );
    }

    // =========================================================
    // VALIDATIONS
    // =========================================================

    private static void validateDeviceId(
            String deviceId,
            String platform
    ) {

        if (
                deviceId == null
                        || deviceId.isBlank()
        ) {

            throw new IllegalArgumentException(
                    "deviceId must be provided for "
                            + platform
                            + " execution"
            );
        }
    }
}