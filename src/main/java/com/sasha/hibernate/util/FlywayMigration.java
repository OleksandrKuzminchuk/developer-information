package com.sasha.hibernate.util;

import com.sasha.hibernate.util.constant.Constants;
import com.sasha.hibernate.exception.NotFoundException;
import org.flywaydb.core.Flyway;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.sasha.hibernate.util.constant.Constants.*;

public class FlywayMigration {
    private FlywayMigration() {
    }
    public static void migrate() {
        Properties properties = new Properties();
        try (InputStream input = FlywayMigration.class.getClassLoader().getResourceAsStream(Constants.HIBERNATE_PROPERTIES)) {
            properties.load(input);

            Flyway flyway = Flyway.configure()
                    .dataSource(properties.getProperty(HIBERNATE_CONNECTION_URL), properties.getProperty(HIBERNATE_CONNECTION_USERNAME), properties.getProperty(HIBERNATE_CONNECTION_PASSWORD))
                    .locations(Constants.CREATE_TABLES_FILE)
                    .load();
            flyway.migrate();

            System.out.println(DATABASE_MIGRATION_SUCCESSFULLY);
        } catch (IOException e) {
            throw new NotFoundException(DATABASE_MIGRATION_FAILED + e);
        }
    }
}
