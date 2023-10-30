package com.minis.test;

import com.minis.ClassPathXmlApplicationContext;
import com.minis.exceptions.BeansException;

import java.util.ArrayList;
import java.util.List;

public class Test1 {
    public static void main(String[] args) throws BeansException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        AService aservice = (AService) ctx.getBean("aservice");
        aservice.sayHello();
    }
}
