package com.minis.web;

import com.minis.exceptions.BeansException;
import com.minis.web.servlet.*;

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
    private static final String HANDLER_ADAPTER_BEAN_NAME = "handlerAdapter";
    private static final String VIEW_RESOLVER_BEAN_NAME = "viewResolver";

    private List<String> packageNames;

    private List<String> controllerNames = new ArrayList<>(); // 存全类名
    private final Map<String, Class<?>> controllerClasses = new HashMap<>(); // controller的全类名作为key
    private final Map<String, Object> controllerObjs = new HashMap<>(); // controller的全类名作为key

    private WebApplicationContext webApplicationContext;
    private WebApplicationContext parentWebApplicationContext;
    private HandlerMapping handlerMapping;
    private HandlerAdapter handlerAdapter;
    private ViewResolver viewResolver;

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
        initViewResolvers(this.webApplicationContext);
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
        try {
            this.handlerAdapter = (HandlerAdapter) wac.getBean(HANDLER_ADAPTER_BEAN_NAME);
        } catch (BeansException e) {
            throw new RuntimeException(e);
        }
    }

    protected void initViewResolvers(WebApplicationContext wac) {
        try {
            this.viewResolver = (ViewResolver) wac.getBean(VIEW_RESOLVER_BEAN_NAME);
        } catch (BeansException e) {
            throw new RuntimeException(e);
        }
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
        ModelAndView modelAndView = this.handlerAdapter.handle(request, response, handlerMethod);
        if (modelAndView != null) {
            render(request, response, modelAndView);
        }
    }

    protected void render(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) throws Exception {
        Map<String, Object> modelMap = mv.getModel();
        String target = mv.getViewName();
        View view = resolveViewName(target, modelMap, request);
        if (view != null) {
            view.render(modelMap, request, response);
        }
    }

    private View resolveViewName(String target, Map<String, Object> modelMap, HttpServletRequest request) throws Exception {
        if (this.viewResolver == null) {
            return null;
        }
        return this.viewResolver.resolveViewName(target);
    }
}
