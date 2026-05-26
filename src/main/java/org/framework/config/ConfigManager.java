package org.framework.config;

import org.framework.utils.LogManager;
import org.slf4j.Logger;

import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public final class ConfigManager {

    private static final Logger LOG =
            LogManager.getLogger(ConfigManager.class);

    private final Properties properties = new Properties();

    // =========================
    // Singleton (safe + lazy)
    // =========================
    private static final class Holder {
        private static final ConfigManager INSTANCE = new ConfigManager();
    }

    private ConfigManager() {
        load();
    }

    public static ConfigManager getInstance() {
        return Holder.INSTANCE;
    }

    // =========================
    // Load config
    // =========================
    private void load() {

        final String resource = "config.properties";
        LOG.info("Loading framework configuration: {}", resource);

        try (InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream(resource)) {

            if (is == null) {
                throw new IllegalStateException(
                        "config.properties not found on classpath"
                );
            }

            properties.load(is);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to load config.properties", e
            );
        }
    }

    // =========================
    // Resolution rules
    // =========================
    private String resolve(String key) {

        Objects.requireNonNull(key, "Config key cannot be null");

        // 1️⃣ JVM override (-Dkey=value)
        String sysValue = System.getProperty(key);
        if (sysValue != null) {
            return sysValue;
        }

        // 2️⃣ config.properties
        String fileValue = properties.getProperty(key);
        if (fileValue != null) {
            return fileValue;
        }

        throw new IllegalArgumentException(
                "Missing required config key: " + key
        );
    }

    // =========================
    // Typed accessors
    // =========================
    public String getString(String key) {
        return resolve(key).trim();
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(resolve(key));
    }

    public int getInt(String key) {
        try {
            return Integer.parseInt(resolve(key));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid integer value for key: " + key, e
            );
        }
    }

    public long getLong(String key) {
        try {
            return Long.parseLong(resolve(key));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid long value for key: " + key, e
            );
        }
    }

    public Properties getAll() {
        return new Properties(properties);
    }


    public boolean getBoolean(String key, boolean defaultValue) {
        String value = resolveOptional(key);
        return (value == null || value.isBlank())
                ? defaultValue
                : Boolean.parseBoolean(value.trim());
    }


    public String getString(String key, String defaultValue) {
        String value = resolveOptional(key);
        return (value == null || value.trim().isEmpty())
                ? defaultValue
                : value.trim();
    }


    private String resolveOptional(String key) {

        Objects.requireNonNull(key, "Config key cannot be null");

        String sysValue = System.getProperty(key);
        if (sysValue != null) {
            return sysValue;
        }

        return properties.getProperty(key);
    }


    public int getInt(String key, int defaultValue) {
        String value = resolveOptional(key);

        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid integer value for key: " + key, e
            );
        }
    }

    public long getLong(String key, long defaultValue) {
        String value = resolveOptional(key);

        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid long value for key: " + key, e
            );
        }
    }
}