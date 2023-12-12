package com.minis.context;

import com.minis.beans.factory.ConfigurableListableBeanFactory;
import com.minis.beans.factory.FactoryAwareBeanPostProcessor;
import com.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.factory.support.DefaultListableBeanFactory;
import com.minis.beans.factory.xml.XmlBeanDefinitionReader;
import com.minis.event.ApplicationListener;
import com.minis.event.ContextRefreshEvent;
import com.minis.event.SimpleApplicationEventPublisher;
import com.minis.resources.ClassPathXmlResource;
import com.minis.resources.Resource;
import com.minis.utils.ScanComponentHelper;

import java.util.List;

public class AnnotationClassPathXmlApplicationContext extends AbstractApplicationContext {
    private final DefaultListableBeanFactory beanFactory;

    public AnnotationClassPathXmlApplicationContext(String fileName, List<String> packageNames) {
        this(fileName, packageNames, true);
    }

    public AnnotationClassPathXmlApplicationContext(String fileName, List<String> packageNames, boolean isRefresh) {
        Resource resource = new ClassPathXmlResource(fileName);
        beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);
        List<String> controllerNames = ScanComponentHelper.scanPackages(packageNames);
        ScanComponentHelper.loadBeanDefinitions(beanFactory, controllerNames);
        if (isRefresh) {
            refresh();
        }
    }

    @Override
    protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor(this));
        beanFactory.addBeanPostProcessor(new FactoryAwareBeanPostProcessor(this));
    }

    @Override
    protected void initApplicationEventPublisher() {
        setApplicationEventPublisher(new SimpleApplicationEventPublisher());
    }

    @Override
    protected void onRefresh() {
        beanFactory.refresh();
    }

    @Override
    protected void registerListeners() {
        ApplicationListener applicationListener = new ApplicationListener();
        getApplicationEventPublisher().addApplicationListener(applicationListener);
    }

    @Override
    protected void finishRefresh() {
        publishEvent(new ContextRefreshEvent("Context Refreshed..."));
    }

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return beanFactory;
    }
}
