package com.primeton.expression.node;

import com.primeton.expression.ExpressionContext;

/**
 * Created by clg on 2018/1/24.
 */
public class VariableNode implements Node{
    private String varName;


    public VariableNode(String varName){
        this.varName = varName;
    }

    @Override
    public Object express(ExpressionContext context) {
        return context.getObject(this.varName);
    }

    @Override
    public String toString() {
        return "VariableNode{" +
                "varName='" + varName + '\'' +
                '}';
    }
}
