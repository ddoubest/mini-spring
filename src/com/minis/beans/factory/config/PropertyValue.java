package com.minis.beans.factory.config;

public class PropertyValue {
    private final String type;
    private final String name;
    private final String value;
    private final boolean isRef;

    public PropertyValue(String type, String name, String value, boolean isRef) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.isRef = isRef;
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

    public boolean isRef() {
        return isRef;
    }
}
