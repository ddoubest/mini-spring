package com.minis.aop;

import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.BeanFactoryAware;
import com.minis.beans.factory.FactoryBean;
import com.minis.exceptions.BeansException;
import com.minis.utils.ClassUtils;

public class ProxyFactoryBean implements FactoryBean<Object>, BeanFactoryAware {
    private AopProxyFactory aopProxyFactory;
    private String[] interceptorNames;
    private String targetName;
    private Object target;
    private final ClassLoader proxyClassLoader = ClassUtils.getDefaultClassLoader();
    private Object singletonInstance;

    private BeanFactory beanFactory;
    private String interceptorName;
    private volatile Advisor advisor;

    public ProxyFactoryBean() {
        this.aopProxyFactory = new DefaultAopProxyFactory();
    }

    private void initializeAdvisor() {
        try {
            MethodInterceptor methodInterceptor = (MethodInterceptor) this.beanFactory.getBean(interceptorName);
            this.advisor = new DefaultAdvisor();
            this.advisor.setMethodInterceptor(methodInterceptor);
        } catch (BeansException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getObject() throws Exception {//获取内部对象
        if (this.advisor == null) {
            synchronized (this) {
                if (this.advisor == null) {
                    initializeAdvisor();
                }
            }
        }
        return getSingletonInstance();
    }

    private synchronized Object getSingletonInstance() {//获取代理
        if (this.singletonInstance == null) {
            this.singletonInstance = getProxy(createAopProxy());
        }
        return this.singletonInstance;
    }

    protected Object getProxy(AopProxy aopProxy) {//生成代理对象
        return aopProxy.getProxy();
    }

    protected AopProxy createAopProxy() {
        return getAopProxyFactory().createAopProxy(target, advisor);
    }

    @Override
    public Class<Object> getObjectType() {
        return Object.class;
    }


    public String getInterceptorName() {
        return interceptorName;
    }

    public void setInterceptorName(String interceptorName) {
        this.interceptorName = interceptorName;
    }

    public Advisor getAdvisor() {
        return advisor;
    }

    public void setAdvisor(Advisor advisor) {
        this.advisor = advisor;
    }

    @Override
    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public AopProxyFactory getAopProxyFactory() {
        return this.aopProxyFactory;
    }

    public void setAopProxyFactory(AopProxyFactory aopProxyFactory) {
        this.aopProxyFactory = aopProxyFactory;
    }

    public void setInterceptorNames(String... interceptorNames) {
        this.interceptorNames = interceptorNames;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}