package com.primeton.expression.reader;

import com.primeton.expression.node.EqualsNode;
import com.primeton.expression.node.Node;
import com.primeton.expression.parser.ExpressionReaderFactory;
import com.primeton.expression.parser.ExpressionString;

import java.io.IOException;

/**
 * Created by clg on 2018/1/28.
 */
public class EqualsExpressionReader extends ExpressionReader {

    private static final char SPLIT = '=';

    @Override
    public Node read() {
        try {
            String left = expressionString.readStrUntil(SPLIT);
            expressionString.skipTo(left+SPLIT+SPLIT);

            String right = expressionString.readToEnd();

            Node leftNode = ExpressionReaderFactory.createExpressionReader(new ExpressionString(left)).read();
            Node rightNode = ExpressionReaderFactory.createExpressionReader(new ExpressionString(right)).read();

            return new EqualsNode(leftNode,rightNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
