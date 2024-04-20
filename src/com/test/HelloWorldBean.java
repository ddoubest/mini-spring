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
    public void doTestAopp() {
        action.doAction();
    }

    @RequestMapping("/testaop")
    @ResponseBody
    public String doTestAop() {
        action.doAction();
        return "test aop, hello world!";
    }

    @RequestMapping("/testaop2")
    public void doTestAop2() {
        action.doSomething();
    }


    @Autowired
    private Action action1;

    @RequestMapping("/testaop3")
    public void doTestAop3() {
        action1.doAction();
        action1.doSomething();
    }

    @Autowired
    private Action action2;

    @RequestMapping("/testaop4")
    public void doTestAop4() {
        action2.doAction();
        action2.doSomething();
    }
}