package com.test.service;

import com.minis.core.Component;

@Component
public class Action2 implements Action {

    @Override
    public void doAction() {
        System.out.println("really do action2");

    }

    @Override
    public void doSomething() {
        System.out.println("really do something action2");
    }

}
