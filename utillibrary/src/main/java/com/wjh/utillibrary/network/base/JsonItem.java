package com.wjh.utillibrary.network.base;

/**
 * Created by wjh on 2018/6/25.
 */

public class JsonItem {
    private String key;
    private Object value;

    public JsonItem(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
