package com.primeton.expression.node.reader;

import com.primeton.expression.node.ExpressionStringReader;
import com.primeton.expression.node.Node;

import java.io.StringReader;

/**
 * Created by clg on 2018/1/24.
 */
public interface ExpressionReader {
    public static final String WORD = "abcdefghigklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ";
    public static final String NUMBER = "1234567890";
    public static final String BLANK = " \r\n\t;";
    public static final String OP_BLANK = ".()";
    public static final String SPLIT = OP_BLANK + BLANK;

    public Node read(ExpressionStringReader stringReader);
}
