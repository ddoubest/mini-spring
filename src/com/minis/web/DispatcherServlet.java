package com.minis.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class DispatcherServlet extends HttpServlet {
    private List<String> packageNames;
    private List<String> controllerNames = new ArrayList<>(); // 存全类名
    private final Map<String, Class<?>> controllerClasses = new HashMap<>(); // controller的全类名作为key
    private final Map<String, Object> controllerObjs = new HashMap<>(); // controller的全类名作为key
    private final List<String> urlMappingNames = new ArrayList<>();
    private final Map<String, Object> mappingObjs = new HashMap<>();
    private final Map<String, Method> mappingMethods = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        String configLocation = config.getInitParameter("contextConfigLocation");
        URL xmlPath = null;
        try {
            xmlPath = config.getServletContext().getResource(configLocation);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);

        refresh();
    }

    private void refresh() {
        initController();
        initMapping();
    }

    private void initMapping() {
        for (String controllerName : controllerNames) {
            Class<?> controllerClazz = controllerClasses.get(controllerName);
            Object controllerObj = controllerObjs.get(controllerName);
            for (Method method : controllerClazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    String url = requestMapping.value();
                    urlMappingNames.add(url);
                    mappingMethods.put(url, method);
                    mappingObjs.put(url, controllerObj);
                }
            }
        }
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
                // 只要Controller注解的类
                if (!clazz.isAnnotationPresent(Controller.class)) {
                    continue;
                }
                controllerClasses.put(controllerName, clazz);
                result.add(controllerName);
            }
        }

        return result;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();

        if (!urlMappingNames.contains(servletPath)) {
            return;
        }

        Object obj = mappingObjs.get(servletPath);
        Method method = mappingMethods.get(servletPath);

        Object result = null;
        try {
            result = method.invoke(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        resp.getWriter().append(result.toString());
    }
}
