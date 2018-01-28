package com.primeton.expression.node;

import com.primeton.expression.ExpressionContext;

/**
 * Created by clg on 2018/1/28.
 */
public class TernaryNode implements Node{
    private Node condition;
    private Node one;
    private Node two;

    public TernaryNode(Node condition, Node one, Node two) {
        this.condition = condition;
        this.one = one;
        this.two = two;
    }

    @Override
    public Object express(ExpressionContext context) {
        if(condition==null || one ==null || two==null){
            throw new IllegalArgumentException("one member variable can't null,"+this);
        }

        Boolean c = (Boolean) condition.express(context);
        if(c == true){
            return one.express(context);
        }else{
            return two.express(context);
        }
    }

    @Override
    public String toString() {
        return "TernaryNode{" +
                "condition=" + condition +
                ", one=" + one +
                ", two=" + two +
                '}';
    }
}
