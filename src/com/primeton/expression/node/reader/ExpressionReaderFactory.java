package com.primeton.expression.node.reader;


import java.io.IOException;
import java.io.StringReader;

/**
 * Created by clg on 2018/1/24.
 */
public class ExpressionReaderFactory {
    public static ExpressionReader createExpressionReader(StringReader stringReader){
        try {
            stringReader.mark(0);
            char c = (char)stringReader.read();
            char cnext = (char)stringReader.read();
            //高优先级在上面
            //"字符串
            if(c == StringExpressionReader.START_MARK){
                return StringExpressionReader.class.newInstance();
            }
            //方法
            else if(c== MethodExpressionReader.START_MARK ){
                return MethodExpressionReader.class.newInstance();
            }
            //默认返回引用
            else{
                return RefExpressionReader.class.newInstance();
            }
        } catch (Exception e) {
            throw new RuntimeException("reader factory error:",e);
        }
    }
}
