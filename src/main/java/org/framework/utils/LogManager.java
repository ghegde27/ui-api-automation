package org.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public final class LogManager {

    private LogManager() {
        // utility class
    }

    public static Logger getLogger(Class<?> clazz) {
        Objects.requireNonNull(clazz, "Logger class cannot be null");
        return LoggerFactory.getLogger(clazz);
    }

    public static Logger getLogger(String name) {
        Objects.requireNonNull(name, "Logger name cannot be null");
        return LoggerFactory.getLogger(name);
    }
}