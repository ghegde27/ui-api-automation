package org.framework.browser;

import org.framework.config.ConfigManager;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BrowserOptions {

    private final ConfigManager config;

    public BrowserOptions(ConfigManager configManager) {
        this.config = configManager;
    }

    // =========================
    // Chrome
    // =========================
    public ChromeOptions chromeOptions() {

        ChromeOptions options = new ChromeOptions();

        // Security / stability
        options.setAcceptInsecureCerts(true);

        // Base arguments (always safe)
        options.addArguments(
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-notifications",
                "--disable-infobars"
        );

        // Headless (CI)
        if (config.getBoolean("headless")) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        } else {
            options.addArguments("--start-maximized");
        }

        // Incognito
        if (config.getBoolean("incognito")) {
            options.addArguments("--incognito");
        }

        // Chrome prefs
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);

        options.setExperimentalOption("prefs", prefs);

        // Exclude automation banner
        options.setExperimentalOption(
                "excludeSwitches",
                List.of("enable-automation")
        );

        return options;
    }

    // =========================
    // Firefox
    // =========================
    public FirefoxOptions firefoxOptions() {

        FirefoxOptions options = new FirefoxOptions();
        options.setAcceptInsecureCerts(true);

        if (config.getBoolean("headless")) {
            options.addArguments("-headless");
        }

        return options;
    }

    // =========================
    // Safari
    // =========================
    public SafariOptions safariOptions() {

        SafariOptions options = new SafariOptions();
        options.setAcceptInsecureCerts(true);

        return options;
    }
}