package com.minis.beans.impl;

import com.minis.beans.BeanDefinition;
import com.minis.beans.BeanDefinitionRegistry;
import com.minis.beans.BeanFactory;
import com.minis.exceptions.BeansException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, BeanDefinitionRegistry {
    private final Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>();

    @Override
    public Object getBean(String beanName) throws BeansException {
        Object bean = getSingleton(beanName);
        if (bean == null) {
            if (!beanDefinitions.containsKey(beanName)) {
                throw new BeansException("BeanDefinition not exist!");
            }
            BeanDefinition beanDefinition = beanDefinitions.get(beanName);
            synchronized (beanDefinition) {
                bean = getSingleton(beanName);
                if (bean == null) {
                    try {
                        bean = Class.forName(beanDefinition.getClassName()).getDeclaredConstructor().newInstance();
                        registerSingleton(beanDefinition.getId(), bean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return bean;
    }

    @Override
    public void registerBean(String beanName, Object obj) {
        registerSingleton(beanName, obj);
    }

    @Override
    public Boolean containsBean(String beanName) {
        return contatinsSingleton(beanName) || beanDefinitions.containsKey(beanName);
    }

    @Override
    public boolean isSingleton(String name) {
        return beanDefinitions.get(name).isSingleton();
    }

    @Override
    public boolean isPrototype(String name) {
        return beanDefinitions.get(name).isPrototype();
    }

    // TODO：争议
    @Override
    public Class<?> getType(String name) {
        return (Class<?>) beanDefinitions.get(name).getBeanClass();
    }

    @Override
    public void registerBeanDefinition(String name, BeanDefinition bd) {
        beanDefinitions.put(name, bd);
        if (!bd.isLazyInit()) {
            try {
                getBean(name);
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void removeBeanDefinition(String name) {
        beanDefinitions.remove(name);
        removeSingleton(name);
    }

    @Override
    public BeanDefinition getBeanDefinition(String name) {
        return beanDefinitions.get(name);
    }

    @Override
    public boolean containsBeanDefinition(String name) {
        return beanDefinitions.containsKey(name);
    }
}
