package com.primeton.expression;

import com.sun.jdi.ClassType;
import com.sun.jdi.Method;
import com.sun.jdi.Value;
import com.sun.jdi.event.BreakpointEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clg on 2018/1/23.
 */
public class VMClassFunction {

    public Object execClassMethod(BreakpointEvent event, String clazz,String method){
        try {
            ClassType classType = (ClassType) event.virtualMachine().classesByName(clazz).get(0);
            Method classMethod = null;
            List<Method> methodList = classType.methodsByName(method);
            /*for (Method m : methodList) {
                if (m.argumentTypes().size() == (arguments == null ? 0 : arguments.size())) {
                    classMethod = m;
                    break;
                }
            }*/
            classMethod = methodList.get(0);

            if(classMethod==null){
                return null;
            }
            List<Value> values = new ArrayList();
            /*for(Object arg:arguments){
                values.add(ExpressionUtil.getValueFromPrimitive(event.virtualMachine(),arg));
            }*/

            Value returnValue = classType.invokeMethod(event.thread(),classMethod,values,ClassType.INVOKE_SINGLE_THREADED);
            return JDIExpressionUtil.getObjFromRefrence(returnValue);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public String fun(BreakpointEvent event,String a,String b){
        return a+b;
    }

}
