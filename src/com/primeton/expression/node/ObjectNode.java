package com.primeton.expression.node;

/**
 * Created by clg on 2018/1/24.
 */
public class ObjectNode implements Node{
    private Object value;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public Object express() {
        return this.value;
    }
}
