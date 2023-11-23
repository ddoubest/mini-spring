package com.minis.web.servlet;

import java.lang.reflect.Method;

public class HandlerMethod {
    private Object bean;
    private Class<?> beanType;
    private String className;
    private Method method;
    private Object[] methodParamters;
    private String methodName;

    private Class<?> returnType;

    private String description;

    public HandlerMethod(Method method, Object obj) {
        setMethod(method);
        setBean(obj);
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
