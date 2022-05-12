package ru.nsu.fit.yakovlev.lab2.database;

import ru.nsu.fit.yakovlev.lab2.generated.Node;
import ru.nsu.fit.yakovlev.lab2.generated.Tag;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeBatchDAO extends NodePreparedDAO {
    private static final int BATCH_SIZE = 1000;
    private final List<Node> nodesToInsert = new ArrayList<>();

    public NodeBatchDAO(DBConnection connection) throws SQLException {
        super(connection);
    }

    @Override
    public void insertNode(Node node) throws SQLException {
        nodesToInsert.add(node);
        if (nodesToInsert.size() >= BATCH_SIZE) {
            insertNodes();
        }
    }

    private void insertNodes() throws SQLException {
        Map<Tag, BigInteger> tagsToInsert = new HashMap<>();

        for (Node node : nodesToInsert) {
            prepareStatement(node);
            getStatement().addBatch();

            for (Tag tag : node.getTag()) {
                tagsToInsert.put(tag, node.getId());
            }
        }
        getStatement().executeBatch();

        ((TagBatchDAO) DAOManager.manager.getTag()).insertTags(tagsToInsert);
        nodesToInsert.clear();
    }

    @Override
    public void close() throws SQLException {
        insertNodes();
    }
}
