package org.framework.driver;

import org.framework.config.ConfigManager;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;

public interface DriverManager<T extends WebDriver> {
    ConfigManager configManager = ConfigManager.getInstance();

    T getDriver() throws MalformedURLException;

     T initialiseDriver() throws MalformedURLException;

     void killDriver(T driver);
}
