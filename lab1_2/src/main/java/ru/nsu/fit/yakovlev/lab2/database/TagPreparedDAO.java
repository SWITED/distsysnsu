package ru.nsu.fit.yakovlev.lab2.database;

import lombok.Getter;
import ru.nsu.fit.yakovlev.lab2.generated.Tag;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Getter
public class TagPreparedDAO implements ITagDAO {
    private PreparedStatement statement;
    private static final String INSERT =
            "INSERT INTO tags (node_id, k, v) VALUES (?, ?, ?)";

    public TagPreparedDAO(DBConnection connection) throws SQLException {
        statement = connection.getConnection().prepareStatement(INSERT);
    }

    public void prepareStatement(Tag tag, BigInteger nodeId) throws SQLException {
        statement.setLong(1, nodeId.longValue());
        statement.setString(2, tag.getK());
        statement.setString(3, tag.getV().replace("'", ""));
    }

    @Override
    public void insertTag(Tag tag, BigInteger nodeId) throws SQLException {
        prepareStatement(tag, nodeId);
        statement.executeUpdate();
    }
}
