package com.minis.beans;

public interface SingletonBeanRegistry {
    void registerSingleton(String beanName, Object singletonObject);
    Object getSingleton(String beanName);
    Boolean contatinsSingleton(String beanName);
    String[] getSingletonNames();
}
