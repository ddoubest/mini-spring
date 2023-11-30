package com.minis.beans;

import com.minis.utils.NumberUtils;
import com.minis.utils.StringUtils;

import java.text.NumberFormat;

public class CustomNumberEditor implements PropertyEditor{
    private Class<? extends Number> numberClass;
    private NumberFormat numberFormat;
    private boolean allowEmtpy;
    private Object value;

    public CustomNumberEditor(Class<? extends Number> numberClass, boolean allowEmtpy) {
        this(numberClass, null, allowEmtpy);
    }

    public CustomNumberEditor(Class<? extends Number> numberClass, NumberFormat numberFormat, boolean allowEmtpy) {
        this.numberClass = numberClass;
        this.numberFormat = numberFormat;
        this.allowEmtpy = allowEmtpy;
    }

    @Override
    public void setFromText(String text) {
        if (allowEmtpy && StringUtils.isEmpty(text)) {
            setValue(null);
            return;
        }

        if (numberFormat != null) {
            setValue(NumberUtils.parseNumber(text, this.numberClass, this.numberFormat));
        } else {
            setValue(NumberUtils.parseNumber(text, this.numberClass));
        }
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Number) {
            this.value = NumberUtils.convertNumberToTargetClass((Number) value, this.numberClass);
        } else {
            this.value = value;
        }
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public String getAsText() {
        if (value == null) {
            return "";
        }

        if (numberFormat != null) {
            return numberFormat.format(value);
        } else {
            return value.toString();
        }
    }
}
