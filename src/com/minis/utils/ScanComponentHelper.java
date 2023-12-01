package com.minis.utils;

import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.support.AbstractBeanFactory;
import com.minis.core.Component;
import com.minis.web.Controller;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScanComponentHelper {
    public static List<String> scanPackages(List<String> packageNames) {
        List<String> result = new ArrayList<>();
        for (String packageName : packageNames) {
            result.addAll(Objects.requireNonNull(scanPackage(packageName)));
        }
        return result;
    }

    public static void loadBeanDefinitions(AbstractBeanFactory beanFactory, List<String> names) {
        for (String name : names) {
            String[] decomposedNames = name.split("\\.");
            String id = StringUtils.lowerFirstCase(decomposedNames[decomposedNames.length - 1]);
            BeanDefinition beanDefinition = new BeanDefinition(id, name);
            beanFactory.registerBeanDefinition(id, beanDefinition);
        }
    }

    private static List<String> scanPackage(String packageName) {
        List<String> result = new ArrayList<>();

        String relativePath = packageName.replace(".", "/");
        URI packagePath;
        try {
            packagePath = ScanComponentHelper.class.getResource("/" + relativePath).toURI();
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
                if (!clazz.isAnnotationPresent(Component.class) && !clazz.isAnnotationPresent(Controller.class)) {
                    continue;
                }
                result.add(controllerName);
            }
        }

        return result;
    }
}
