package com.primeton.expression.node;


import com.primeton.expression.node.ExpressionStringReader;
import com.primeton.expression.node.reader.*;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by clg on 2018/1/24.
 */
public class ExpressionReaderFactory {
    public static ExpressionReader createExpressionReader(ExpressionStringReader reader){
        try {
            reader.mark(0);
            char c = (char)reader.read();
            char cnext = (char)reader.read();
            reader.reset();
            //高优先级在上面
            if(reader.readStrUntil(StringExpressionReader.BLANK).matches(DefExpressionReader.START_MARK)){
                return DefExpressionReader.class.newInstance();
            }
            else if(c == StringExpressionReader.START_MARK){
                return StringExpressionReader.class.newInstance();
            }
            //方法
            else if(ExpressionReader.WORD.indexOf(c)>-1 && reader.readStrUntil(')').matches("^[\\w\\.]+\\(([\\w,]+)?\\)") ){
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
