package org.framework.pages;

import org.openqa.selenium.WebDriver;

import java.lang.reflect.Constructor;
import java.util.Objects;

public final class PageFactory {

    private PageFactory() {
        // utility class
    }

    public static <T extends BasePage> T create(
            Class<T> pageClass,
            WebDriver driver
    ) {

        Objects.requireNonNull(pageClass, "Page class cannot be null");
        Objects.requireNonNull(driver, "WebDriver cannot be null");

        try {
            Constructor<T> constructor =
                    pageClass.getConstructor(WebDriver.class);

            return constructor.newInstance(driver);

        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(
                    "Page must have a public constructor with WebDriver parameter: "
                            + pageClass.getName(),
                    e
            );
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to create page instance: " + pageClass.getName(),
                    e
            );
        }
    }
}