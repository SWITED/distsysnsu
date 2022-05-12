package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class BZipParser implements AutoCloseable{
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private InputStreamReader isReader;
    public void parse(String filename) {
        InputStream is = Main.class.getClassLoader().getResourceAsStream(filename);

        try {
            isReader = BZip2Reader.getBZip2Reader(is);
            StAXProcessor processor = new StAXProcessor(isReader);
            LOGGER.info("StAX processor created successfully");
            XMLEventReader reader = processor.getReader();

            processXML(reader);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    private void processXML(XMLEventReader reader) {
        Map<String, Integer> editsMap = new HashMap<>();
        Map<String, Integer> uniqueTags = new HashMap<>();
        LOGGER.info("Starting file processing");
        try {
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    if (startElement.getName().getLocalPart().equals("node")) {
                        processEditors(startElement, editsMap);
                    }
                    if (startElement.getName().getLocalPart().equals("tag")) {
                        processTags(startElement, uniqueTags);
                    }
                }
            }
            LOGGER.info("Editors:");
            printMap(sortByValues(editsMap));
            LOGGER.info("Tags:");
            printMap(sortByValues(uniqueTags));
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private void processTags(StartElement startElement, Map<String, Integer> uniqueTags) {
        Iterator<Attribute> tags = startElement.getAttributes();
        var key = startElement.getAttributeByName(new QName("k"));
        var value = startElement.getAttributeByName(new QName("v"));
        if (key != null && key.getValue().equals("name") && value != null) {
            uniqueTags.merge(value.getValue(), 1, Integer::sum);
        }
    }

    private void processEditors(StartElement startElement, Map<String, Integer> editors) {
        Attribute user = startElement.getAttributeByName(new QName("user"));
        if (user != null) {
            editors.merge(user.getValue(), 1, Integer::sum);
        }
    }

    private HashMap<String, Integer> sortByValues(Map<String, Integer> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x1, x2) -> x1, LinkedHashMap::new));
    }

    private void printMap(Map <String, Integer> map) {
        for (String key : map.keySet()) {
            LOGGER.info(key + " - " + map.get(key));
        }
    }

    @Override
    public void close() throws Exception {
        if (isReader != null) {
            isReader.close();
        }
    }
}
