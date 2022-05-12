package ru.nsu.fit.yakovlev.lab2.database;

import ru.nsu.fit.yakovlev.lab2.generated.Tag;

import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Statement;

public class TagRawDAO implements ITagDAO {
    private Statement statement;
    private final String INSERT =
            "INSERT INTO tags (node_id, k, v) VALUES ('";

    public TagRawDAO(DBConnection connection) throws SQLException {
        statement = connection.getConnection().createStatement();
    }

    @Override
    public void insertTag(Tag tag, BigInteger nodeId) throws SQLException {
        statement.execute(INSERT + nodeId + "', '" + tag.getK() + "', '" + tag.getV().replace("'", "") + "')");
    }
}
