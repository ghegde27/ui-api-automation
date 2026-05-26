package org.framework.driver.providers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.framework.browser.BrowserOptions;
import org.framework.config.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

public class SafariDriverProvider {


    public static WebDriver create() {

        // 1️⃣ Setup binary
        WebDriverManager.safaridriver().setup();

        // 2️⃣ Build options (headless, args, prefs, etc.)
        SafariOptions options = new BrowserOptions(
                ConfigManager.getInstance()
        ).safariOptions();

        // 3️⃣ Create driver
        return new SafariDriver(options);
    }
}






