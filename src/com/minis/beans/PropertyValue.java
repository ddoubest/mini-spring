package com.minis.beans;

public class PropertyValue {
    private final String type;
    private final String name;
    private final String value;
    private final boolean isRef;

    public PropertyValue(String type, String name, String value, Boolean isRef) {
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
