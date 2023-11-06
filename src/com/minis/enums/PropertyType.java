package com.minis.enums;

public enum PropertyType {
    STRING("String", String.class),
    INTEGER("Integer", Integer.class),
    INT("int", int.class),
    ;


    private final String simplifyType;
    private final Class<?> typeClazz;

    PropertyType(String simplifyType, Class<?> typeClazz) {
        this.simplifyType = simplifyType;
        this.typeClazz = typeClazz;
    }

    public String getSimplifyType() {
        return simplifyType;
    }

    public Class<?> getTypeClazz() {
        return typeClazz;
    }

    public Object parseValue(String value) {
        switch (this) {
            case INTEGER: {
                return Integer.parseInt(value);
            }
            case INT: {
                return Integer.parseInt(value);
            }
        }
        return value;
    }

    public static Class<?> getTypeClazz(String type) {
        for (PropertyType propertyType : PropertyType.values()) {
            if (propertyType.getSimplifyType().equals(type)) {
                return propertyType.getTypeClazz();
            }
        }
        return null;
    }


    public static PropertyType getConcretePropertyType(String type) {
        for (PropertyType propertyType : PropertyType.values()) {
            if (propertyType.getSimplifyType().equals(type)) {
                return propertyType;
            }
        }
        return null;
    }
}
