package com.minis.web.servlet;

import com.minis.web.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class RequestMappingHandlerAdapter implements HandlerAdapter{
    private final WebApplicationContext webApplicationContext;

    public RequestMappingHandlerAdapter(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        handleInternal(request, response, (HandlerMethod) handler);
    }

    private void handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
        Object bean = handler.getBean();
        Method method = handler.getMethod();
        try {
            Object result = method.invoke(bean);
            response.getWriter().append(result.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
