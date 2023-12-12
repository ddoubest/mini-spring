package com.minis.beans.factory;

public interface BeanFactoryAware {
    BeanFactory getBeanFactory();
    void setBeanFactory(BeanFactory beanFactory);
}
