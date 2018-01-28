package com.primeton.expression.reader;

import com.primeton.expression.node.MethodNode;
import com.primeton.expression.node.Node;
import com.primeton.expression.node.TernaryNode;
import com.primeton.expression.parser.ExpressionReaderFactory;
import com.primeton.expression.parser.ExpressionString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by clg on 2018/1/24.
 */
public class TernaryExpressionReader extends ExpressionReader{

    public static final char START_MARK = '?';
    public static final char MIDDLE_MARK = ':';


    @Override
    public Node read() {
        String condition = "";
        String one = "";
        String two = "";
        try {
            //read obj
            condition = expressionString.readStrUntil(START_MARK);
            expressionString.skipTo(condition+START_MARK);

            one = expressionString.readStrUntil(MIDDLE_MARK);
            expressionString.skipTo(one+MIDDLE_MARK);

            two = expressionString.readToEnd();
            expressionString.skipTo(two);

            Node cNode = ExpressionReaderFactory.createExpressionReader(new ExpressionString(condition.trim())).read();
            Node oNode = ExpressionReaderFactory.createExpressionReader(new ExpressionString(one.trim())).read();
            Node tNode = ExpressionReaderFactory.createExpressionReader(new ExpressionString(two.trim())).read();

            return new TernaryNode(cNode,oNode,tNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
