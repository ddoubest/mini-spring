package com.minis.web.servlet;

import com.minis.exceptions.BeansException;
import com.minis.web.RequestMapping;
import com.minis.web.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

public class RequestMappingHandlerMapping implements HandlerMapping {
    private final WebApplicationContext webApplicationContext;
    private final MappingRegistry mappingRegistry = new MappingRegistry();

    public RequestMappingHandlerMapping(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
        initMapping();
    }

    protected void initMapping() {
        String[] controllerNames = webApplicationContext.getBeanDefinitionNames();
        for (String controllerName : controllerNames) {
            Object controllerObj;
            try {
                controllerObj = webApplicationContext.getBean(controllerName);
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
            Class<?> beanType = controllerObj.getClass();
            for (Method method : beanType.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    String urlMappingName = requestMapping.value();
                    mappingRegistry.getUrlMappingNames().add(urlMappingName);
                    mappingRegistry.getMappingObjs().put(urlMappingName, controllerObj);
                    mappingRegistry.getMappingMethods().put(urlMappingName, method);
                }
            }
        }
    }

    @Override
    public HandlerMethod getHandler(HttpServletRequest request) throws Exception {
        String servletPath = request.getServletPath();
        if (!mappingRegistry.getUrlMappingNames().contains(servletPath)) {
            return null;
        }
        Method method = this.mappingRegistry.getMappingMethods().get(servletPath);
        Object obj = this.mappingRegistry.getMappingObjs().get(servletPath);
        return new HandlerMethod(method, obj);
    }
}
