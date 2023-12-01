package com.test;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.context.AnnotationClassPathXmlApplicationContext;
import com.minis.context.ApplicationContext;
import com.minis.core.Component;
import com.minis.exceptions.BeansException;
import com.test.entity.User;
import com.test.service.UserService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class Test2 {
    @Autowired
    UserService userService;

    public static void main(String[] args) throws BeansException {
        List<String> scanPaths = new ArrayList<>();
        scanPaths.add("com.test");
        ApplicationContext ctx = new AnnotationClassPathXmlApplicationContext("applicationContext.xml", scanPaths);

        User user = ((Test2)ctx.getBean("test2")).userService.getUserInfoPrepared(2);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(user.getBirthday());
        System.out.println(simpleDateFormat.format(user.getBirthday()));
    }
}
