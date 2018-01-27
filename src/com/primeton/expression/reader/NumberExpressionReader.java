package com.primeton.expression.reader;

import com.primeton.expression.node.Node;
import com.primeton.expression.node.NumberNode;
import com.primeton.expression.node.StringNode;

/**
 * Created by clg on 2018/1/24.
 */
public class NumberExpressionReader extends ExpressionReader{

    @Override
    public Node read() {
        try{
            String number = expressionString.readLastStrUntil(NUMBER);
            expressionString.skipTo(number);

            Number n = null;
            if(number.indexOf('.')!=-1){
                n = Double.parseDouble(number);
            }else{
                n = Integer.parseInt(number);
            }
            return new NumberNode(n);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
