package com.primeton.expression.node;

import com.primeton.expression.ExpressionContext;

/**
 * Created by clg on 2018/1/24.
 */
public class VariableNode implements Node{
    private String varName;

    private ExpressionContext ctx;

    public VariableNode(String varName,ExpressionContext ctx){
        this.varName = varName;
        this.ctx = ctx;
    }

    @Override
    public Object express() {
        return ctx.getObject(varName);
    }
}
