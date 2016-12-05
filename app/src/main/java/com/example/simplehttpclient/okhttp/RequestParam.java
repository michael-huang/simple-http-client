package com.example.simplehttpclient.okhttp;

/**
 * Created by huangyanzhen on 2016/12/5.
 */

public class RequestParam {

    private String key;
    private Object object;

    public RequestParam(String key, Object object) {
        this.key = key;
        this.object = object;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
