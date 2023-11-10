package com.minis.beans.factory.config;

public class ConstructorArgumentValue {
    private final String type;
    private final String name;
    private final String value;

    public ConstructorArgumentValue(String type, String name, String value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
