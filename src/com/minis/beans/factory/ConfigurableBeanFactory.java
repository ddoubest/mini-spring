package com.minis.beans.factory;

import com.minis.beans.factory.support.BeanPostProcessor;
import com.minis.beans.factory.support.SingletonBeanRegistry;

import java.util.List;

public interface ConfigurableBeanFactory extends BeanFactory, SingletonBeanRegistry {
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
    int getBeanPostProcessorCount();
    List<BeanPostProcessor> getBeanPostProcessors();
    void registerDependentBean(String beanName, String dependentBeanName);
    // 获取依赖的bean
    String[] getDependentBeans(String beanName);
}
