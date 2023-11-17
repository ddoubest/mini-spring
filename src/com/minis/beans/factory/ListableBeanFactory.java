package com.minis.beans.factory;

import java.util.Map;

public interface ListableBeanFactory extends BeanFactory {
    boolean containsBeanDefinition(String beanName);
    int getBeanDefinitionCount();
    String[] getBeanDefinitionNames();
    String[] getBeanNamesForType(Class<?> clazz);
    <T> Map<String, T> getBeansOfTypes(Class<T> clazz);
}
