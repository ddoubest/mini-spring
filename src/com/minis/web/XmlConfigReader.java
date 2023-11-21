package com.minis.web;

import com.minis.resources.Resource;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Map;

public class XmlConfigReader {
    public Map<String, MappingValue> loadConfig(Resource resource) {
        Map<String, MappingValue> res = new HashMap<>();
        while (resource.hasNext()) {
            Element definition = (Element) resource.next();
            String uri = definition.attributeValue("id");
            String clazz = definition.attributeValue("class");
            String method = definition.attributeValue("value");
            MappingValue mappingValue = new MappingValue(uri, clazz, method);
            res.put(uri, mappingValue);
        }
        return res;
    }
}
