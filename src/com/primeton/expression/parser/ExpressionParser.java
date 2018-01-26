package com.primeton.expression.parser;

import com.primeton.expression.node.Node;
import com.primeton.expression.reader.ExpressionReader;

import java.util.*;

/**
 * Created by clg on 2018/1/24.
 */
public class ExpressionParser {

    private Stack<String> parenthesis = new Stack();

    public List<Node> analyze(String expression) {
        List<Node> nodes = new ArrayList();
        String[] expressions =  expression.split("[\\r\\n\\t;]");
        try {
            for(String exp:expressions){
                ExpressionString stringReader = new ExpressionString(exp);
                while (true) {
                    stringReader.mark(0);
                    int i = stringReader.read();
                    if(i==-1) break;
                    char c = (char)i;
                    if(c==-1){
                        break;
                    }
                    if(ExpressionReader.SPLIT.indexOf(c)<0){
                        stringReader.reset();
                        ExpressionReader reader = ExpressionReaderFactory.createExpressionReader(stringReader);
                        Node node = reader.read();
                        nodes.add(node);
                    }
                }
            }
        }catch (Exception e){
            System.out.println("expression parser error:"+e.getMessage());
            e.printStackTrace();
        }
        return nodes;
    }
}
