package com.minis.beans.factory.support;

import com.minis.beans.factory.ConfigurableListableBeanFactory;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.exceptions.BeansException;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements ConfigurableListableBeanFactory {

    @Override
    public void registerDependentBean(String beanName, String dependentBeanName) {
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        List<String> currentDependentBeans = Arrays.stream(beanDefinition.getDependsOn()).collect(Collectors.toList());
        currentDependentBeans.add(dependentBeanName);
        beanDefinition.setDependsOn(currentDependentBeans.toArray(new String[0]));
    }

    @Override
    public String[] getDependentBeans(String beanName) {
        return getBeanDefinition(beanName).getDependsOn();
    }

    @Override
    public int getBeanDefinitionCount() {
        return beanDefinitions.size();
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitions.keySet().toArray(new String[0]);
    }

    @Override
    public String[] getBeanNamesForType(Class<?> clazz) {
        List<String> beanNames = new ArrayList<>();
        beanDefinitions.forEach((name, beanDefinition) -> {
            if (clazz.isAssignableFrom(beanDefinition.getBeanClass())) {
                beanNames.add(name);
            }
        });
        return beanNames.toArray(new String[0]);
    }

    @Override
    public <T> Map<String, T> getBeansOfTypes(Class<T> clazz) {
        Map<String, T> beans = new HashMap<>();
        beanDefinitions.forEach((name, beanDefinition) -> {
            if (clazz.isAssignableFrom(beanDefinition.getBeanClass())) {
                try {
                    Object bean = getBean(name);
                    beans.put(name, clazz.cast(bean));
                } catch (BeansException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return beans;
    }
}
