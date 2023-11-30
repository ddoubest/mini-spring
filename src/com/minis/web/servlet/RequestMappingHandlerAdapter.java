package com.minis.web.servlet;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.web.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class RequestMappingHandlerAdapter implements HandlerAdapter{
    private WebApplicationContext webApplicationContext;
    @Autowired
    private WebBindingInitializer webBindingInitializer;
    @Autowired
    private HttpMessageConverter messageConverter;

    public RequestMappingHandlerAdapter() {
    }

    public RequestMappingHandlerAdapter(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return handleInternal(request, response, (HandlerMethod) handler);
    }

    private ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
        try {
            return invokeHandlerMethod(request, response, handler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ModelAndView invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        WebDataBinderFactory webDataBinderFactory = new WebDataBinderFactory();
        Method invokeMethod = handler.getMethod();
        Parameter[] methodParameters = invokeMethod.getParameters();
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

        Object result = invokeMethod.invoke(handler.getBean(), methodParamObjs);

        ModelAndView modelAndView = null;
        if (invokeMethod.isAnnotationPresent(ResponseBody.class)) {
            this.messageConverter.write(result, response);
        } else {
            if (result instanceof ModelAndView) {
                return (ModelAndView) result;
            } else if (result instanceof String) {
                modelAndView = new ModelAndView((String) result);
            }
        }

        return modelAndView;
    }

    public WebBindingInitializer getWebBindingInitializer() {
        return webBindingInitializer;
    }

    public void setWebBindingInitializer(WebBindingInitializer webBindingInitializer) {
        this.webBindingInitializer = webBindingInitializer;
    }

    public HttpMessageConverter getMessageConverter() {
        return messageConverter;
    }

    public void setMessageConverter(HttpMessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }
}
