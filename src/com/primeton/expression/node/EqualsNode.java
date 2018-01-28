package com.primeton.expression.node;

import com.primeton.expression.ExpressionContext;
import com.primeton.expression.node.Node;

/**
 * Created by clg on 2018/1/28.
 */
public class EqualsNode implements Node {

    private Node left;
    private Node right;

    public EqualsNode(Node left, Node right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Object express(ExpressionContext context) {
        if(left==null||right==null){
            throw new IllegalArgumentException("left or right is null,"+this);
        }

        Object leftObj = left.express(context);
        Object rightObj = right.express(context);

        if(leftObj instanceof String && rightObj instanceof String && leftObj!=null && rightObj!=null){
            return leftObj.equals(rightObj);
        }
        return leftObj == rightObj;
    }

    @Override
    public String toString() {
        return "EqualsNode{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }
}
