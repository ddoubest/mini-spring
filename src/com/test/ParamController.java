package com.test;

import com.minis.web.Controller;
import com.minis.web.RequestMapping;

import java.util.Date;

@Controller
public class ParamController {
    @RequestMapping("/param")
    public String getParam(Element element) { // /param?number=123123123123&text=hello&date=2023-11-30
        return "Success:\n" + element.toString();
    }

    private static class Element {
        private Long number;
        private String text;
        private Date date;

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
                    '}';
        }
    }
}
