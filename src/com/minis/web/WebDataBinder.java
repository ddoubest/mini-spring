package com.minis.web;


import com.minis.beans.PropertyEditor;
import com.minis.beans.factory.config.PropertyValues;
import com.minis.utils.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class WebDataBinder {
    private final Object target;
    private final String targetName;
    private final Class<?> clazz;
    private BeanWrapperImpl beanWrapper;

    public WebDataBinder(Object target) {
        this(target, "");
    }

    public WebDataBinder(Object target, String targetName) {
        this.target = target;
        this.targetName = targetName;
        this.clazz = this.target.getClass();
    }

    public void bind(HttpServletRequest request) {
        PropertyValues propertyValues = assginParameters(request);
        addBindValue(propertyValues, request);
        doBind(propertyValues);
    }

    private PropertyValues assginParameters(HttpServletRequest request) {
        Map<String, String> map = WebUtils.getParametersStartingWith(request, "");
        return new PropertyValues(map);
    }

    private void addBindValue(PropertyValues propertyValues, HttpServletRequest request) {

    }

    private void doBind(PropertyValues propertyValues) {
        applyPropertyValues(propertyValues);
    }

    private void applyPropertyValues(PropertyValues propertyValues) {
        getPropertyAccessor().setPropertyValues(propertyValues);
    }

    private BeanWrapperImpl getPropertyAccessor() {
        if (beanWrapper == null) {
            beanWrapper = new BeanWrapperImpl(this.target);
        }
        return beanWrapper;
    }

    public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
        getPropertyAccessor().registerCustomEditor(requiredType, propertyEditor);
    }
}