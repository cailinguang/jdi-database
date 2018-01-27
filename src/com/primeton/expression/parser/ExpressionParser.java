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
        String[] expressions =  expression.split(";");
        try {
            for(String exp:expressions){
                ExpressionString stringReader = new ExpressionString(exp);

                ExpressionReader reader = ExpressionReaderFactory.createExpressionReader(stringReader);
                Node node = reader.read();
                nodes.add(node);
            }
        }catch (Exception e){
            System.out.println("expression parser error:"+e.getMessage());
            e.printStackTrace();
        }
        return nodes;
    }
}
