package com.minis.web;

public class MappingValue {
    private String uri;
    private String clazz;
    private String method;

    public MappingValue(String uri, String clazz, String method) {
        this.uri = uri;
        this.clazz = clazz;
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
