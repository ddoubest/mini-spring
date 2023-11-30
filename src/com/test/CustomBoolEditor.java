package com.test;

import com.minis.beans.PropertyEditor;

public class CustomBoolEditor implements PropertyEditor {
    private Boolean value;

    @Override
    public void setFromText(String text) {
        if ("false".equals(text) || "False".equals(text)) {
            value = false;
        } else if ("true".equals(text) || "True".equals(text)) {
            value = true;
        } else {
            throw new RuntimeException("not support");
        }
    }

    @Override
    public void setValue(Object value) {
        this.value = (Boolean) value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getAsText() {
        return value.toString();
    }
}
