package com.minis.web;

import com.minis.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DefaultHttpMessageConverter implements  HttpMessageConverter{
    public static final String DEFAULT_CONTENT_TYPE = "text/json;charset=UTF-8";
    public static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";
    @Autowired
    private ObjectMapper objectMapper;

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void write(Object obj, HttpServletResponse response) throws IOException {
        response.setContentType(DEFAULT_CONTENT_TYPE);
        response.setCharacterEncoding(DEFAULT_CHARACTER_ENCODING);
        writeInternal(obj, response);
        response.flushBuffer();
    }

    private void writeInternal(Object obj, HttpServletResponse response) throws IOException {
        String jsonObj = getObjectMapper().writeValuesAsString(obj);
        response.getWriter().write(jsonObj);
    }
}
