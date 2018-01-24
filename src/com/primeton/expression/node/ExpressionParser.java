package com.primeton.expression.node;

import com.primeton.expression.node.reader.ExpressionReader;
import com.primeton.expression.node.reader.ExpressionReaderFactory;
import com.primeton.expression.operate.Operator;

import java.io.StringReader;
import java.util.*;

/**
 * Created by clg on 2018/1/24.
 */
public class ExpressionParser {
    private static Map<String, Operator> operators = new HashMap();

    private Stack<String> parenthesis = new Stack();

    public List<Node> analyze(String expression) {
        List<Node> nodes = new ArrayList();
        String[] expressions =  expression.split("[\\\\r\\\\n\\\\t]");
        try {
            for(String exp:expressions){
                StringReader stringReader = new StringReader(exp);
                while (true) {
                    char c = (char)stringReader.read();
                    if(c==-1){
                        break;
                    }
                    if(" .()".indexOf(c)>-1){
                        ExpressionReader reader = ExpressionReaderFactory.createExpressionReader(stringReader);
                        Node node = reader.read(stringReader);
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

    static {
        operators.put(Operator.NOT.getToken(), Operator.NOT);
        operators.put(Operator.MUTI.getToken(), Operator.MUTI);
        operators.put(Operator.DIV.getToken(), Operator.DIV);
        operators.put(Operator.MOD.getToken(), Operator.MOD);
        operators.put(Operator.PLUS.getToken(), Operator.PLUS);
        operators.put(Operator.MINUS.getToken(), Operator.MINUS);
        operators.put(Operator.LT.getToken(), Operator.LT);
        operators.put(Operator.LE.getToken(), Operator.LE);
        operators.put(Operator.GT.getToken(), Operator.GT);
        operators.put(Operator.GE.getToken(), Operator.GE);
        operators.put(Operator.EQ.getToken(), Operator.EQ);
        operators.put(Operator.NEQ.getToken(), Operator.NEQ);
        operators.put(Operator.AND.getToken(), Operator.AND);
        operators.put(Operator.OR.getToken(), Operator.OR);
        operators.put(Operator.APPEND.getToken(), Operator.APPEND);
        operators.put(Operator.SELECT.getToken(), Operator.SELECT);
        operators.put(Operator.QUES.getToken(), Operator.QUES);
        operators.put(Operator.COLON.getToken(), Operator.COLON);
    }
}
