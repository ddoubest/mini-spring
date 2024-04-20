package com.minis.web;

import com.minis.beans.factory.ConfigurableListableBeanFactory;
import com.minis.beans.factory.FactoryAwareBeanPostProcessor;
import com.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.factory.support.BeanPostProcessor;
import com.minis.beans.factory.support.DefaultListableBeanFactory;
import com.minis.context.AbstractApplicationContext;
import com.minis.event.ApplicationListener;
import com.minis.event.ContextRefreshEvent;
import com.minis.event.SimpleApplicationEventPublisher;
import com.minis.exceptions.BeansException;
import com.minis.utils.ScanComponentHelper;

import javax.servlet.ServletContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class AnnotationConfigWebApplicationContext extends AbstractApplicationContext implements WebApplicationContext {
    private final DefaultListableBeanFactory beanFactory;
    private ServletContext servletContext;
    private WebApplicationContext parentWebApplicationContext;

    public AnnotationConfigWebApplicationContext(String fileName) {
        this(fileName, null);
    }

    public AnnotationConfigWebApplicationContext(String fileName, WebApplicationContext parentWebApplicationContext) {
        this.parentWebApplicationContext = parentWebApplicationContext;
        this.servletContext = parentWebApplicationContext.getServletContext();
        this.beanFactory = new DefaultListableBeanFactory();
        this.beanFactory.setParentBeanFactory(this.parentWebApplicationContext.getBeanFactory());

        URL xmlPath;
        try {
            xmlPath = getServletContext().getResource(fileName);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        List<String> packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);
        List<String> controllerNames = ScanComponentHelper.scanPackages(packageNames);
        ScanComponentHelper.loadBeanDefinitions(beanFactory, controllerNames);
        refresh();
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        Object result = super.getBean(beanName);
        if (result == null) {
            result = parentWebApplicationContext.getBean(beanName);
        }
        return result;
    }

    public WebApplicationContext getParentWebApplicationContext() {
        return parentWebApplicationContext;
    }

    public void setParentWebApplicationContext(WebApplicationContext parentWebApplicationContext) {
        this.parentWebApplicationContext = parentWebApplicationContext;
        this.beanFactory.setParentBeanFactory(this.parentWebApplicationContext.getBeanFactory());
    }

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public WebApplicationContext getParent() {
        return parentWebApplicationContext;
    }

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return beanFactory;
    }

    @Override
    protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor(this));
        beanFactory.addBeanPostProcessor(new FactoryAwareBeanPostProcessor(this));
        try {
            beanFactory.addBeanPostProcessor((BeanPostProcessor) this.getBean("autoProxyCreator"));
        } catch (BeansException e) {
            e.printStackTrace();
        }
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
        getApplicationEventPublisher().addApplicationListener(new ApplicationListener());
    }

    @Override
    protected void finishRefresh() {
        publishEvent(new ContextRefreshEvent("Web Application Context Refreshed..."));
    }
}
