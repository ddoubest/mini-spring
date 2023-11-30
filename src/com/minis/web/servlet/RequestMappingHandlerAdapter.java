package com.minis.web.servlet;

import com.minis.exceptions.BeansException;
import com.minis.web.WebApplicationContext;
import com.minis.web.WebBindingInitializer;
import com.minis.web.WebDataBinder;
import com.minis.web.WebDataBinderFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;

public class RequestMappingHandlerAdapter implements HandlerAdapter{
    private final WebApplicationContext webApplicationContext;
    private final WebBindingInitializer webBindingInitializer;

    public RequestMappingHandlerAdapter(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
        try {
            this.webBindingInitializer =
                    (WebBindingInitializer) webApplicationContext.getBean("webBindingInitializer");
        } catch (BeansException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        handleInternal(request, response, (HandlerMethod) handler);
    }

    private void handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
        try {
            invokeHandlerMethod(request, response, handler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        WebDataBinderFactory webDataBinderFactory = new WebDataBinderFactory();
        Parameter[] methodParameters = handler.getMethod().getParameters();
        Object[] methodParamObjs = new Object[methodParameters.length];
        int idx = 0;
        for (Parameter methodParameter : methodParameters) {
            Constructor<?> methodParamConstructor = methodParameter.getType().getDeclaredConstructor();
            methodParamConstructor.setAccessible(true);
            Object methodParamObj = methodParamConstructor.newInstance();
            WebDataBinder binder = webDataBinderFactory.createBinder(request, methodParamObj, methodParameter.getName());
            webBindingInitializer.initBinder(binder);
            binder.bind(request);
            methodParamObjs[idx] = methodParamObj;
        }
        Object result = handler.getMethod().invoke(handler.getBean(), methodParamObjs);
        response.getWriter().append(result.toString());
    }
}
