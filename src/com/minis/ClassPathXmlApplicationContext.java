package com.minis;

import com.minis.resources.ClassPathXmlResource;
import com.minis.resources.Resource;
import com.minis.beans.factory.ConfigurableListableBeanFactory;
import com.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.factory.support.DefaultListableBeanFactory;
import com.minis.beans.factory.xml.XmlBeanDefinitionReader;
import com.minis.event.ApplicationListener;
import com.minis.event.ContextRefreshEvent;
import com.minis.event.SimpleApplicationEventPublisher;

public class ClassPathXmlApplicationContext extends AbstractApplicationContext {
    private final DefaultListableBeanFactory beanFactory;

    public ClassPathXmlApplicationContext(String fileName) {
        this(fileName, true);
    }

    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        Resource resource = new ClassPathXmlResource(fileName);
        beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);
        if (isRefresh) {
            refresh();
        }
    }

    @Override
    void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    @Override
    void initApplicationEventPublisher() {
        setApplicationEventPublisher(new SimpleApplicationEventPublisher());
    }

    @Override
    void onRefresh() {
        beanFactory.refresh();
    }

    @Override
    void registerListeners() {
        ApplicationListener applicationListener = new ApplicationListener();
        getApplicationEventPublisher().addApplicationListener(applicationListener);
    }

    @Override
    void finishRefresh() {
        publishEvent(new ContextRefreshEvent("Context Refreshed..."));
    }

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return beanFactory;
    }
}
