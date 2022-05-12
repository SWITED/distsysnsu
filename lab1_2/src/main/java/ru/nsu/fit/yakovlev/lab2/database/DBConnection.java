package ru.nsu.fit.yakovlev.lab2.database;

import lombok.Getter;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String DB_STRUCTURE_PATH = "ds.sql";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/ds";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "1234";

    @Getter
    private final Connection connection;

    public DBConnection() throws SQLException, IOException {
        InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(DB_STRUCTURE_PATH);
        connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        connection.setAutoCommit(false);

        assert inputStream != null;
        String dbStructureSql = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        connection.createStatement().execute(dbStructureSql);
    }
}
