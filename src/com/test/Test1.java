package com.test;

import com.minis.context.ClassPathXmlApplicationContext;
import com.minis.exceptions.BeansException;
import com.test.service.BaseService;

public class Test1 {
    public static void main(String[] args) throws BeansException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        BaseService baseService = (BaseService) ctx.getBean("baseservice");
        baseService.sayHello();
    }
}
