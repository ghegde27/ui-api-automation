package org.framework.database;

import org.framework.config.ConfigManager;

public final class DatabaseConfig {

    private static final ConfigManager CONFIG = ConfigManager.getInstance();

    private DatabaseConfig() {
        // utility class
    }

    public static String url() {
        return CONFIG.getString("db.url", "");
    }

    public static String username() {
        return CONFIG.getString("db.username", "");
    }

    public static String password() {
        return CONFIG.getString("db.password", "");
    }

    public static String driverClass() {
        return CONFIG.getString("db.driver.class", "");
    }

    public static int connectionTimeoutMillis() {
        return CONFIG.getInt("db.connection.timeout.ms", 10000);
    }
}
