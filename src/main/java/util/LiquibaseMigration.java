package util;

import exception.NotFoundException;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.SQLException;

import static util.ConnectionUtil.getConnection;
import static util.constant.Constants.CHANGELOG_FILE;

public class LiquibaseMigration {
    public static void migrate(){
        try (Connection connection = getConnection();
        Liquibase liquibase = new Liquibase(CHANGELOG_FILE, new ClassLoaderResourceAccessor(), new JdbcConnection(connection))){
            liquibase.validate();
            liquibase.update("");
            System.out.println("Database migration completed successfully!");
            liquibase.forceReleaseLocks();
        }catch (LiquibaseException | SQLException e){
            throw new NotFoundException("Database migration failed: " + e);
        }
    }
}
