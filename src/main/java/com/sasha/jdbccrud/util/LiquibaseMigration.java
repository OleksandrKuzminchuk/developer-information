package com.sasha.jdbccrud.util;

import com.sasha.jdbccrud.exception.NotFoundException;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.SQLException;

import static com.sasha.jdbccrud.util.constant.Constants.CHANGELOG_FILE;

public class LiquibaseMigration {
    private static final ConnectionUtil connectionUtil = ConnectionUtil.getInstance();
    private LiquibaseMigration() {}
    public static void migrate(){
        try (Connection connection = connectionUtil.getConnection();
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
