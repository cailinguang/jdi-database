package com.primeton.monitor;


import com.sun.jdi.Location;
import com.sun.jdi.Method;
import com.sun.jdi.ReferenceType;
import org.jdiscript.JDIScript;

import java.util.List;

/**
 * Created by clg on 2017/12/26.
 */
public abstract class Monitor {
    protected JDIScript j = null;

    public Monitor(JDIScript j){
        this.j = j;
    }

    public abstract void doMonitor();

    protected List<Location> getClassMethodLocations(String className, String methodName, int methodArgLength){
        Location location = null;
        List<ReferenceType> classs = j.vm().classesByName(className);
        if(classs.size()==0){
            throw new RuntimeException(className+" not found");
        }
        List<Method> methods = classs.get(0).methodsByName(methodName);
        if(methods.size()==0){
            throw new RuntimeException(methodName+" not found from "+className);
        }
        try {
            for (Method m : methods) {
                if (m.argumentTypes().size() == methodArgLength) {
                    return m.allLineLocations();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("not found Location");
    }
}
