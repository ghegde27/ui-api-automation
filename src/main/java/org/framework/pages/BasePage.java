package org.framework.pages;

import com.fasterxml.jackson.core.type.TypeReference;
import org.framework.config.ConfigManager;
import org.framework.parser.LocatorFactory;
import org.framework.utils.JacksonProvider;
import org.framework.utils.LogManager;
import org.framework.utils.WebUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebUtils ui;

    protected final ConfigManager config;
    protected final String platform;

    private final Map<String, Map<String, String>> locators;

    private static final Logger LOG =
            LogManager.getLogger(BasePage.class);

    protected BasePage(WebDriver driver, String locatorResource) {
        this.driver = Objects.requireNonNull(driver, "WebDriver cannot be null");
        this.ui = new WebUtils(driver);
        this.config = ConfigManager.getInstance();
        this.platform = config.getString("platform").toLowerCase();
        this.locators = loadLocators(locatorResource);
    }

    // =========================
    // Locator resolution
    // =========================
    protected By locator(String name) {
        String raw = resolveRawLocator(name);
        return LocatorFactory.getLocator(raw);
    }

    protected String locatorValue(String name) {
        return LocatorFactory.getLocatorValue(resolveRawLocator(name));
    }

    private String resolveRawLocator(String name) {
        Objects.requireNonNull(name, "Locator name cannot be null");

        Map<String, String> entry = locators.get(name);
        if (entry == null) {
            throw new IllegalArgumentException(
                    "Locator key not found in repository: " + name
            );
        }

        // Primary platform lookup
        String raw = entry.get(platform);

        // Optional fallback (web → default)
        if (raw == null) {
            raw = entry.get("web");
        }
        if (raw == null) {
            raw = entry.get("default");
        }

        if (raw == null) {
            throw new IllegalArgumentException(
                    "No locator found for key '" + name +
                            "' | platform=" + platform +
                            " | available=" + entry.keySet()
            );
        }

        return raw;
    }

    // =========================
    // Locator loading
    // =========================
    private Map<String, Map<String, String>> loadLocators(String resource) {

        LOG.debug("Loading locator file: {}", resource);

        try (InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream(resource)) {

            if (is == null) {
                throw new IllegalArgumentException(
                        "Locator resource not found on classpath: " + resource
                );
            }

            return JacksonProvider.mapper()
                    .readValue(is, new TypeReference<>() {});

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to load locator resource: " + resource, e
            );
        }
    }
}