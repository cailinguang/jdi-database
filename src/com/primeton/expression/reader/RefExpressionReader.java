package com.primeton.expression.reader;

import com.primeton.expression.node.Node;
import com.primeton.expression.node.VariableNode;

import java.io.IOException;

/**
 * Created by clg on 2018/1/24.
 */
public class RefExpressionReader extends ExpressionReader{
    private static final char SPLIT = '.';

    @Override
    public Node read() {
        String varName = "";
        //read varName
        try {
            while (true) {
                int i= expressionString.read();
                if(i==-1) break;
                char c = (char)i;
                varName+=c;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new VariableNode(varName);
    }
}
