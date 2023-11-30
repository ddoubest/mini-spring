package com.minis.beans;

public interface PropertyEditor {
    void setFromText(String text);
    void setValue(Object value);
    Object getValue();
    String getAsText();
}