package org.framework.driver.providers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.framework.browser.BrowserOptions;
import org.framework.config.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxDriverProvider {

    public static WebDriver create() {

        // 1️⃣ Setup binary
        WebDriverManager.firefoxdriver().setup();

        // 2️⃣ Build options (headless, args, prefs, etc.)
        FirefoxOptions options = new BrowserOptions(
                ConfigManager.getInstance()
        ).firefoxOptions();

        // 3️⃣ Create driver
        return new FirefoxDriver(options);
    }
}
