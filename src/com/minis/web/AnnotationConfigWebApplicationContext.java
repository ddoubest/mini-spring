package com.minis.web;

import com.minis.beans.factory.ConfigurableListableBeanFactory;
import com.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.support.DefaultListableBeanFactory;
import com.minis.context.AbstractApplicationContext;
import com.minis.event.ApplicationListener;
import com.minis.event.ContextRefreshEvent;
import com.minis.event.SimpleApplicationEventPublisher;
import com.minis.exceptions.BeansException;

import javax.servlet.ServletContext;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        List<String> controllerNames = scanPackages(packageNames);
        loadBeanDefinitions(controllerNames);
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

    private void loadBeanDefinitions(List<String> controllerNames) {
        for (String controllerName : controllerNames) {
            BeanDefinition beanDefinition = new BeanDefinition(controllerName, controllerName);
            beanFactory.registerBeanDefinition(controllerName, beanDefinition);
        }
    }

    private List<String> scanPackages(List<String> packageNames) {
        List<String> result = new ArrayList<>();
        for (String packageName : packageNames) {
            result.addAll(Objects.requireNonNull(scanPackage(packageName)));
        }
        return result;
    }

    private List<String> scanPackage(String packageName) {
        List<String> result = new ArrayList<>();

        String relativePath = packageName.replace(".", "/");
        URI packagePath;
        try {
            packagePath = this.getClass().getResource("/" + relativePath).toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        File dir = new File(packagePath);
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                result.addAll(scanPackage(packageName + "." + file.getName()));
            } else {
                String controllerName = packageName + "." + file.getName().replace(".class", "");
                Class<?> clazz = null;
                try {
                    clazz = Class.forName(controllerName);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                // 只要Component注解的类
                if (!clazz.isAnnotationPresent(Controller.class)) {
                    continue;
                }
                result.add(controllerName);
            }
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
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return beanFactory;
    }

    @Override
    protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
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
