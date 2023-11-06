package com.minis.beans;


import com.minis.beans.impl.SimpleBeanFactory;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class XmlBeanDefinitionReader {
    private final SimpleBeanFactory simpleBeanFactory;

    public XmlBeanDefinitionReader(SimpleBeanFactory simpleBeanFactory) {
        this.simpleBeanFactory = simpleBeanFactory;
    }

    public void loadBeanDefinitions(Resource resource) {
        while (resource.hasNext()) {
            Element element = (Element) resource.next();
            String beanName = element.attributeValue("id");
            String beanClass = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(beanName, beanClass);

            // 构造器
            List<Element> argumentElements = element.elements("constructor-arg");
            ArgumentValues AVS = new ArgumentValues();
            for (Element argumentElement : argumentElements) {
                String type = argumentElement.attributeValue("type");
                String name = argumentElement.attributeValue("name");
                String value = argumentElement.attributeValue("value");
                AVS.addArgumentValue(new ArgumentValue(type, name, value));
            }
            beanDefinition.setConstructorArgumentValues(AVS);

            // 属性
            List<Element> propertyElements = element.elements("property");
            PropertyValues PVS = new PropertyValues();
            List<String> refs = new ArrayList<>();
            for (Element propertyElement : propertyElements) {
                String type = propertyElement.attributeValue("type");
                String name = propertyElement.attributeValue("name");
                String value = propertyElement.attributeValue("value");
                String ref = propertyElement.attributeValue("ref");
                boolean isRef = false;
                if (ref != null && !ref.equals("")) {
                    value = ref;
                    isRef = true;
                    refs.add(ref);
                }
                PVS.addPropertyValue(new PropertyValue(type, name, value, isRef));
            }
            beanDefinition.setPropertyValues(PVS);

            // 依赖引用
            beanDefinition.setDependsOn(refs.toArray(new String[0]));

            simpleBeanFactory.registerBeanDefinition(beanName, beanDefinition);
        }
    }
}
