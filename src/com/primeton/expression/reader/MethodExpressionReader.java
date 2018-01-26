package com.primeton.expression.reader;

import com.primeton.expression.node.MethodNode;
import com.primeton.expression.parser.ExpressionReaderFactory;
import com.primeton.expression.parser.ExpressionString;
import com.primeton.expression.node.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by clg on 2018/1/24.
 */
public class MethodExpressionReader extends ExpressionReader{

    public static final char START_MARK = '.';//字符窜开始标志
    public static final char MIDDLE_MARK = '(';
    public static final char END_MARK = ')';//字符窜结束标志
    public static final char ARGS_APLIT = ',';


    @Override
    public Node read() {
        String objectName = "";
        String methodName = "";
        Node[] args = new Node[0];

        try {
            //read obj
            objectName = expressionString.readLastStrUntil(START_MARK);
            expressionString.skipTo(objectName+START_MARK);

            //read method
            methodName = expressionString.readStrUntil(MIDDLE_MARK);
            expressionString.skipTo(methodName+MIDDLE_MARK);

            //read args
            List<String> argStrs = new ArrayList();
            String argStr = "";
            while(true){
                int i = expressionString.read();
                if(i==-1){
                    argStrs.add(argStr);
                    break;
                }
                char c = (char)i;
                if(c==END_MARK){
                    argStrs.add(argStr);
                    break;
                }
                if(i==ARGS_APLIT){
                    argStrs.add(argStr);
                    argStr="";
                    continue;
                }
                argStr+=c;
            }

            ExpressionString objExpresionStr = new ExpressionString(objectName);
            Node objNode = ExpressionReaderFactory.createExpressionReader(objExpresionStr).read();

            args = new Node[argStrs.size()];
            int index = 0;
            for(String arg:argStrs){
                ExpressionString ar = new ExpressionString(arg);
                ExpressionReader argReader = ExpressionReaderFactory.createExpressionReader(ar);
                Node argNode = argReader.read();
                args[index++]=argNode;
            }

            return new MethodNode(objNode,methodName,args);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
