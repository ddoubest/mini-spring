package com.minis.beans;

import com.minis.enums.PropertyType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArgumentValues {
    private final List<ArgumentValue> argumentValueList = new ArrayList<>();

    public ArgumentValues() {
    }

    public void addArgumentValue(ArgumentValue argumentValue) {
        this.argumentValueList.add(argumentValue);
    }

    public ArgumentValue getIndexedArgumentValue(int index) {
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
            ArgumentValue argumentValue = argumentValueList.get(i);
            PropertyType concretePropertyType = PropertyType.getConcretePropertyType(argumentValue.getType());
            objs[i] = Objects.requireNonNull(concretePropertyType).parseValue(argumentValue.getValue());
        }
        return objs;
    }
}
