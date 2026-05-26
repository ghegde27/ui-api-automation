package org.framework.driver.providers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.framework.browser.BrowserOptions;
import org.framework.config.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public final class ChromeDriverProvider {

    private ChromeDriverProvider() {
        // utility class
    }

    public static WebDriver create() {

        // 1️⃣ Setup binary
        WebDriverManager.chromedriver().setup();

        // 2️⃣ Build options (headless, args, prefs, etc.)
        ChromeOptions options = new BrowserOptions(
                ConfigManager.getInstance()
        ).chromeOptions();

        // 3️⃣ Create driver
        return new ChromeDriver(options);
    }
}