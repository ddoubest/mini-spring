package com.minis.beans;

import com.minis.beans.impl.ClassPathXmlResource;
import org.dom4j.Element;

public class XmlBeanDefinitionReader {
    private final BeanFactory beanFactory;

    public XmlBeanDefinitionReader(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void loadBeanDefinitions(Resource resource) {
        while (resource.hasNext()) {
            Element element = (Element) resource.next();
            String beanName = element.attributeValue("id");
            String beanClass = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(beanName, beanClass);
            beanFactory.registerBeanDefinition(beanDefinition);
        }
    }
}
