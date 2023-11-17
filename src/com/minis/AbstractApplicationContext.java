package com.minis;

import com.minis.beans.factory.ConfigurableListableBeanFactory;
import com.minis.beans.factory.support.BeanFactoryPostProcessor;
import com.minis.beans.factory.support.BeanPostProcessor;
import com.minis.env.Environment;
import com.minis.event.ApplicationEvent;
import com.minis.event.ApplicationEventPublisher;
import com.minis.event.ApplicationListener;
import com.minis.exceptions.BeansException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractApplicationContext implements ApplicationContext {
    private Environment environment;
    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();
    private long startupDate;
    private final AtomicBoolean active = new AtomicBoolean();
    private ApplicationEventPublisher applicationEventPublisher;

    public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
        return beanFactoryPostProcessors;
    }

    public void refresh() {
        postProcessBeanFactory(getBeanFactory());
        registerBeanPostProcessors(getBeanFactory());
        initApplicationEventPublisher();
        onRefresh();
        registerListeners();
        finishRefresh();
    }

    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        beanFactoryPostProcessors.forEach(beanFactoryPostProcessor -> {
            try {
                beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
        });
    };

    abstract void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory);

    abstract void initApplicationEventPublisher();

    abstract void onRefresh();

    abstract void registerListeners();

    abstract void finishRefresh();

    @Override
    public String getApplicationName() {
        return "";
    }

    @Override
    public long getStartupDate() {
        return startupDate;
    }

    @Override
    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
        beanFactoryPostProcessors.add(postProcessor);
    }

    @Override
    public boolean isActive() {
        return active.get();
    }

    @Override
    public void close() {
        active.set(false);
    }

    public ApplicationEventPublisher getApplicationEventPublisher() {
        return applicationEventPublisher;
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Environment getEnvironment() {
        return environment;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        getApplicationEventPublisher().publishEvent(event);
    }

    @Override
    public void addApplicationListener(ApplicationListener listener) {
        getApplicationEventPublisher().addApplicationListener(listener);
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        return getBeanFactory().getBean(beanName);
    }

    @Override
    public Boolean containsBean(String beanName) {
        return getBeanFactory().containsBean(beanName);
    }

    @Override
    public void registerBean(String beanName, Object obj) {
        getBeanFactory().registerBean(beanName, obj);
    }

    @Override
    public boolean isSingleton(String name) {
        return getBeanFactory().isSingleton(name);
    }

    @Override
    public boolean isPrototype(String name) {
        return getBeanFactory().isPrototype(name);
    }

    @Override
    public Class<?> getType(String name) {
        return getBeanFactory().getType(name);
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        getBeanFactory().addBeanPostProcessor(beanPostProcessor);
    }

    @Override
    public int getBeanPostProcessorCount() {
        return getBeanFactory().getBeanPostProcessorCount();
    }

    @Override
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return getBeanFactory().getBeanPostProcessors();
    }

    @Override
    public void registerDependentBean(String beanName, String dependentBeanName) {
        getBeanFactory().registerDependentBean(beanName, dependentBeanName);
    }

    @Override
    public String[] getDependentBeans(String beanName) {
        return getBeanFactory().getDependentBeans(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return getBeanFactory().containsBeanDefinition(beanName);
    }

    @Override
    public int getBeanDefinitionCount() {
        return getBeanFactory().getBeanDefinitionCount();
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    @Override
    public String[] getBeanNamesForType(Class<?> clazz) {
        return getBeanFactory().getBeanNamesForType(clazz);
    }

    @Override
    public <T> Map<String, T> getBeansOfTypes(Class<T> clazz) {
        return getBeanFactory().getBeansOfTypes(clazz);
    }

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        getBeanFactory().registerSingleton(beanName, singletonObject);
    }

    @Override
    public Object getSingleton(String beanName) {
        return getBeanFactory().getSingleton(beanName);
    }

    @Override
    public Boolean contatinsSingleton(String beanName) {
        return getBeanFactory().contatinsSingleton(beanName);
    }

    @Override
    public String[] getSingletonNames() {
        return getBeanFactory().getSingletonNames();
    }
}
