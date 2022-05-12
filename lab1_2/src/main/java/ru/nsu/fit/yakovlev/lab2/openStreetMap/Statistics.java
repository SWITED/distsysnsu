package ru.nsu.fit.yakovlev.lab2.openStreetMap;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;
import ru.nsu.fit.yakovlev.lab2.database.DAOManager;
import ru.nsu.fit.yakovlev.lab2.enums.LoggerMessages;
import ru.nsu.fit.yakovlev.lab2.generated.Tag;
import ru.nsu.fit.yakovlev.lab2.parser.JAXBParser;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;

public class Statistics {
    private final static Logger logger = LogManager.getLogger(Statistics.class);

    private XMLStreamReader reader;

    @Getter
    private List<User> users;

    @Getter
    private Map<String, Integer> tagNodes = new HashMap<>();

    public Statistics(InputStream inputStream) throws XMLStreamException, JAXBException, SAXException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
        reader = factory.createXMLStreamReader(inputStream);
        JAXBParser parser = new JAXBParser();
        Map<String, Integer> usersMap = new HashMap<>();
        logger.info(LoggerMessages.PARSING_STARTED.getMessage());
        parser.parseXML(reader, node -> {
            usersMap.put(node.getUser(), (usersMap.get(node.getUser()) == null ? 0 : usersMap.get(node.getUser()) + 1));
            List<Tag> tags = node.getTag();
            if (tags != null) {
                for (Tag tag : tags) {
                    tagNodes.put(tag.getK(), (tagNodes.get(tag.getK()) == null ? 0 : tagNodes.get(tag.getK()) + 1));
                }
            }

            try {
                DAOManager.manager.getNode().insertNode(node);
            } catch (SQLException e) {
                logger.info(e.getMessage());
            }
        });
        logger.info(LoggerMessages.PARSING_FINISHED.getMessage());
        users = new ArrayList<>();

        for (Map.Entry<String, Integer> data : usersMap.entrySet()) {
            users.add(new User(data.getKey(), data.getValue()));
        }
        users.sort(Comparator.comparingInt(User::getChangeCount));
    }

}
