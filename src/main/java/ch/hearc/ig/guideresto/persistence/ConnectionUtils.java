package ch.hearc.ig.guideresto.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Provide helper methods to deal with database connections.
 * Uses ThreadLocal to manage one connection per thread.
 *
 * @author arnaud.geiser
 * @author alain.matile
 */
public class ConnectionUtils {

    private static final Logger logger = LogManager.getLogger();

    // ThreadLocal pour gérer une connexion par thread
    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    public static Connection getConnection() {
        try {
            Connection conn = connectionHolder.get();

            // Vérifier si la connexion existe et est valide
            if (conn == null || conn.isClosed()) {
                // Load database credentials from resources/database.properties
                ResourceBundle dbProps = ResourceBundle.getBundle("database");
                String url = dbProps.getString("database.url");
                String username = dbProps.getString("database.username");
                String password = dbProps.getString("database.password");

                logger.info("Trying to connect to user schema '{}' with JDBC string '{}'", username, url);

                conn = DriverManager.getConnection(url, username, password);
                conn.setAutoCommit(false);
                connectionHolder.set(conn);
            }

            return conn;
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException("Failed to get database connection", ex);
        } catch (MissingResourceException ex) {
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException("Database configuration not found", ex);
        }
    }

    public static void closeConnection() {
        Connection conn = connectionHolder.get();
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.error("Error closing connection: {}", e.getMessage(), e);
            } finally {
                connectionHolder.remove();
            }
        }
    }

    public static void commit() throws SQLException {
        Connection conn = connectionHolder.get();
        if (conn != null && !conn.isClosed()) {
            conn.commit();
        }
    }

    public static void rollback() throws SQLException {
        Connection conn = connectionHolder.get();
        if (conn != null && !conn.isClosed()) {
            conn.rollback();
        }
    }
}
