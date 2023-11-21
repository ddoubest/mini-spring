package com.minis.test;

import com.minis.web.Controller;
import com.minis.web.RequestMapping;

@Controller
public class HelloWorldBean {
    @RequestMapping("/test")
    public String doTest() {
        return "hello world for doGet!";
    }
}