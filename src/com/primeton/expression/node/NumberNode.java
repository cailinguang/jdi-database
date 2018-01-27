package com.primeton.expression.node;

import com.primeton.expression.ExpressionContext;

/**
 * Created by clg on 2018/1/24.
 */
public class NumberNode implements Node{
    private Number number;

    public NumberNode(Number number) {
        this.number = number;
    }

    @Override
    public Object express(ExpressionContext context) {
        return this.number;
    }

    @Override
    public String toString() {
        return "NumberNode{" +
                "number=" + number +
                '}';
    }
}
