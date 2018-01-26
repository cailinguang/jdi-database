package com.primeton.expression.parser;


import com.primeton.expression.reader.*;
import com.primeton.expression.reader.jdi.JDIFieldExpressionReader;

/**
 * Created by clg on 2018/1/24.
 */
public class ExpressionReaderFactory {
    public static ExpressionReader createExpressionReader(ExpressionString reader){
        try {
            //skip left blank
            reader.skipChars(StringExpressionReader.BLANK.toCharArray());

            reader.mark(0);
            char c = (char)reader.read();
            reader.reset();

            String pointStr = reader.readStrUntil('.');

            //高优先级在上面
            //var 定义属性
            if(reader.readStrUntil(StringExpressionReader.BLANK).equals(DefExpressionReader.START_MARK)){
                return DefExpressionReader.class.newInstance().setExpressionReader(reader);
            }
            //" 字符串
            else if(c == StringExpressionReader.START_MARK){
                return StringExpressionReader.class.newInstance().setExpressionReader(reader);
            }
            //[\w\.]+\(([\w,]+)?\) 方法调用
            else if(reader.readStrUntil(')').matches("^[\\w\\.]+\\(([\\w,]+)?$") ){
                return MethodExpressionReader.class.newInstance().setExpressionReader(reader);
            }
            //[\w@]+\(([\w,]+)?\) 方法调用 jdi
            else if(reader.readStrUntil(')').matches("^[\\w@]+\\(([\\w,]+)?$") ){
                return MethodExpressionReader.class.newInstance().setExpressionReader(reader);
            }
            //[\w\.]+ 属性调用
            else if(pointStr!=null && pointStr.matches("^[\\w\\.]+$")){
                return FieldExpressionReader.class.newInstance().setExpressionReader(reader);
            }
            //[\w@]+ 属性调用 jdi
            else if(pointStr!=null && pointStr.matches("^[\\w@]+$")){
                return JDIFieldExpressionReader.class.newInstance().setExpressionReader(reader);
            }
            //默认返回引用
            else{
                return RefExpressionReader.class.newInstance().setExpressionReader(reader);
            }
        } catch (Exception e) {
            throw new RuntimeException("reader factory error:",e);
        }
    }
}
