package com.minis.beans.factory.support;

import com.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.minis.exceptions.BeansException;

import java.util.ArrayList;
import java.util.List;

public class AutowireCapableBeanFactory extends AbstractBeanFactory {
    private final List<AutowiredAnnotationBeanPostProcessor> beanPostProcessors = new ArrayList<>();

    public void addBeanPostProcessor(AutowiredAnnotationBeanPostProcessor beanPostProcessor) {
        beanPostProcessors.remove(beanPostProcessor); // 保证不重复添加
        beanPostProcessors.add(beanPostProcessor);
    }

    public int getBeanPostProcessorCount() {
        return beanPostProcessors.size();
    }

    public List<AutowiredAnnotationBeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }

    @Override
    public Object applyBeanPostProcessorBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (AutowiredAnnotationBeanPostProcessor beanPostProcessor : beanPostProcessors) {
            beanPostProcessor.setBeanFactory(this);
            Object processBean = beanPostProcessor.postProcessBeforeInitialization(existingBean, beanName);
            if (processBean != null) {
                result = processBean;
            }
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (AutowiredAnnotationBeanPostProcessor beanPostProcessor : beanPostProcessors) {
            beanPostProcessor.setBeanFactory(this);
            Object processBean = beanPostProcessor.postProcessAfterInitialization(existingBean, beanName);
            if (processBean != null) {
                result = processBean;
            }
        }
        return result;
    }
}
