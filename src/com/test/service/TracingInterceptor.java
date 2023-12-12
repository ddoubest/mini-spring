package com.test.service;

import com.minis.aop.MethodInterceptor;
import com.minis.aop.MethodInvocation;

import java.util.Arrays;

public class TracingInterceptor implements MethodInterceptor {
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        System.out.println("method " + methodInvocation.getMethod() + " is called on " +
                methodInvocation.getTarget() + " with args " + Arrays.toString(methodInvocation.getArguments()));
        Object ret = methodInvocation.proceed();
        System.out.println("method " + methodInvocation.getMethod() + " returns " + ret);
        return ret;
    }
}
