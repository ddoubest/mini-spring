package com.minis.beans.factory;

public interface FactoryBean<T> {
    T getObject() throws Exception;

    Class<T> getObjectType();

    default boolean isSingleton() {
        return true;
    }
}