package com.primeton.expression.node.reader;

import com.primeton.expression.node.ExpressionReaderFactory;
import com.primeton.expression.node.ExpressionStringReader;
import com.primeton.expression.node.Node;
import com.primeton.expression.node.VariableNode;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by clg on 2018/1/24.
 */
public class RefExpressionReader implements ExpressionReader{
    @Override
    public Node read(ExpressionStringReader reader) {
        String varName = "";
        //read varName
        try {
            reader.skipTo(BLANK);
            while (true) {
                char c = (char) reader.read();
                if (c != '=') {
                    varName += c;
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new VariableNode(varName);
    }
}
