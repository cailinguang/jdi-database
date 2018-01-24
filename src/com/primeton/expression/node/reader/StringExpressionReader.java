package com.primeton.expression.node.reader;

import com.primeton.expression.node.Node;

import java.io.StringReader;

/**
 * Created by clg on 2018/1/24.
 */
public class StringExpressionReader implements ExpressionReader{
    public static final char START_MARK = '"';//字符窜开始标志
    public static final char END_MARK = '"';//字符窜结束标志

    @Override
    public Node read(StringReader stringReader) {
        return null;
    }
}
