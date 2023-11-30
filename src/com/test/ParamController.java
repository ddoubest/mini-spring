package com.test;

import com.minis.web.Controller;
import com.minis.web.RequestMapping;
import com.minis.web.ResponseBody;

import java.util.Date;

@Controller
public class ParamController {
    @RequestMapping("/param")
    @ResponseBody
    public Element getParam(Element element) { // /param?number=123123123123&text=hello&date=2023-11-30&rate=213.123&flag=false
        return element;
    }

    public static class Element {
        private Long number;
        private String text;
        private Date date;
        private double rate;
        private Boolean flag;

        public Boolean getFlag() {
            return flag;
        }

        public void setFlag(Boolean flag) {
            this.flag = flag;
        }

        public Long getNumber() {
            return number;
        }

        public void setNumber(Long number) {
            this.number = number;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        @Override
        public String toString() {
            return "Element{" +
                    "number=" + number +
                    ", text='" + text + '\'' +
                    ", date=" + date +
                    ", rate=" + rate +
                    ", flag=" + flag +
                    '}';
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }
    }
}
