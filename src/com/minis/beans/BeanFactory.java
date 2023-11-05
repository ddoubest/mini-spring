package com.minis.beans;

import com.minis.exceptions.BeansException;

public interface BeanFactory {
    Object getBean(String beanName) throws BeansException;
    void registerBean(String beanName, Object obj);
    Boolean containsBean(String beanName);
    boolean isSingleton(String name);
    boolean isPrototype(String name);
    Class<?> getType(String name);
}
