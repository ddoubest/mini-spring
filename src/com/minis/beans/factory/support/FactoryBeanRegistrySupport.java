package com.minis.beans.factory.support;

import com.minis.beans.factory.FactoryBean;
import com.minis.exceptions.BeansException;

public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry {
    protected Class<?> getTypeForFactoryBean(final FactoryBean<?> factoryBean) {
        return factoryBean.getObjectType();
    }

    protected Object getObjectFromFactoryBean(FactoryBean<?> factoryBean, String beanName) {
        Object res = doGetObjectFromFactoryBean(factoryBean, beanName);
        try {
            res = postProcessObjectFromFactoryBean(res, beanName);
        } catch (BeansException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    private Object doGetObjectFromFactoryBean(FactoryBean<?> factoryBean, String beanName) {
        Object res;
        try {
            res = factoryBean.getObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }


    protected abstract Object postProcessObjectFromFactoryBean(Object object, String beanName) throws BeansException;
}
