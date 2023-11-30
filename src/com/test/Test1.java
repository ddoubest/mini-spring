package com.test;

import com.minis.exceptions.BeansException;
import com.minis.web.DefaultObjectMapper;

import java.util.Date;

public class Test1 {
    public static void main(String[] args) throws BeansException {
        // ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        // BaseService baseService = (BaseService) ctx.getBean("baseservice");
        // baseService.sayHello();

        DefaultObjectMapper defaultObjectMapper = new DefaultObjectMapper();
        ParamController.Element[] elements = new ParamController.Element[3];
        for (int i = 0; i < 3; i ++) {
            elements[i] = new ParamController.Element();
            if (i != 1) {
                elements[i].setDate(new Date());
            }
            elements[i].setNumber((long) i);
            elements[i].setText("element_" + i);
            if (i != 2) {
                elements[i].setRate(123123.123);
            }
            if (i != 2) {
                elements[i].setFlag(true);
            }
        }
        System.out.println(defaultObjectMapper.writeValuesAsString(elements));
    }
}
