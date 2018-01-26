package com.primeton.expression.node;

import com.primeton.expression.ExpressionContext;

/**
 * Created by clg on 2018/1/24.
 */
public interface Node {
    public Object express(ExpressionContext context);
}
