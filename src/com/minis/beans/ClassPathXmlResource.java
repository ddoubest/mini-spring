package com.minis.beans;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.Iterator;

public class ClassPathXmlResource implements Resource {
    private final Document document;
    private final Element rootElement;
    private final Iterator<Element> elementIterator;

    public Document getDocument() {
        return document;
    }

    public Element getRootElement() {
        return rootElement;
    }

    @SuppressWarnings("unchecked")
    public ClassPathXmlResource(String fileName) {
        SAXReader reader = new SAXReader();
        try {
            URL xmlPath = ClassPathXmlResource.class.getClassLoader().getResource(fileName);
            this.document = reader.read(xmlPath);
            this.rootElement = document.getRootElement();
            this.elementIterator = rootElement.elementIterator();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasNext() {
        return elementIterator.hasNext();
    }

    @Override
    public Object next() {
        return elementIterator.next();
    }
}
