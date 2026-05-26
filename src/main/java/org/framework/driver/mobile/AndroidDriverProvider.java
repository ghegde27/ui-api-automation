package org.framework.driver.mobile;

import io.appium.java_client.android.AndroidDriver;
import org.framework.driver.core.DriverManager;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public final class AndroidDriverProvider {

    private AndroidDriverProvider() {}

    public static void create(DesiredCapabilities caps) {

        String deviceId = DeviceAllocator.allocate();

        URL serverUrl =
                AppiumServerProvider.getServerUrl(deviceId);

        AndroidDriver driver =
                new AndroidDriver(serverUrl, caps);

        DriverManager.setDriver(driver);
        DriverManager.setDeviceId(deviceId);
    }
}