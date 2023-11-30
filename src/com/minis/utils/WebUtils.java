package com.minis.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class WebUtils {
    public static Map<String, String> getParametersStartingWith(HttpServletRequest request, String prefix) {
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, String> params = new HashMap<>();
        if (prefix == null) {
            prefix = "";
        }
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if (prefix.isEmpty() || paramName.startsWith(prefix)) {
                // TODO 感觉多此一举
                String unprefixed = paramName.substring(prefix.length());
                String value = request.getParameter(paramName);

                params.put(unprefixed, value);
            }
        }
        return params;
    }
}