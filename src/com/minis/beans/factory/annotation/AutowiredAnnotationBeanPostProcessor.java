package com.minis.beans.factory.annotation;

import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.support.BeanPostProcessor;
import com.minis.exceptions.BeansException;

import java.lang.reflect.Field;

public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {
    private BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                String fieldName = field.getName();
                Object autowiredBean = beanFactory.getBean(fieldName);
                try {
                    field.setAccessible(true);
                    field.set(bean, autowiredBean);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
