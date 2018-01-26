package com.primeton.expression.reader;

import com.primeton.expression.node.FieldNode;
import com.primeton.expression.node.Node;
import com.primeton.expression.node.StringNode;
import com.primeton.expression.parser.ExpressionReaderFactory;
import com.primeton.expression.parser.ExpressionString;

import java.io.IOException;

/**
 * Created by clg on 2018/1/25.
 */
public class FieldExpressionReader extends ExpressionReader{
    public static final char SPLIT = '.';


    @Override
    public Node read() {
        try {
            String objName = expressionString.readStrUntil(SPLIT);
            expressionString.skipTo(objName+SPLIT);

            Node objNode = ExpressionReaderFactory.createExpressionReader(new ExpressionString(objName)).read();

            //read end match word
            String fieldName = expressionString.readToEnd();
            if(fieldName.matches("[\\w]+")){
                expressionString.skipTo(fieldName);
                return new FieldNode(objNode,new StringNode(fieldName));
            }else{
                Node leftObjNode = ExpressionReaderFactory.createExpressionReader(new ExpressionString(fieldName)).read();
                return new FieldNode(objNode,leftObjNode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
