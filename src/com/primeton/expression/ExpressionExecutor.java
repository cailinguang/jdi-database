package com.primeton.expression;


import com.primeton.expression.node.ExpressionParser;
import com.primeton.expression.node.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by clg on 2018/1/24.
 */
public class ExpressionExecutor {
    private ExpressionContext ctx = null;

    public ExpressionExecutor(ExpressionContext ctx) {
        this.ctx = ctx;
    }



    public static Object execute(String expression,Map<String,Object> context){
        ExpressionContext ctx = null;
        if(context==null){
            ctx = new ExpressionContext();
        }else{
            ctx = new ExpressionContext(context);
        }
        ExpressionExecutor executor = new ExpressionExecutor(ctx);

        ExpressionParser parser = new ExpressionParser();
        List<Node> nodes = parser.analyze(expression);
        return null;
    }

    public static void main(String[] args){
        String expression = "var hashCode = test.hashCode();";
        Map<String, Object> map = new HashMap();
        map.put("test",new Integer(1));
        execute(expression,map);
    }
}
