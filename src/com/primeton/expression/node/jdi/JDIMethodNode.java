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
                Value value = classType.invokeMethod(thread, oriJdiMethod, args, ObjectReference.INVOKE_NONVIRTUAL);
                return JDIExpressionUtil.getObjFromRefrence(value,thread);
            }catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException("jdi objectReference invoke method error:",e);
            }

        }

        Object oriObject = object.express(context);
        if(oriObject==null){
            throw new IllegalAccessError("object node return is null,"+object);
        }

        //get args
        List<Value> args = new ArrayList();
        int index=0;
        for(Node argNode:this.args){
            args.add(JDIExpressionUtil.getValueFromPrimitive(thread.virtualMachine(),argNode.express(context)));
        }


        ObjectReference objectReference = (ObjectReference)oriObject;
        List<com.sun.jdi.Method> jdiMethods = objectReference.referenceType().methodsByName(method);
        com.sun.jdi.Method oriJdiMethod = null;
        try {
            for (com.sun.jdi.Method jdiMethod : jdiMethods) {
                if (jdiMethod.argumentTypes().size() == this.args.length) {
                    if(args.size()==0){
                        oriJdiMethod = jdiMethod;
                        break;
                    }
                    else if(jdiMethod.argumentTypes().get(0).name().equals(args.get(0).type().name())){
                        oriJdiMethod = jdiMethod;
                        break;
                    }

                }
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        if(oriJdiMethod==null){
            throw new IllegalAccessError("object don't have method");
        }


        //invoke method
        try {
            Value value = objectReference.invokeMethod(thread, oriJdiMethod, args, ObjectReference.INVOKE_NONVIRTUAL);
            return JDIExpressionUtil.getObjFromRefrence(value,thread);
        }
        catch (InvocationException invocationException){
            ObjectReference eo = invocationException.exception();
            String message = null;
            try {
                message = ((StringReference) eo.invokeMethod(thread, eo.referenceType().methodsByName("getMessage").get(0), new ArrayList(), ObjectReference.INVOKE_NONVIRTUAL)).value();
            }catch (Exception e){}
            throw new RuntimeException(message,invocationException);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("jdi objectReference invoke method error:",e);
        }

    }
}
