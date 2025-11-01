package cinema.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private String url;
    private String username;
    private String password;
    private String driver;

    private DatabaseConnection() {
        try {
            loadProperties();
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }

    private void loadProperties() {
        Properties props = new Properties();
        try {
            // Try to load from database folder
            FileInputStream fis = new FileInputStream("database/db.properties");
            props.load(fis);
            fis.close();
        } catch (IOException e) {
            // Use default values if file not found
            System.out.println("Using default database configuration");
            props.setProperty("db.url", "jdbc:mysql://localhost:3306/cinema_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
            props.setProperty("db.username", "root");
            props.setProperty("db.password", "");
            props.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");
        }

        this.url = props.getProperty("db.url");
        this.username = props.getProperty("db.username");
        this.password = props.getProperty("db.password");
        this.driver = props.getProperty("db.driver");
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, username, password);
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
}
