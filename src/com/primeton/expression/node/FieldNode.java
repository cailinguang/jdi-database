package com.primeton.expression.node;

import com.primeton.expression.ExpressionContext;

import java.lang.reflect.Field;

/**
 * Created by clg on 2018/1/26.
 */
public class FieldNode implements Node{
    private Node object;
    private Node filed;

    public FieldNode(Node object, Node filed) {
        this.object = object;
        this.filed = filed;
    }

    @Override
    public Object express(ExpressionContext context) {
        if(object==null||filed==null){
            throw new IllegalAccessError("node member variable can't null,"+this);
        }
        Object oriObj = object.express(context);
        String fieldName = (String)filed.express(context);

        if(oriObj==null){
            throw new IllegalAccessError("object node express return null:"+object);
        }
        if(fieldName==null){
            throw new IllegalAccessError("file node express return null:"+filed);
        }

        try {
            Field field = oriObj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(oriObj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "FieldNode{" +
                "object=" + object +
                ", filed=" + filed +
                '}';
    }
}
