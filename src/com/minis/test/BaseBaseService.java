package com.minis.test;

public class BaseBaseService implements AService {
    private AService as;

    public AService getAs() {
        return as;
    }

    public void setAs(AService as) {
        this.as = as;
    }

    public void sayHello() {
        System.out.println("BaseBaseService says Hello");
    }
}
