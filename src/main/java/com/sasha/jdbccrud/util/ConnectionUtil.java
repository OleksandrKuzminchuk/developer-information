package com.sasha.jdbccrud.util;

import com.sasha.jdbccrud.exception.NotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

import static com.sasha.jdbccrud.util.constant.Constants.PROPERTIES_FILE;

public class ConnectionUtil {
    private static ConnectionUtil instance;
    private ConnectionUtil() {}
    public static ConnectionUtil getInstance() {
        if (instance == null) {
            synchronized(ConnectionUtil.class) {
                if (instance == null) {
                    instance = new ConnectionUtil();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        try(InputStream input = ConnectionUtil.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)){
            Properties properties = new Properties();
            properties.load(input);
            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");
            Class.forName(properties.getProperty("db.driver"));
            return DriverManager.getConnection(url, user, password);
        } catch (IOException | ClassNotFoundException | SQLException e){
            throw new NotFoundException("Failed to establish database connection", e);
        }
    }

    public PreparedStatement getPreparedStatement(String sql) throws SQLException {
        return getConnection().prepareStatement(sql);
    }

    public PreparedStatement getPreparedStatementWithGeneratedKey(String sql, int autoGeneratedKeys) throws SQLException {
        return getConnection().prepareStatement(sql, autoGeneratedKeys);
    }

    public PreparedStatement getPreparedStatementWithResultSetType(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return getConnection().prepareStatement(sql, resultSetType, resultSetConcurrency);
    }
}

