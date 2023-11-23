package com.minis.web;

import com.minis.web.servlet.HandlerMethod;
import com.minis.web.servlet.RequestMappingHandlerAdapter;
import com.minis.web.servlet.RequestMappingHandlerMapping;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class DispatcherServlet extends HttpServlet {
    public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.class.getName() + ".CONTEXT";

    private List<String> packageNames;

    private List<String> controllerNames = new ArrayList<>(); // 存全类名
    private final Map<String, Class<?>> controllerClasses = new HashMap<>(); // controller的全类名作为key
    private final Map<String, Object> controllerObjs = new HashMap<>(); // controller的全类名作为key

    private WebApplicationContext webApplicationContext;
    private WebApplicationContext parentWebApplicationContext;
    private RequestMappingHandlerMapping handlerMapping;
    private RequestMappingHandlerAdapter handlerAdapter;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        this.parentWebApplicationContext = (WebApplicationContext) this.getServletContext()
                .getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

        String configLocation = config.getInitParameter("contextConfigLocation");

        URL xmlPath;
        try {
            xmlPath = config.getServletContext().getResource(configLocation);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);

        this.webApplicationContext = new AnnotationConfigWebApplicationContext(configLocation, this.parentWebApplicationContext);

        refresh();
    }

    private void refresh() {
        initController();
        initHandlerMappings(this.webApplicationContext);
        initHandlerAdapters(this.webApplicationContext);
    }

    private void initController() {
        controllerNames = scanPackages(packageNames);
        for (String controllerName : controllerNames) {
            try {
                Class<?> clazz = controllerClasses.get(controllerName);
                Object obj = clazz.getDeclaredConstructor().newInstance();
                controllerObjs.put(controllerName, obj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private List<String> scanPackages(List<String> packageNames) {
        List<String> result = new ArrayList<>();
        for (String packageName : packageNames) {
            result.addAll(Objects.requireNonNull(scanPackage(packageName)));
        }
        return result;
    }

    private List<String> scanPackage(String packageName) {
        List<String> result = new ArrayList<>();

        String relativePath = packageName.replace(".", "/");
        URI packagePath;
        try {
            packagePath = this.getClass().getResource("/" + relativePath).toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        File dir = new File(packagePath);
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                result.addAll(scanPackage(packageName + "." + file.getName()));
            } else {
                String controllerName = packageName + "." + file.getName().replace(".class", "");
                Class<?> clazz = null;
                try {
                    clazz = Class.forName(controllerName);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                // 只要Component注解的类
                if (!clazz.isAnnotationPresent(Controller.class)) {
                    continue;
                }
                controllerClasses.put(controllerName, clazz);
                result.add(controllerName);
            }
        }

        return result;
    }

    protected void initHandlerMappings(WebApplicationContext wac) {
        this.handlerMapping = new RequestMappingHandlerMapping(wac);
    }

    protected void initHandlerAdapters(WebApplicationContext wac) {
        this.handlerAdapter = new RequestMappingHandlerAdapter(wac);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.webApplicationContext);
        try {
            doDispatch(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerMethod handlerMethod = this.handlerMapping.getHandler(request);
        if (handlerMethod == null) {
            return;
        }
        this.handlerAdapter.handle(request, response, handlerMethod);
    }
}
