package com.minis.beans;


import com.minis.beans.impl.SimpleBeanFactory;
import org.dom4j.Element;

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
            simpleBeanFactory.registerBeanDefinition(beanName, beanDefinition);
        }
    }
}
