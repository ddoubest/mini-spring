package com.minis.aop;

import com.minis.utils.PatternMatchUtils;

import java.lang.reflect.Method;

public class NameMatchMethodPointcut implements MethodMatcher, PointCut {
    private String mappedName = "";

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        if (mappedName.equals(method.getName()) || isMatch(method.getName(), mappedName)) {
            return true;
        }
        return false;
    }

    private boolean isMatch(String methodName, String mappedName) {
        return PatternMatchUtils.simpleMatch(mappedName, methodName);
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }

    public void setMappedName(String mappedName) {
        this.mappedName = mappedName;
    }
}
