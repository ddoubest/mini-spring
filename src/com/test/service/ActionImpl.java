package com.test.service;

public class ActionImpl implements Action {
    @Override
    public void doAction() {
        System.out.println("really do action");
    }

    @Override
    public void doSomething() {
        System.out.println("really do something");
    }
}