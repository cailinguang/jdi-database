package com.primeton.expression.node.reader;

import com.primeton.expression.node.Node;

import java.io.StringReader;

/**
 * Created by clg on 2018/1/24.
 */
public interface ExpressionReader {
    public Node read(StringReader stringReader);
}
