package com.minis.aop;

import java.lang.reflect.Method;

public class ReflectiveMethodInvocation implements MethodInvocation {
    private final Method method;
    private final Object[] arguments;
    private final Object target;

    public ReflectiveMethodInvocation(Method method, Object[] arguments, Object target) {
        this.method = method;
        this.arguments = arguments;
        this.target = target;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public Object getTarget() {
        return target;
    }

    @Override
    public Object proceed() throws Throwable {
        return method.invoke(target, arguments);
    }
}
