package org.framework.driver.mobile;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.openqa.selenium.WebElement;

import java.net.URL;

public final class IOSDriverProvider {

    private IOSDriverProvider() {
        // utility class
    }

    public static IOSDriver create() {

        URL serverUrl = AppiumLocalBuilder.getServerUrl();
        XCUITestOptions options = AppiumLocalBuilder.iosCaps();

        return new IOSDriver(serverUrl, options);
    }
}