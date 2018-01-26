package com.primeton.expression;


import com.primeton.expression.parser.ExpressionParser;
import com.primeton.expression.node.Node;

import java.math.BigDecimal;
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

    public Object eval(List<Node> nodes){
        Object obj = null;
        for(Node node:nodes){
            obj = node.express(ctx);
        }
        return obj;
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
        return executor.eval(nodes);
    }


    public static void main(String[] args){
        String expression = "var hashCode = test.divide(a);\n" +
                "var c = \"test\"";
        Map<String, Object> map = new HashMap();
        map.put("test",new BigDecimal("3.226677777777777777"));
        map.put("a",new BigDecimal("2"));



        Object a = execute(expression,map);
        System.out.println(a);
    }
}
