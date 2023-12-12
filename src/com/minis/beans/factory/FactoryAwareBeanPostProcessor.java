package com.minis.beans.factory;

import com.minis.beans.factory.support.BeanPostProcessor;
import com.minis.exceptions.BeansException;

public class FactoryAwareBeanPostProcessor implements BeanPostProcessor {
    private BeanFactory beanFactory;

    public FactoryAwareBeanPostProcessor(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof BeanFactoryAware) {
            BeanFactoryAware beanFactoryAware = (BeanFactoryAware) bean;
            beanFactoryAware.setBeanFactory(beanFactory);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public BeanFactory getBeanFactory() {
        return beanFactory;
    }
}
