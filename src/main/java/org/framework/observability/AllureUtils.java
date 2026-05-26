package org.framework.observability;

import io.qameta.allure.Allure;
import org.framework.utils.LogManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;

public final class AllureUtils {

    private static final Logger LOGGER =
            LogManager.getLogger(AllureUtils.class);

    private AllureUtils() {
        // utility class
    }

    /**
     * Attach screenshot to Allure report (best effort).
     * Never throws.
     */
    public static void attachScreenshot(WebDriver driver, String name) {

        if (driver == null) {
            LOGGER.warn("Skipping screenshot: driver is null");
            return;
        }

        try {
            byte[] screenshot =
                    ((TakesScreenshot) driver)
                            .getScreenshotAs(OutputType.BYTES);

            Allure.addAttachment(
                    name,
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    ".png"
            );

        } catch (Exception e) {
            LOGGER.warn(
                    "Unable to capture screenshot for Allure: {}",
                    e.getMessage()
            );
        }
    }
}