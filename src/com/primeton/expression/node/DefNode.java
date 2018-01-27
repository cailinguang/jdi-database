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
    public Object express(ExpressionContext context) {
        if(varName==null||left==null){
            throw new IllegalAccessError("node member variable can't null,"+this);
        }
        Object object = left.express(context);
        context.putObject(varName,object);
        return object;
    }

    @Override
    public String toString() {
        return "DefNode{" +
                "varName='" + varName + '\'' +
                ", left=" + left +
                '}';
    }
}
