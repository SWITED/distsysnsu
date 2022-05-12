package ru.nsu.fit.yakovlev.lab2.database;

import lombok.Getter;
import ru.nsu.fit.yakovlev.lab2.generated.Node;
import ru.nsu.fit.yakovlev.lab2.generated.Tag;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Getter
public class NodePreparedDAO implements INodeDAO {

    private final PreparedStatement statement;
    private static final String INSERT =
            "INSERT INTO nodes (id, username, lon, lat) VALUES (?, ?, ?, ?)";

    public NodePreparedDAO(DBConnection connection) throws SQLException {
        statement = connection.getConnection().prepareStatement(INSERT);
    }

    @Override
    public void insertNode(Node node) throws SQLException {
        prepareStatement(node);
        statement.executeUpdate();

        for (Tag tag : node.getTag()) {
            DAOManager.manager.getTag().insertTag(tag, node.getId());
        }
    }

    protected void prepareStatement(Node node) throws SQLException {
        statement.setLong(1, node.getId().longValue());
        statement.setString(2, node.getUser().replace("'", ""));
        statement.setDouble(3, node.getLon());
        statement.setDouble(4, node.getLat());
    }
}
