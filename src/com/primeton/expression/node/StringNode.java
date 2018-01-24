package com.primeton.expression.node;

/**
 * Created by clg on 2018/1/24.
 */
public class StringNode implements Node{
    private String string;

    @Override
    public Object express() {
        return this.string;
    }
}
