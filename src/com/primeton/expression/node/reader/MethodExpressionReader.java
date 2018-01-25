package com.primeton.expression.node.reader;

import com.primeton.expression.node.ExpressionStringReader;
import com.primeton.expression.node.Node;

import java.io.StringReader;

/**
 * Created by clg on 2018/1/24.
 */
public class MethodExpressionReader implements ExpressionReader{

    public static final char START_MARK = '.';//字符窜开始标志
    public static final char MIDDLE_MARK = '(';
    public static final char END_MARK = ')';//字符窜结束标志

    private Node objectNode;
    private String methodName;
    private Node[] args;

    @Override
    public Node read(ExpressionStringReader stringReader) {
        return null;
    }
}
