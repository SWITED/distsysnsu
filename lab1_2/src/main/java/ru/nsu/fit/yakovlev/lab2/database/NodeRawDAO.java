package ru.nsu.fit.yakovlev.lab2.database;

import ru.nsu.fit.yakovlev.lab2.generated.Node;
import ru.nsu.fit.yakovlev.lab2.generated.Tag;

import java.sql.SQLException;
import java.sql.Statement;

public class NodeRawDAO implements INodeDAO {
    private Statement statement;
    private static final String INSERT =
            "INSERT INTO nodes (id, username, lon, lat) VALUES ('";

    public NodeRawDAO(DBConnection connection) throws SQLException {
        statement = connection.getConnection().createStatement();
    }

    @Override
    public void insertNode(Node node) throws SQLException {
        statement.execute(INSERT + node.getId() + "', '" + node.getUser().replace("'", "") +
                "', '" + node.getLon() + "', '" + node.getLat() + "')");
        for (Tag tag : node.getTag()) {
            DAOManager.manager.getTag().insertTag(tag, node.getId());
        }
    }
}
