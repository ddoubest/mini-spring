package com.minis;

import com.minis.beans.BeanFactory;
import com.minis.beans.Resource;
import com.minis.beans.XmlBeanDefinitionReader;
import com.minis.beans.impl.ClassPathXmlResource;
import com.minis.beans.impl.SimpleBeanFactory;
import com.minis.event.ApplicationEvent;
import com.minis.event.ApplicationEventPublisher;
import com.minis.exceptions.BeansException;

public class ClassPathXmlApplicationContext implements BeanFactory, ApplicationEventPublisher {
    private final BeanFactory beanFactory;

    public ClassPathXmlApplicationContext(String fileName) {
        Resource resource = new ClassPathXmlResource(fileName);
        beanFactory = new SimpleBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader((SimpleBeanFactory) beanFactory);
        reader.loadBeanDefinitions(resource);
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        return beanFactory.getBean(beanName);
    }

    @Override
    public Boolean containsBean(String  beanName) {
        return beanFactory.containsBean(beanName);
    }

    @Override
    public void registerBean(String beanName, Object obj) {
        beanFactory.registerBean(beanName, obj);
    }

    @Override
    public boolean isSingleton(String name) {
        return beanFactory.isSingleton(name);
    }

    @Override
    public boolean isPrototype(String name) {
        return beanFactory.isPrototype(name);
    }

    @Override
    public Class<?> getType(String name) {
        return beanFactory.getType(name);
    }

    @Override
    public void publishApplicationEvent(ApplicationEvent applicationEvent) {

    }
}
