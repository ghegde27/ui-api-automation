package org.framework.test;

import org.framework.config.ConfigManager;

import java.util.List;

public final class ConfigValidator {

    private static final ConfigManager CONFIG =
            ConfigManager.getInstance();

    private ConfigValidator() {}

    public static void validate() {

        // Required keys
        List<String> requiredKeys = List.of(
                "platform"
        );

        for (String key : requiredKeys) {
            CONFIG.getString(key); // throws if missing
        }

        String platform = CONFIG.getString("platform").toLowerCase();

        if (!List.of("web", "android", "ios", "mweb").contains(platform)) {
            throw new IllegalArgumentException(
                    "Invalid platform value: " + platform
            );
        }
    }
}