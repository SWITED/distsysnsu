package ru.nsu.fit.yakovlev.lab2.database;

import ru.nsu.fit.yakovlev.lab2.generated.Tag;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TagBatchDAO extends TagPreparedDAO {
    private static final int BATCH_SIZE = 1000;
    private final Map<Tag, BigInteger> tagsToInsert = new HashMap<>();

    public TagBatchDAO(DBConnection connection) throws SQLException {
        super(connection);
    }

    @Override
    public void insertTag(Tag tag, BigInteger nodeId) throws SQLException {
        tagsToInsert.put(tag, nodeId);
        if (tagsToInsert.size() >= BATCH_SIZE) {
            insertTags(tagsToInsert);
            tagsToInsert.clear();
        }
    }

    public void insertTags(Map<Tag, BigInteger> tagsToInsert) throws SQLException {
        for (Map.Entry<Tag, BigInteger> tag : tagsToInsert.entrySet()) {
            prepareStatement(tag.getKey(), tag.getValue());
            getStatement().addBatch();
        }

        getStatement().executeBatch();
    }

    @Override
    public void close() throws SQLException {
        insertTags(tagsToInsert);
        tagsToInsert.clear();
    }
}
