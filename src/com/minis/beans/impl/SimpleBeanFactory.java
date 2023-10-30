package com.minis.beans.impl;

import com.minis.beans.BeanDefinition;
import com.minis.beans.BeanFactory;
import com.minis.exceptions.BeansException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class SimpleBeanFactory implements BeanFactory {
    private final Map<String, BeanDefinition> beanDefinitions = new HashMap<>();
    private final Map<String, Object> singletons = new HashMap<>();

    @Override
    public Object getBean(String beanName) throws BeansException {
        if (!singletons.containsKey(beanName)) {
            if (!beanDefinitions.containsKey(beanName)) {
                throw new BeansException("BeanDefinition not exist!");
            }
            BeanDefinition beanDefinition = beanDefinitions.get(beanName);
            try {
                Object instance = Class.forName(beanDefinition.getClassName()).getDeclaredConstructor().newInstance();
                singletons.put(beanDefinition.getId(), instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return singletons.get(beanName);
    }

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        beanDefinitions.put(beanDefinition.getId(), beanDefinition);
    }
}
