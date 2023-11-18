package com.minis.beans.factory.support;

import com.minis.beans.factory.AutowireCapableBeanFactory;
import com.minis.beans.factory.ConfigurableBeanFactory;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.ConstructorArgumentValues;
import com.minis.beans.factory.config.PropertyValue;
import com.minis.beans.factory.config.PropertyValues;
import com.minis.enums.PropertyType;
import com.minis.exceptions.BeansException;
import com.minis.utils.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory, BeanDefinitionRegistry, AutowireCapableBeanFactory {
    protected final Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>();

    @Override
    public Object getBean(String beanName) throws BeansException {
        Object bean = getSingleton(beanName);
        if (bean == null) {
            // 尝试从缓存实例中取
            bean = earlySingletonObjects.get(beanName);
            if (bean == null) {
                if (!beanDefinitions.containsKey(beanName)) {
                    throw new BeansException("BeanDefinition not exist!");
                }
                BeanDefinition beanDefinition = beanDefinitions.get(beanName);
                synchronized (beanDefinition) {
                    bean = getSingleton(beanName);
                    if (bean == null) {
                        try {
                            bean = createBean(beanDefinition);
                            registerSingleton(beanDefinition.getId(), bean);
                            // 删除缓存实例
                            earlySingletonObjects.remove(beanName);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        // 预留 beanpostprocessor 位置
                        // step 1: postProcessBeforeInitialization
                        applyBeanPostProcessorBeforeInitialization(bean, beanName);
                        // step 2: afterPropertiesSet
                        // step 3: init-method
                        if (!StringUtils.isEmpty(beanDefinition.getInitMethodName())) {
                            invokeInitMethod(beanDefinition, bean);
                        }
                        // step 4: postProcessAfterInitialization
                        applyBeanPostProcessorAfterInitialization(bean, beanName);
                    }
                }
            }
        }

        return bean;
    }

    private Object createBean(BeanDefinition beanDefinition) {
        // 构造
        Object obj = doCreateBean(beanDefinition);
        // 存放入缓存实例中
        earlySingletonObjects.put(beanDefinition.getId(), obj);
        // setter填充
        handleProperties(beanDefinition, obj);
        return obj;
    }

    private Object doCreateBean(BeanDefinition beanDefinition) {
        Object obj;
        try {
            Class<?> clazz = Class.forName(beanDefinition.getClassName());
            ConstructorArgumentValues CAVS = beanDefinition.getConstructorArgumentValues();
            if (CAVS != null && !CAVS.isEmpty()) {
                Constructor<?> constructor = clazz.getDeclaredConstructor(CAVS.getArgumentTypes());
                obj = constructor.newInstance(CAVS.getArgumentValues());
            } else {
                Constructor<?> constructor = clazz.getDeclaredConstructor();
                obj = constructor.newInstance();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(beanDefinition.getId() + " bean created. "
                + beanDefinition.getClassName() + " : " + obj.toString());
        return obj;
    }

    private void handleProperties(BeanDefinition beanDefinition, Object obj) {
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        if (propertyValues != null && !propertyValues.isEmpty()) {
            for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                String methodName = "set" + StringUtils.upperFirstCase(propertyValue.getName());
                Class<?> typeClazz;
                Object concreteVal;

                if (!propertyValue.isRef()) { // 非依赖引用
                    typeClazz = PropertyType.getTypeClazz(propertyValue.getType());
                    concreteVal = Objects.requireNonNull(PropertyType
                            .getConcretePropertyType(propertyValue.getType()))
                            .parseValue(propertyValue.getValue());
                } else { // 依赖引用
                    try {
                        typeClazz = Class.forName(propertyValue.getType());
                        concreteVal = getBean(propertyValue.getValue());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                try {
                    Method declaredMethod = obj.getClass().getDeclaredMethod(methodName, typeClazz);
                    declaredMethod.invoke(obj, concreteVal);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void invokeInitMethod(BeanDefinition beanDefinition, Object bean) {
        Class<?> clazz = beanDefinition.getBeanClass();
        if (clazz == null) {
            beanDefinition.setBeanClass(bean.getClass());
            clazz = bean.getClass();
        }
        try {
            Method method = clazz.getMethod(beanDefinition.getInitMethodName());
            method.invoke(bean);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建所有的 bean
     */
    public void refresh() {
        for (String beanName : beanDefinitions.keySet()) {
            try {
                getBean(beanName);
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void registerBean(String beanName, Object obj) {
        registerSingleton(beanName, obj);
    }

    @Override
    public Boolean containsBean(String beanName) {
        return contatinsSingleton(beanName) || beanDefinitions.containsKey(beanName);
    }

    @Override
    public boolean isSingleton(String name) {
        return beanDefinitions.get(name).isSingleton();
    }

    @Override
    public boolean isPrototype(String name) {
        return beanDefinitions.get(name).isPrototype();
    }

    @Override
    public Class<?> getType(String name) {
        return beanDefinitions.get(name).getBeanClass();
    }

    @Override
    public void registerBeanDefinition(String name, BeanDefinition bd) {
        beanDefinitions.put(name, bd);
        if (!bd.isLazyInit()) {
            try {
                getBean(name);
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void removeBeanDefinition(String name) {
        beanDefinitions.remove(name);
        removeSingleton(name);
    }

    @Override
    public BeanDefinition getBeanDefinition(String name) {
        return beanDefinitions.get(name);
    }

    @Override
    public boolean containsBeanDefinition(String name) {
        return beanDefinitions.containsKey(name);
    }
}
