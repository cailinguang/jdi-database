package com.primeton.expression;

import com.sun.jdi.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;

/**
 * Created by clg on 2018/1/23.
 */
public class JDIExpressionUtil {

    public static Value getValueFromPrimitive(VirtualMachine vm, Object primitiveValue){
        Value jdiValue = null;
        if(primitiveValue instanceof Boolean){
            jdiValue = vm.mirrorOf((Boolean)primitiveValue);
        }
        else if(primitiveValue instanceof  Byte){
            jdiValue = vm.mirrorOf((Byte)primitiveValue);
        }
        else if(primitiveValue instanceof  Character){
            jdiValue = vm.mirrorOf((Character)primitiveValue);
        }
        else if(primitiveValue instanceof  Short){
            jdiValue = vm.mirrorOf((Short)primitiveValue);
        }
        else if(primitiveValue instanceof  Integer){
            jdiValue = vm.mirrorOf((Integer)primitiveValue);
        }
        else if(primitiveValue instanceof  Long){
            jdiValue = vm.mirrorOf((Long)primitiveValue);
        }
        else if(primitiveValue instanceof  Float){
            jdiValue = vm.mirrorOf((Float)primitiveValue);
        }
        else if(primitiveValue instanceof  Double){
            jdiValue = vm.mirrorOf((Double)primitiveValue);
        }
        else if(primitiveValue instanceof  String){
            jdiValue = vm.mirrorOf((String)primitiveValue);
        }
        else if(primitiveValue instanceof Value){
            return (Value) primitiveValue;
        }
        else{
            throw new IllegalArgumentException("value not a primitive value!");
        }
        return jdiValue;
    }

    public static Object getObjFromRefrence(Value value,ThreadReference thread){
        if(value instanceof BooleanValue){
            return ((BooleanValue)value).value();
        }
        else if(value instanceof ByteValue){
            return ((ByteValue)value).value();
        }
        else if(value instanceof  CharValue){
            return ((CharValue)value).value();
        }
        else if(value instanceof  ShortValue){
            return ((ShortValue)value).value();
        }
        else if(value instanceof  IntegerValue){
            return ((IntegerValue)value).value();
        }
        else if(value instanceof  LongValue){
            return ((LongValue)value).value();
        }
        else if(value instanceof  FloatValue){
            return ((FloatValue)value).value();
        }
        else if(value instanceof  DoubleValue){
            return ((DoubleValue)value).value();
        }
        else if(value instanceof  StringReference){
            return ((StringReference)value).value();
        }
        else if(value instanceof ObjectReference){
            String className = ((ObjectReference)value).referenceType().name();
            if(className.equals("java.util.Date")||className.equals("java.sql.Date")||className.equals("java.sql.Timestamp")){
                //Value fastTime = ((ObjectReference)value).getValue(((ObjectReference)value).referenceType().fieldByName("fastTime"));
                try {
                    Value time = ((ObjectReference) value).invokeMethod(thread, ((ObjectReference) value).referenceType().methodsByName("getTime").get(0), Collections.emptyList(), ObjectReference.INVOKE_NONVIRTUAL);
                    return new Date((Long)getObjFromRefrence(time,thread));
                }catch (Exception e){e.printStackTrace();}
                return null;
            }
            if(className.equals("java.math.BigDecimal")){
                try {
                    Value time = ((ObjectReference) value).invokeMethod(thread, ((ObjectReference) value).referenceType().methodsByName("toString").get(0), Collections.emptyList(), ObjectReference.INVOKE_NONVIRTUAL);
                    return new BigDecimal((String)getObjFromRefrence(time,thread));
                }catch (Exception e){e.printStackTrace();}
                return null;
            }
            return (ObjectReference)value;
        }
        return null;
    }
}
