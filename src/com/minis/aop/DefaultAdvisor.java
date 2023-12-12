package com.minis.aop;

public class DefaultAdvisor implements Advisor {
    private MethodInterceptor methodInterceptor;

    @Override
    public MethodInterceptor getMethodInterceptor() {
        return methodInterceptor;
    }

    @Override
    public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }
}
