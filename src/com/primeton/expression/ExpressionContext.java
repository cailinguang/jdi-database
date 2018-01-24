package com.primeton.expression;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by clg on 2018/1/24.
 */
public class ExpressionContext {
    private final Map<String,Object> context = new HashMap();

    public void putObject(String key,Object value){
        context.put(key,value);
    }

    public Object getObject(String key){
        return context.get(key);
    }

}
