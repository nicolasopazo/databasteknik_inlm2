package app;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class Main {
    static Connection connection = null;

    public static void main(String[] args) {
        CustomerUtil customerUtil = new CustomerUtil();

        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("src/app/Settings.properties"));
            connection = DriverManager.getConnection(
                    properties.getProperty("connectionString"),
                    properties.getProperty("name"),
                    properties.getProperty("password"));
            } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        customerUtil.login(connection);
    }
}