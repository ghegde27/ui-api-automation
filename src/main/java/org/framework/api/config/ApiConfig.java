package org.framework.api.config;

import org.framework.config.ConfigManager;

public final class ApiConfig {

    private static final ConfigManager CONFIG = ConfigManager.getInstance();

    private ApiConfig() {
        // utility class
    }

    public static String baseUrl() {
        return CONFIG.getString("api.base.url");
    }

    public static int connectionTimeoutMillis() {
        return CONFIG.getInt("api.connection.timeout.ms", 10000);
    }

    public static int socketTimeoutMillis() {
        return CONFIG.getInt("api.socket.timeout.ms", 30000);
    }

    public static String defaultContentType() {
        return CONFIG.getString("api.default.content.type", "application/json");
    }

    public static String bearerToken() {
        return CONFIG.getString("api.auth.bearer.token", "");
    }

    public static String apiKeyHeader() {
        return CONFIG.getString("api.auth.api.key.header", "");
    }

    public static String apiKeyValue() {
        return CONFIG.getString("api.auth.api.key.value", "");
    }

    public static boolean loggingEnabled() {
        return CONFIG.getBoolean("api.logging.enabled", true);
    }
}
