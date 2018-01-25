package com.primeton.expression.node.reader;

import com.primeton.expression.node.DefNode;
import com.primeton.expression.node.ExpressionReaderFactory;
import com.primeton.expression.node.ExpressionStringReader;
import com.primeton.expression.node.Node;

import java.io.IOException;

/**
 * Created by clg on 2018/1/25.
 */
public class DefExpressionReader implements ExpressionReader{
    public static final String START_MARK = "var\\s";


    @Override
    public Node read(ExpressionStringReader reader) {
        String varName = "";
        Node left = null;
        try {
            reader.skipTo(START_MARK);
            //read varName
            while(true){
                try {
                    int i = reader.read();
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
            reader.skipTo(BLANK);
            ExpressionReader leftReader = ExpressionReaderFactory.createExpressionReader(reader);
            left = leftReader.read(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new DefNode(varName.trim(),left);
    }
}
