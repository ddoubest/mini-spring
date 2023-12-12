package com.minis.aop;

public class AfterReturningAdviceInterceptor implements MethodInterceptor, AfterAdvice {
    private AfterReturningAdvice advice;

    public AfterReturningAdviceInterceptor() {

    }

    public AfterReturningAdviceInterceptor(AfterReturningAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        Object retVal = mi.proceed();
        this.advice.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getTarget());
        return retVal;
    }

    public AfterReturningAdvice getAdvice() {
        return advice;
    }

    public void setAdvice(AfterReturningAdvice advice) {
        this.advice = advice;
    }
}
