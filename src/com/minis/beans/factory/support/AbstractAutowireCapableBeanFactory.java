package com.minis.beans.factory.support;

import com.minis.beans.factory.AutowireCapableBeanFactory;
import com.minis.exceptions.BeansException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        beanPostProcessors.remove(beanPostProcessor); // 保证不重复添加
        beanPostProcessors.add(beanPostProcessor);
    }

    @Override
    public int getBeanPostProcessorCount() {
        return beanPostProcessors.size();
    }

    @Override
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }

    @Override
    public Object applyBeanPostProcessorBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            if (beanPostProcessor.getBeanFactory() == null) {
                beanPostProcessor.setBeanFactory(this);
            }
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
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            if (beanPostProcessor.getBeanFactory() == null) {
                beanPostProcessor.setBeanFactory(this);
            }
            Object processBean = beanPostProcessor.postProcessAfterInitialization(existingBean, beanName);
            if (processBean != null) {
                result = processBean;
            }
        }
        return result;
    }
}
