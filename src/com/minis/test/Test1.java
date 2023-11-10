package com.minis.test;

import com.minis.ClassPathXmlApplicationContext;
import com.minis.exceptions.BeansException;

public class Test1 {
    public static void main(String[] args) throws BeansException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        BaseService baseService = (BaseService) ctx.getBean("baseservice");
        baseService.sayHello();
    }
}
