package com.primeton.expression.reader;

import com.primeton.expression.node.DefNode;
import com.primeton.expression.parser.ExpressionReaderFactory;
import com.primeton.expression.node.Node;

import java.io.IOException;

/**
 * Created by clg on 2018/1/25.
 */
public class DefExpressionReader extends ExpressionReader{
    public static final String START_MARK = "var";


    @Override
    public Node read() {
        String varName = "";
        Node left = null;
        try {
            expressionString.skipTo(START_MARK);
            //read varName
            while(true){
                try {
                    int i = expressionString.read();
                    if(i==-1) break;
                    char c = (char)i;
                    if(c!='='){
                        varName+=c;
                    }else{
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //read left
            ExpressionReader leftReader = ExpressionReaderFactory.createExpressionReader(expressionString);
            left = leftReader.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new DefNode(varName.trim(),left);
    }
}
