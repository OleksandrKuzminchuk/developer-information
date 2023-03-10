package util;

import exception.NotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static util.constant.Constants.PROPERTIES_FILE;

public class ConnectionUtil {
    public static Connection getConnection(){
        try(InputStream input = ConnectionUtil.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)){
            Properties properties = new Properties();
            properties.load(input);
            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");
            Class.forName(properties.getProperty("db.driver"));
            return DriverManager.getConnection(url, user, password);
        }catch (IOException | ClassNotFoundException | SQLException e){
            throw new NotFoundException("Failed to establish database connection", e);
        }
    }
}
