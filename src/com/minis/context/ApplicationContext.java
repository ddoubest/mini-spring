package com.minis.context;

import com.minis.beans.factory.ConfigurableBeanFactory;
import com.minis.beans.factory.ConfigurableListableBeanFactory;
import com.minis.beans.factory.ListableBeanFactory;
import com.minis.beans.factory.support.BeanFactoryPostProcessor;
import com.minis.env.Environment;
import com.minis.env.EnvironmentCapable;
import com.minis.event.ApplicationEventPublisher;
import com.minis.exceptions.BeansException;

public interface ApplicationContext
        extends EnvironmentCapable, ListableBeanFactory,
        ConfigurableBeanFactory, ApplicationEventPublisher{
    String getApplicationName();
    long getStartupDate();
    ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;
    void setEnvironment(Environment environment);
    Environment getEnvironment();
    void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor);
    void refresh() throws BeansException, IllegalStateException;
    void close();
    boolean isActive();
}
