package com.minis.web;

import com.minis.beans.PropertyEditor;
import com.minis.beans.PropertyEditorRegistrySupport;
import com.minis.beans.factory.config.PropertyValue;
import com.minis.beans.factory.config.PropertyValues;
import com.minis.utils.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BeanWrapperImpl extends PropertyEditorRegistrySupport {
    private Object wrappedTarget;
    private Class<?> targetClazz;
    private PropertyValues propertyValues;

    public BeanWrapperImpl(Object target) {
        this.wrappedTarget = target;
        targetClazz = target.getClass();
    }

    public void setBeanInstance(Object wrappedTarget) {
        this.wrappedTarget = wrappedTarget;
    }

    public Object getBeanInstance() {
        return this.wrappedTarget;
    }

    //绑定参数值
    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
        for (PropertyValue propertyValue : propertyValues.getPropertyValueList()) {
            setPropertyValue(propertyValue);
        }
    }

    //绑定具体某个参数
    public void setPropertyValue(PropertyValue propertyValue) {
        BeanPropertyHandler beanPropertyHandler = new BeanPropertyHandler(propertyValue.getName());
        PropertyEditor propertyEditor = this.findCustomEditor(beanPropertyHandler.getPropertyClazz());
        if (propertyEditor == null) {
            propertyEditor = this.getDefaultEditor(beanPropertyHandler.getPropertyClazz());
        }
        // 转换
        propertyEditor.setFromText(propertyValue.getValue());
        // 取出
        beanPropertyHandler.setValue(propertyEditor.getValue());
    }

    //一个内部类，用于处理参数，通过getter()和setter()操作属性
    class BeanPropertyHandler {
        private Method writeMethod;
        private Method readMethod;
        private Class<?> propertyClazz;

        public BeanPropertyHandler(String propertyName) {
            try {
                Field field = targetClazz.getDeclaredField(propertyName);
                propertyClazz = field.getType();

                this.writeMethod = targetClazz.getDeclaredMethod(
                        "set" + StringUtils.upperFirstCase(propertyName),
                        propertyClazz
                );

                this.readMethod = targetClazz.getDeclaredMethod(
                        "get" + StringUtils.upperFirstCase(propertyName)
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public Class<?> getPropertyClazz() {
            return this.propertyClazz;
        }

        //调用getter读属性值
        public Object getValue() {
            readMethod.setAccessible(true);
            try {
                return readMethod.invoke(wrappedTarget);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        //调用setter设置属性值
        public void setValue(Object value) {
            writeMethod.setAccessible(true);
            try {
                writeMethod.invoke(wrappedTarget, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}