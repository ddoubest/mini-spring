package com.minis.beans.factory.config;

import com.minis.enums.PropertyType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConstructorArgumentValues {
    private final List<ConstructorArgumentValue> argumentValueList = new ArrayList<>();

    public void addArgumentValue(ConstructorArgumentValue argumentValue) {
        this.argumentValueList.add(argumentValue);
    }

    public ConstructorArgumentValue getIndexedArgumentValue(int index) {
        return this.argumentValueList.get(index);
    }

    public int getArgumentCount() {
        return (this.argumentValueList.size());
    }

    public boolean isEmpty() {
        return (this.argumentValueList.isEmpty());
    }

    public Class<?>[] getArgumentTypes() {
        Class<?>[] clazzs = new Class<?>[getArgumentCount()];
        for (int i = 0; i < getArgumentCount(); i ++) {
            clazzs[i] = PropertyType.getTypeClazz(argumentValueList.get(i).getType());
        }
        return clazzs;
    }

    public Object[] getArgumentValues() {
        Object[] objs = new Object[getArgumentCount()];
        for (int i = 0; i < getArgumentCount(); i ++) {
            ConstructorArgumentValue argumentValue = argumentValueList.get(i);
            PropertyType concretePropertyType = PropertyType.getConcretePropertyType(argumentValue.getType());
            objs[i] = Objects.requireNonNull(concretePropertyType).parseValue(argumentValue.getValue());
        }
        return objs;
    }
}
