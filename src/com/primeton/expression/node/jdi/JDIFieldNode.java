package com.primeton.expression.node.jdi;

import com.primeton.expression.ExpressionContext;
import com.primeton.expression.JDIExpressionUtil;
import com.primeton.expression.node.Node;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.Value;

import java.lang.reflect.Field;

/**
 * Created by clg on 2018/1/26.
 */
public class JDIFieldNode implements Node{
    private Node object;
    private Node filed;

    public JDIFieldNode(Node object, Node filed) {
        this.object = object;
        this.filed = filed;
    }

    @Override
    public Object express(ExpressionContext context) {
        if(object==null||filed==null){
            throw new IllegalAccessError("node member variable can't null ");
        }
        Object oriObj = object.express(context);
        String fieldName = (String)filed.express(context);

        if(oriObj==null||fieldName==null){
            throw new IllegalAccessError("objectNode or filedNode eval express return is null!");
        }

        ObjectReference objectReference = (ObjectReference) oriObj;
        com.sun.jdi.Field jdiField = objectReference.referenceType().fieldByName(fieldName);
        Value value = objectReference.getValue(jdiField);

        return JDIExpressionUtil.getObjFromRefrence(value);
    }
}
