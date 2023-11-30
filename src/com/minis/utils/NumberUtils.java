package com.minis.utils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

@SuppressWarnings("unchecked")
public class NumberUtils {
    public static final Class<? extends Number>[] supportNumber = new Class[] {
            Byte.class, Short.class, Integer.class, Long.class, BigInteger.class,
            Float.class, Double.class, BigDecimal.class
    };

    public static <T extends Number> T parseNumber(String text, Class<T> targetClass) {
        text = StringUtils.trimAllWhitespace(text);
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        BigDecimal number = new BigDecimal(text);
        return convertNumberToTargetClass(number, targetClass);
    }

    public static <T extends Number> T parseNumber(
            String text, Class<T> targetClass, NumberFormat numberFormat) {
        if (numberFormat != null) {
            DecimalFormat decimalFormat = null;
            boolean resetFormat = false;
            if (numberFormat instanceof DecimalFormat) {
                decimalFormat = (DecimalFormat) numberFormat;
                if (targetClass == BigDecimal.class && !decimalFormat.isParseBigDecimal()) {
                    decimalFormat.setParseBigDecimal(true);
                    resetFormat = true;
                }
            }

            try {
                Number number = numberFormat.parse(StringUtils.trimAllWhitespace(text));
                return convertNumberToTargetClass(number, targetClass);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Could not parse number: " + e.getMessage());
            } finally {
              if (resetFormat) {
                  decimalFormat.setParseBigDecimal(false);
              }
            }

        } else {
            return parseNumber(text, targetClass);
        }
    }

    public static <T extends Number> T convertNumberToTargetClass(Number number, Class<T> targetClass) {
        if (targetClass.isInstance(number)) {
            return targetClass.cast(number);
        }

        if (!checkSupport(targetClass)) {
            throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" +
                    number.getClass().getName() + "] to unsupported target class [" + targetClass.getName() + "]");
        }

        if (targetClass == BigInteger.class) {
            String numStr = number.toString().split("\\.")[0];
            return targetClass.cast(new BigInteger(numStr));
        } else if (targetClass == BigDecimal.class) {
            return targetClass.cast(new BigDecimal(number.toString()));
        } else {
            String[] t = targetClass.toString().split("\\.");
            String methodName = t[t.length - 1].toLowerCase() + "Value";
            try {
                Method method = Number.class.getMethod(methodName);
                return targetClass.cast(method.invoke(number));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static <T extends Number> boolean checkSupport(Class<T> targetClass) {
        for (Class<? extends Number> clazz : supportNumber) {
            if (clazz == targetClass) {
                return true;
            }
        }

        return false;
    }
}
