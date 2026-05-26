package org.framework.redis;

import org.framework.config.ConfigManager;

public final class RedisConfig {

    private static final ConfigManager CONFIG = ConfigManager.getInstance();

    private RedisConfig() {
        // utility class
    }

    public static String host() {
        return CONFIG.getString("redis.host", "localhost");
    }

    public static int port() {
        return CONFIG.getInt("redis.port", 6379);
    }

    public static String password() {
        return CONFIG.getString("redis.password", "");
    }

    public static int database() {
        return CONFIG.getInt("redis.database", 0);
    }

    public static int connectionTimeoutMillis() {
        return CONFIG.getInt("redis.connection.timeout.ms", 10000);
    }

    public static int socketTimeoutMillis() {
        return CONFIG.getInt("redis.socket.timeout.ms", 10000);
    }

    public static int maxTotal() {
        return CONFIG.getInt("redis.max.total", 8);
    }

    public static int maxIdle() {
        return CONFIG.getInt("redis.max.idle", 8);
    }

    public static int minIdle() {
        return CONFIG.getInt("redis.min.idle", 0);
    }
}
