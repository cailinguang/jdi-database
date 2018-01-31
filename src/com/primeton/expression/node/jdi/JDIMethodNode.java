package com.primeton.expression.node.jdi;

import com.primeton.expression.ExpressionContext;
import com.primeton.expression.JDIExpressionUtil;
import com.primeton.expression.node.Node;
import com.sun.jdi.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by clg on 2018/1/26.
 */
public class JDIMethodNode implements Node {

    private Node object;
    private String method;
    private Node[] args;

    public JDIMethodNode(Node object, String method, Node[] args) {
        this.object = object;
        this.method = method;
        this.args = args;
    }

    @Override
    public Object express(ExpressionContext context) {
        if(object==null||method==null||args==null){
            throw new IllegalAccessError("node member variable can't null ");
        }
        ThreadReference thread = (ThreadReference) context.getObject("thread");
        if(thread==null){
            new IllegalAccessException("context don't have ThreadReference obj,key is 'thread'");
        }

        if(object instanceof JDIClassTypeNode){
            Object oriObject = object.express(context);
            if(oriObject==null){
                throw new IllegalAccessError("object node return is null,"+object);
            }

            ClassType classType = (ClassType)oriObject;
            List<com.sun.jdi.Method> jdiMethods = classType.methodsByName(method);
            com.sun.jdi.Method oriJdiMethod = null;
            try {
                for (com.sun.jdi.Method jdiMethod : jdiMethods) {
                    if (jdiMethod.argumentTypes().size() == this.args.length) {
                        oriJdiMethod = jdiMethod;
                        break;
                    }
                }
            }catch (Exception e){
                throw new RuntimeException(e);
            }
            if(oriJdiMethod==null){
                throw new IllegalAccessError("object don't have method");
            }
            if(!oriJdiMethod.isStatic()){
                throw new RuntimeException(oriJdiMethod.name()+" is not static !");
            }
            //get args
            List<Value> args = new ArrayList();
            int index=0;
            for(Node argNode:this.args){
                args.add(JDIExpressionUtil.getValueFromPrimitive(thread.virtualMachine(),argNode.express(context)));
            }

            //invoke method
            try {
                Value value = classType.invokeMethod(thread, oriJdiMethod, args, ObjectReference.INVOKE_SINGLE_THREADED);
                return JDIExpressionUtil.getObjFromRefrence(value);
            }catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException("jdi objectReference invoke method error:",e);
            }

        }

        Object oriObject = object.express(context);
        if(oriObject==null){
            throw new IllegalAccessError("object node return is null,"+object);
        }

        ObjectReference objectReference = (ObjectReference)oriObject;
        List<com.sun.jdi.Method> jdiMethods = objectReference.referenceType().methodsByName(method);
        com.sun.jdi.Method oriJdiMethod = null;
        try {
            for (com.sun.jdi.Method jdiMethod : jdiMethods) {
                if (jdiMethod.argumentTypes().size() == this.args.length) {
                    oriJdiMethod = jdiMethod;
                    break;
                }
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        if(oriJdiMethod==null){
            throw new IllegalAccessError("object don't have method");
        }

        //get args
        List<Value> args = new ArrayList();
        int index=0;
        for(Node argNode:this.args){
            args.add(JDIExpressionUtil.getValueFromPrimitive(thread.virtualMachine(),argNode.express(context)));
        }

        //invoke method
        try {
            Value value = objectReference.invokeMethod(thread, oriJdiMethod, args, ObjectReference.INVOKE_SINGLE_THREADED);
            return JDIExpressionUtil.getObjFromRefrence(value);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("jdi objectReference invoke method error:",e);
        }

    }
}
