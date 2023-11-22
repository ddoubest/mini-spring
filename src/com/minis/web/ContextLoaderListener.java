package com.minis.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {
    public static final String CONFIG_LOCATION = "contextConfigLocation";
    private WebApplicationContext context;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        initWebApplicationContext(sce.getServletContext());
    }

    private void initWebApplicationContext(ServletContext servletContext) {
        String configLocation = servletContext.getInitParameter(CONFIG_LOCATION);
        WebApplicationContext wac = new AnnotationConfigWebApplicationContext(configLocation);
        wac.setServletContext(servletContext);
        this.context = wac;
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.context);
    }
}
