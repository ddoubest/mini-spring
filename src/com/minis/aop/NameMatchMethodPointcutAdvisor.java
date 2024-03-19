package com.minis.aop;

public class NameMatchMethodPointcutAdvisor implements PointCutAdvisor {
    private final NameMatchMethodPointcut pointCut = new NameMatchMethodPointcut();
    private String mappedName;
    private MethodInterceptor methodInterceptor;
    private Advice advice;

    @Override
    public PointCut getPointCut() {
        return pointCut;
    }

    @Override
    public MethodInterceptor getMethodInterceptor() {
        return this.methodInterceptor;
    }

    @Override
    public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    public String getMappedName() {
        return mappedName;
    }

    public void setMappedName(String mappedName) {
        this.mappedName = mappedName;
        this.pointCut.setMappedName(mappedName);
    }

    public Advice getAdvice() {
        return advice;
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
        MethodInterceptor mi = null;
        if (advice instanceof BeforeAdvice) {
            mi = new MethodBeforeAdviceInterceptor((MethodBeforeAdvice) advice);
        } else if (advice instanceof  AfterReturningAdvice) {
            mi = new AfterReturningAdviceInterceptor((AfterReturningAdvice) advice);
        } else if (advice instanceof MethodInterceptor) {
            mi = (MethodInterceptor) advice;
        }
        setMethodInterceptor(mi);
    }
}
