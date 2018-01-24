package com.primeton.expression.node;

import com.primeton.expression.operate.Operator;

/**
 * Created by clg on 2018/1/24.
 */
public abstract class OperateNode implements Node{

    private Operator operator;

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }


    public abstract Node operate(Node[] args);

    public abstract Node[] getOperateArgs();

    @Override
    public Object express() {
        Node[] args = getOperateArgs();
        return operate(args);
    }
}
