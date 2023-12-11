package com.test;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.web.Controller;
import com.minis.web.RequestMapping;
import com.minis.web.ResponseBody;
import com.test.service.Action;

@Controller
public class HelloWorldBean {
    @Autowired
    private Action action;

    @RequestMapping("/test")
    @ResponseBody
    public String doTest() {
        return "hello world for doGet!";
    }

    @RequestMapping("/testaop")
    @ResponseBody
    public String doTestAop() {
        action.doAction();
        return "test aop, hello world!";
    }

}