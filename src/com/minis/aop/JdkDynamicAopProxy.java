package com.minis.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    private final Object target;
    private final PointCutAdvisor advisor;

    public JdkDynamicAopProxy(Object target, Advisor advisor) {
        this.target = target;
        this.advisor = (PointCutAdvisor) advisor;
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(JdkDynamicAopProxy.class.getClassLoader(), target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> targetClass = (target != null ? target.getClass() : null);
        if (advisor.getPointCut().getMethodMatcher().matches(method, targetClass)) {
            MethodInterceptor methodInterceptor = this.advisor.getMethodInterceptor();
            MethodInvocation methodInvocation = new ReflectiveMethodInvocation(method, args, target);
            return methodInterceptor.invoke(methodInvocation);
        }
        return method.invoke(target, args);
    }
}
