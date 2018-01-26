package com.primeton.expression.reader;

import com.primeton.expression.node.Node;
import com.primeton.expression.parser.ExpressionString;

/**
 * Created by clg on 2018/1/24.
 */
public abstract class ExpressionReader {
    public static final String WORD = "abcdefghigklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ";
    public static final String NUMBER = "1234567890";
    public static final String BLANK = " \r\n\t;";
    public static final String OP_BLANK = ".()";
    public static final String SPLIT = OP_BLANK + BLANK;
    public static final char END = '\0';

    protected ExpressionString expressionString;

    public ExpressionReader setExpressionReader(ExpressionString reader){
        this.expressionString = reader;
        return this;
    }

    public abstract Node read();
}
