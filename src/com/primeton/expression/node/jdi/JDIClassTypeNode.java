package com.primeton.expression.node.jdi;

import com.primeton.expression.ExpressionContext;
import com.primeton.expression.JDIExpressionUtil;
import com.primeton.expression.node.Node;
import com.sun.jdi.ClassType;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Value;

/**
 * Created by clg on 2018/1/26.
 */
public class JDIClassTypeNode implements Node{
    String classType;

    public JDIClassTypeNode(String classType) {
        this.classType = classType;
    }

    @Override
    public Object express(ExpressionContext context) {
        if(classType==null){
            throw new IllegalArgumentException("class type is null");
        }
        ThreadReference thread = (ThreadReference) context.getObject("thread");
        if(thread==null){
            new IllegalAccessException("context don't have ThreadReference obj,key is 'thread'");
        }

        ClassType classType = (ClassType) thread.virtualMachine().classesByName(this.classType).get(0);

        return classType;
    }
}
