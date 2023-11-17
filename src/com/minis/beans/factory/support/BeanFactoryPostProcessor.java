package com.minis.beans.factory.support;

import com.minis.beans.factory.ConfigurableListableBeanFactory;
import com.minis.exceptions.BeansException;

public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
}
