package com.minis.aop.autoproxy;

import com.minis.aop.*;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.support.BeanPostProcessor;
import com.minis.exceptions.BeansException;
import com.minis.utils.PatternMatchUtils;

public class BeanNameAutoProxyCreator implements BeanPostProcessor {
    private String pattern;
    private BeanFactory beanFactory;
    private AopProxyFactory aopProxyFactory;
    private String interceptorName;
    private PointCutAdvisor advisor;

    public BeanNameAutoProxyCreator() {
        this.aopProxyFactory = new DefaultAopProxyFactory();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (isMatch(beanName, pattern)) {
            ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();

            proxyFactoryBean.setTarget(bean);
            proxyFactoryBean.setBeanFactory(beanFactory);
            proxyFactoryBean.setAopProxyFactory(aopProxyFactory);
            proxyFactoryBean.setInterceptorName(interceptorName);
            proxyFactoryBean.setAdvisor(advisor);

            return proxyFactoryBean;
        }
        return bean;
    }

    private boolean isMatch(String beanName, String mappedName) {
        return PatternMatchUtils.simpleMatch(mappedName, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public AopProxyFactory getAopProxyFactory() {
        return aopProxyFactory;
    }

    public void setAopProxyFactory(AopProxyFactory aopProxyFactory) {
        this.aopProxyFactory = aopProxyFactory;
    }

    public String getInterceptorName() {
        return interceptorName;
    }

    public void setInterceptorName(String interceptorName) {
        this.interceptorName = interceptorName;
    }

    public PointCutAdvisor getAdvisor() {
        return advisor;
    }

    public void setAdvisor(PointCutAdvisor advisor) {
        this.advisor = advisor;
    }
}
