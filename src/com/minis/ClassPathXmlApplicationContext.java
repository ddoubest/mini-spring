package com.minis;

import com.minis.beans.ClassPathXmlResource;
import com.minis.beans.Resource;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.factory.support.AbstractBeanFactory;
import com.minis.beans.factory.support.AutowireCapableBeanFactory;
import com.minis.beans.factory.xml.XmlBeanDefinitionReader;
import com.minis.event.ApplicationEvent;
import com.minis.event.ApplicationEventPublisher;
import com.minis.exceptions.BeansException;

public class ClassPathXmlApplicationContext implements BeanFactory, ApplicationEventPublisher {
    private final AbstractBeanFactory beanFactory;

    public ClassPathXmlApplicationContext(String fileName) {
        this(fileName, true);
    }

    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        Resource resource = new ClassPathXmlResource(fileName);
        beanFactory = new AutowireCapableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);
        if (isRefresh) {
            refresh();
        }
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        return beanFactory.getBean(beanName);
    }

    @Override
    public Boolean containsBean(String beanName) {
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

    public void refresh() {
        registerBeanPostProcessors((AutowireCapableBeanFactory) beanFactory);
        onRefresh();
    }

    private void registerBeanPostProcessors(AutowireCapableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    private void onRefresh() {
        beanFactory.refresh();
    }
}
