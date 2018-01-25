package com.primeton.expression.node;

import com.primeton.expression.ExpressionContext;

/**
 * Created by clg on 2018/1/24.
 */
public class DefNode implements Node{
    private String varName;

    private Node left;

    public DefNode(String varName, Node left){
        this.varName = varName;
        this.left = left;
    }

    @Override
    public Object express() {
        //TODO
        return null;
    }
}
