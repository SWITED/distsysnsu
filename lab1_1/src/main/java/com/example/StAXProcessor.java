package com.example;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.InputStreamReader;

public class StAXProcessor implements AutoCloseable {

    private static final XMLInputFactory FACTORY = XMLInputFactory.newInstance();
    private final XMLEventReader reader;

    public StAXProcessor(InputStreamReader isReader) throws XMLStreamException {
        reader = FACTORY.createXMLEventReader(isReader);
    }

    public XMLEventReader getReader() {
        return reader;
    }

    @Override
    public void close() throws Exception {
        if (reader != null) {
            reader.close();
        }
    }
}
