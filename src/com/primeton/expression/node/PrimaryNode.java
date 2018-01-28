package com.primeton.expression.node;

import com.primeton.expression.ExpressionContext;

/**
 * Created by clg on 2018/1/28.
 */
public class PrimaryNode implements Node{
    private String primaryValue;

    public PrimaryNode(String primaryValue) {
        this.primaryValue = primaryValue;
    }

    @Override
    public Object express(ExpressionContext context) {
        if(this.primaryValue==null){
            throw new IllegalArgumentException("primary value is null");
        }
        if("null".equals(primaryValue)){
            return null;
        }
        else if(primaryValue.matches("true|false")){
            return Boolean.parseBoolean(primaryValue);
        }
        else if(primaryValue.matches("^[\\d\\.]+$")){
            Number n = null;
            if(primaryValue.indexOf('.')!=-1){
                n = Double.parseDouble(primaryValue);
            }else{
                n = Integer.parseInt(primaryValue);
            }
            return n;
        }
        else{
            throw new RuntimeException("not support this primary value:"+this.primaryValue);
        }
    }
}
