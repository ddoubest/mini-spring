package com.test;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.context.AnnotationClassPathXmlApplicationContext;
import com.minis.context.ApplicationContext;
import com.minis.core.Component;
import com.minis.exceptions.BeansException;
import com.test.entity.User;
import com.test.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Component
public class Test3 {
    @Autowired
    UserService userService;

    public static void main(String[] args) throws BeansException {
        List<String> scanPaths = new ArrayList<>();
        scanPaths.add("com.test");
        ApplicationContext ctx = new AnnotationClassPathXmlApplicationContext("applicationContext.xml", scanPaths);

        List<User> users1 = ((Test3)ctx.getBean("test3")).userService.getUsers(2);
        List<User> users2 = ((Test3)ctx.getBean("test3")).userService.getUsers(2);
        List<User> users3 = ((Test3)ctx.getBean("test3")).userService.getUsers(2);
        List<User> users4 = ((Test3)ctx.getBean("test3")).userService.getUsers(1);
        System.out.println(users4);
    }
}
