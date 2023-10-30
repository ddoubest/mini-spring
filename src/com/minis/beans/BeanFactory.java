package com.minis.beans;

import com.minis.exceptions.BeansException;

public interface BeanFactory {
    Object getBean(String beanName) throws BeansException;
    void registerBeanDefinition(BeanDefinition beanDefinition);
}
