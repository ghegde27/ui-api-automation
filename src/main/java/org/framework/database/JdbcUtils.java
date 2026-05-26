package org.framework.database;

import org.framework.utils.LogManager;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public final class JdbcUtils {

    private static final Logger LOG = LogManager.getLogger(JdbcUtils.class);

    private JdbcUtils() {
        // utility class
    }

    public static Connection getConnection() {
        validateDatabaseUrl();
        loadDriverIfConfigured();

        Properties properties = new Properties();
        properties.setProperty("connectTimeout", String.valueOf(DatabaseConfig.connectionTimeoutMillis()));

        if (!DatabaseConfig.username().isBlank()) {
            properties.setProperty("user", DatabaseConfig.username());
        }

        if (!DatabaseConfig.password().isBlank()) {
            properties.setProperty("password", DatabaseConfig.password());
        }

        try {
            return java.sql.DriverManager.getConnection(DatabaseConfig.url(), properties);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create JDBC connection for: " + DatabaseConfig.url(), e);
        }
    }

    public static List<Map<String, Object>> executeQuery(String sql, Object... parameters) {
        try (Connection connection = getConnection();
             PreparedStatement statement = prepare(connection, sql, parameters);
             ResultSet resultSet = statement.executeQuery()) {

            return toRows(resultSet);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to execute JDBC query: " + sql, e);
        }
    }

    public static Map<String, Object> executeQueryForSingleRow(String sql, Object... parameters) {
        List<Map<String, Object>> rows = executeQuery(sql, parameters);
        if (rows.isEmpty()) {
            return Map.of();
        }
        return rows.get(0);
    }

    public static int executeUpdate(String sql, Object... parameters) {
        try (Connection connection = getConnection();
             PreparedStatement statement = prepare(connection, sql, parameters)) {

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to execute JDBC update: " + sql, e);
        }
    }

    public static boolean isConnectionAvailable() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("SELECT 1");
            return true;
        } catch (Exception e) {
            LOG.warn("JDBC connection check failed", e);
            return false;
        }
    }

    private static PreparedStatement prepare(Connection connection, String sql, Object... parameters)
            throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        for (int index = 0; index < parameters.length; index++) {
            statement.setObject(index + 1, parameters[index]);
        }
        return statement;
    }

    private static List<Map<String, Object>> toRows(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> rows = new ArrayList<>();
        ResultSetMetaData metadata = resultSet.getMetaData();
        int columnCount = metadata.getColumnCount();

        while (resultSet.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                String columnName = metadata.getColumnLabel(columnIndex);
                row.put(columnName, resultSet.getObject(columnIndex));
            }
            rows.add(row);
        }

        return rows;
    }

    private static void loadDriverIfConfigured() {
        String driverClass = DatabaseConfig.driverClass();
        if (driverClass.isBlank()) {
            return;
        }

        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Configured JDBC driver class was not found: " + driverClass, e);
        }
    }

    private static void validateDatabaseUrl() {
        if (DatabaseConfig.url().isBlank()) {
            throw new IllegalStateException("Missing required config key: db.url");
        }
    }
}
