package org.framework.browser;

import org.checkerframework.checker.units.qual.C;
import org.framework.config.ConfigManager;
import org.framework.driver.DesiredOptions;
import org.openqa.selenium.chrome.ChromeDriverLogLevel;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BrowserOptions implements DesiredOptions {

    ConfigManager configManager;


    public BrowserOptions(ConfigManager manager){
        this.configManager = manager;

    }

    /*Chrome Browser level Capabilities are set here.

     */
    public ChromeOptions chromeOptions(){
        ChromeOptions options = new ChromeOptions();
        options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.addArguments("--no-sandbox");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--start-maximised");
        options.addArguments("--incognito");
        options.addArguments("--disable-logging");
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        return options;
    }

    public FirefoxOptions firefoxOptions(){
        return new FirefoxOptions();
    }
}
