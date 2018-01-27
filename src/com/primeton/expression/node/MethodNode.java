package com.primeton.expression.node;

import com.primeton.expression.ExpressionContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by clg on 2018/1/26.
 */
public class MethodNode implements Node {

    private Node object;
    private String method;
    private Node[] args;

    public MethodNode(Node object, String method, Node[] args) {
        this.object = object;
        this.method = method;
        this.args = args;
    }

    @Override
    public Object express(ExpressionContext context) {

        if(object==null||method==null||args==null){
            throw new IllegalAccessError("node member variable can't null,"+this);
        }

        Object oriObject = object.express(context);
        if(oriObject==null){
            throw new IllegalAccessError("object node return is null,"+object);
        }
        //get method
        Method method = null;
        for(Method classMethod:oriObject.getClass().getDeclaredMethods()){
            if(classMethod.getName().equals(this.method) && classMethod.getParameterCount()==args.length){
                method = classMethod;
            }
        }
        if(method==null){
            throw new IllegalAccessError("object:"+object+" cant't find method:"+this.method);
        }
        //get args
        Object[] args = new Object[this.args.length];
        int index=0;
        for(Node argNode:this.args){
            args[index++]=argNode.express(context);
        }
        method.setAccessible(true);
        try {
            return method.invoke(oriObject,args);
        } catch (Exception e) {
            throw new RuntimeException("调用方法失败",e);
        }
    }

    @Override
    public String toString() {
        return "MethodNode{" +
                "object=" + object +
                ", method='" + method + '\'' +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
