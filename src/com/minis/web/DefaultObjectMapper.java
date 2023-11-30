package com.minis.web;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DefaultObjectMapper implements ObjectMapper{
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DECIMAL_FORMAT = "###.##";

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
    private DecimalFormat decimalFormatter = new DecimalFormat(DEFAULT_DECIMAL_FORMAT);

    public static boolean isDecimal(Class<?> clazz) {
        return BigDecimal.class.isAssignableFrom(clazz) || Float.class.isAssignableFrom(clazz) || float.class.isAssignableFrom(clazz)
                || Double.class.isAssignableFrom(clazz) || double.class.isAssignableFrom(clazz);
    }

    public static boolean isPrimitive(Class<?> clazz) {
        try {
            if (clazz.isPrimitive()) {
                return true;
            }
            return ((Class<?>) clazz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void setDateFormat(String dateFormat) {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
    }

    @Override
    public void setDecimalFormat(String decimalFormat) {
        this.decimalFormatter = new DecimalFormat(decimalFormat);
    }

    @Override
    public String writeValuesAsString(Object obj) {
        if (obj == null) return "null";

        Class<?> clazz = obj.getClass();
        StringBuilder jsonObj = new StringBuilder();

        try {
            if (clazz.isArray()) {
                jsonObj.append("[");
                for (int i = 0; i < Array.getLength(obj); i++) {
                    if (i != 0) {
                        jsonObj.append(",");
                    }
                    jsonObj.append(writeValuesAsString(Array.get(obj, i)));
                }
                jsonObj.append("]");
            } else if (isDecimal(clazz)) {
                jsonObj.append(writeValuesAsString(this.decimalFormatter.format(obj)));
            } else if (isPrimitive(clazz)) {
                jsonObj.append(obj);
            } else if (String.class.isAssignableFrom(clazz)) {
                jsonObj.append("\"").append(obj).append("\"");
            } else if (Date.class.isAssignableFrom(clazz)) {
                LocalDate localDate = ((Date) obj).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                jsonObj.append(writeValuesAsString(dateTimeFormatter.format(localDate)));
            } else {
                jsonObj.append("{");
                int idx = 0;
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);

                    if (idx != 0) {
                        jsonObj.append(",");
                    }
                    idx ++;

                    String fieldName = field.getName();
                    Object fieldObj = field.get(obj);

                    jsonObj.append("\"").append(fieldName).append("\"").append(":").append(writeValuesAsString(fieldObj));
                }
                jsonObj.append("}");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return jsonObj.toString();
    }
}
