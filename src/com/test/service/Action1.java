package com.test.service;

import com.minis.core.Component;

@Component
public class Action1 implements Action {

    @Override
    public void doAction() {
        System.out.println("really do action1");
    }

    @Override
    public void doSomething() {
        System.out.println("really do something action1");
    }

}
