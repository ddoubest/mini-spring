package com.minis.context;

import com.minis.beans.factory.ConfigurableListableBeanFactory;
import com.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.factory.support.DefaultListableBeanFactory;
import com.minis.beans.factory.xml.XmlBeanDefinitionReader;
import com.minis.event.ApplicationListener;
import com.minis.event.ContextRefreshEvent;
import com.minis.event.SimpleApplicationEventPublisher;
import com.minis.resources.ClassPathXmlResource;
import com.minis.resources.Resource;

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
    protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        AutowiredAnnotationBeanPostProcessor beanPostProcessor = new AutowiredAnnotationBeanPostProcessor();
        beanPostProcessor.setBeanFactory(this);
        beanFactory.addBeanPostProcessor(beanPostProcessor);
    }

    @Override
    protected void initApplicationEventPublisher() {
        setApplicationEventPublisher(new SimpleApplicationEventPublisher());
    }

    @Override
    protected void onRefresh() {
        beanFactory.refresh();
    }

    @Override
    protected void registerListeners() {
        ApplicationListener applicationListener = new ApplicationListener();
        getApplicationEventPublisher().addApplicationListener(applicationListener);
    }

    @Override
    protected void finishRefresh() {
        publishEvent(new ContextRefreshEvent("Context Refreshed..."));
    }

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return beanFactory;
    }
}
