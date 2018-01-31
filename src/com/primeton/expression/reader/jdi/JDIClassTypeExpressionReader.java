package com.primeton.expression.reader.jdi;

import com.primeton.expression.node.Node;
import com.primeton.expression.node.StringNode;
import com.primeton.expression.node.jdi.JDIClassTypeNode;
import com.primeton.expression.node.jdi.JDIFieldNode;
import com.primeton.expression.parser.ExpressionReaderFactory;
import com.primeton.expression.parser.ExpressionString;
import com.primeton.expression.reader.ExpressionReader;

import java.io.IOException;

/**
 * Created by clg on 2018/1/25.
 */
public class JDIClassTypeExpressionReader extends ExpressionReader{
    public static final char SPLIT = '@';


    @Override
    public Node read() {
        try {

            String classType = expressionString.readToEnd();
            expressionString.skipTo(classType);

            classType = classType.substring(1,classType.length()-1);
            return new JDIClassTypeNode(classType);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
