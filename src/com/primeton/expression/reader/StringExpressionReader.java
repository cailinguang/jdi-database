package com.primeton.expression.reader;

import com.primeton.expression.node.StringNode;
import com.primeton.expression.node.Node;

/**
 * Created by clg on 2018/1/24.
 */
public class StringExpressionReader extends ExpressionReader{
    public static final char START_MARK = '"';//字符窜开始标志
    public static final char END_MARK = '"';//字符窜结束标志

    @Override
    public Node read() {
        try{
            expressionString.skipChars(START_MARK);
            String str = expressionString.readStrUntil(END_MARK);
            expressionString.skipTo(str+END_MARK);
            return new StringNode(str);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
