package com.primeton.expression.node;

import com.primeton.expression.ExpressionContext;

/**
 * Created by clg on 2018/1/24.
 */
public class StringNode implements Node{
    private String string;

    public StringNode(String string) {
        this.string = string;
    }

    @Override
    public Object express(ExpressionContext context) {
        return this.string;
    }
}
