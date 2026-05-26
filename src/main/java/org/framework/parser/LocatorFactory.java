package org.framework.parser;

import io.appium.java_client.AppiumBy;
import org.framework.utils.LogManager;
import org.openqa.selenium.*;
import org.slf4j.Logger;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public final class LocatorFactory {

    private static final Logger LOG =
            LogManager.getLogger(LocatorFactory.class);

    private LocatorFactory() {
        // utility class
    }

    // =========================
    // Platform-aware entry point
    // =========================
    public static By getLocator(Map<String, String> platformLocators,
                                String platform) {

        Objects.requireNonNull(platformLocators, "Locator map cannot be null");
        Objects.requireNonNull(platform, "Platform cannot be null");

        String raw = platformLocators.get(platform.toLowerCase(Locale.ROOT));

        if (raw == null) {
            throw new IllegalArgumentException(
                    "No locator found for platform: " + platform +
                            " | Available: " + platformLocators.keySet()
            );
        }

        return getLocator(raw);
    }

    // =========================
    // Strategy=value resolver
    // =========================
    public static By getLocator(String raw) {

        ParsedLocator parsed = parse(raw);

        LOG.debug("Resolving locator [{}={}]", parsed.strategy, parsed.value);

        switch (parsed.strategy) {

            // ---------- Web ----------
            case "id":
                return By.id(parsed.value);

            case "name":
                return By.name(parsed.value);

            case "xpath":
                return By.xpath(parsed.value);

            case "css":
            case "cssselector":
                return By.cssSelector(parsed.value);

            case "classname":
                return By.className(parsed.value);

            case "tagname":
                return By.tagName(parsed.value);

            // ---------- Mobile ----------
            case "accessibilityid":
                return AppiumBy.accessibilityId(parsed.value);

            case "androiduiautomator":
                return AppiumBy.androidUIAutomator(parsed.value);

            case "iospredicate":
                return AppiumBy.iOSNsPredicateString(parsed.value);

            case "iosclasschain":
                return AppiumBy.iOSClassChain(parsed.value);

            default:
                throw new IllegalArgumentException(
                        "Unsupported locator strategy: " + parsed.strategy
                );
        }
    }

    public static String getLocatorValue(String raw) {
        return parse(raw).value;
    }

    public static String dynamic(String template,
                                 String placeholder,
                                 String value) {

        Objects.requireNonNull(template);
        Objects.requireNonNull(placeholder);
        Objects.requireNonNull(value);

        return template.replace(placeholder, value);
    }

    // =========================
    // Internal parsing
    // =========================
    private static ParsedLocator parse(String raw) {

        Objects.requireNonNull(raw, "Locator string cannot be null");

        int idx = raw.indexOf('=');
        if (idx <= 0 || idx == raw.length() - 1) {
            throw new IllegalArgumentException(
                    "Invalid locator format. Expected strategy=value but got: " + raw
            );
        }

        String strategy = raw.substring(0, idx)
                .trim()
                .toLowerCase(Locale.ROOT);

        String value = raw.substring(idx + 1).trim();

        return new ParsedLocator(strategy, value);
    }

    private static final class ParsedLocator {
        final String strategy;
        final String value;

        ParsedLocator(String strategy, String value) {
            this.strategy = strategy;
            this.value = value;
        }
    }
}