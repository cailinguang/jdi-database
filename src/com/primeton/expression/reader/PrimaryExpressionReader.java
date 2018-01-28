package com.primeton.expression.reader;

import com.primeton.expression.node.Node;
import com.primeton.expression.node.PrimaryNode;

import java.io.IOException;

/**
 * Created by clg on 2018/1/28.
 */
public class PrimaryExpressionReader extends ExpressionReader{

    public static final String MATCH = "^(null)|(true)|(false)|([\\d\\.]+)";

    @Override
    public Node read() {
        try {
            String primaryValue = expressionString.readToEnd();
            return new PrimaryNode(primaryValue);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
