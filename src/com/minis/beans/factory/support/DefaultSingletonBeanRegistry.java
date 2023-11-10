package com.minis.beans.factory.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    protected final List<String> beanNames = new ArrayList<>();
    protected final Map<String, Object> singletons = new ConcurrentHashMap<>();
    protected final Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>();

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletons) {
            singletons.put(beanName, singletonObject);
            beanNames.add(beanName);
        }
    }

    @Override
    public Object getSingleton(String beanName) {
        return singletons.get(beanName);
    }

    @Override
    public Boolean contatinsSingleton(String beanName) {
        return singletons.containsKey(beanName);
    }

    @Override
    public String[] getSingletonNames() {
        return beanNames.toArray(new String[0]);
    }

    protected void removeSingleton(String beanName) {
        if (contatinsSingleton(beanName)) {
            synchronized (singletons) {
                beanNames.remove(beanName);
                singletons.remove(beanName);
            }
        }
    }
}
