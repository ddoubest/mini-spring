package com.minis;

import com.minis.beans.BeanDefinition;
import com.minis.beans.BeanFactory;
import com.minis.beans.Resource;
import com.minis.beans.XmlBeanDefinitionReader;
import com.minis.beans.impl.ClassPathXmlResource;
import com.minis.beans.impl.SimpleBeanFactory;
import com.minis.exceptions.BeansException;

public class ClassPathXmlApplicationContext {
    private final BeanFactory beanFactory;

    public ClassPathXmlApplicationContext(String fileName) {
        Resource resource = new ClassPathXmlResource(fileName);
        beanFactory = new SimpleBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);
    }

    public Object getBean(String beanName) throws BeansException {
        return beanFactory.getBean(beanName);
    }

    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        beanFactory.registerBeanDefinition(beanDefinition);
    }
}
