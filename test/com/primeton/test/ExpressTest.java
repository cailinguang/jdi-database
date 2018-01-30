package com.primeton.test;

import com.primeton.expression.ExpressionContext;
import com.primeton.expression.ExpressionExecutor;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by clg on 2018/1/28.
 */
public class ExpressTest {

    @Test
    public void testExp(){
        ExpressionContext context = new ExpressionContext();
        String expression = "\"对对对发3\"";
        Object obj = ExpressionExecutor.execute(expression,context);
        Assert.assertEquals("对对对发3",obj.toString());
    }

    @Test
    public void testDefExpression1(){
        ExpressionContext context = new ExpressionContext();

        String expression = "var str = \"对对对发3\"";
        Object str = ExpressionExecutor.execute(expression,context);
        Assert.assertEquals(str,"对对对发3");
    }

    @Test
    public void testDefExpression2(){
        ExpressionContext context = new ExpressionContext();
        context.putObject("str1","hello word");

        String expression = "var str = str1";
        Object str = ExpressionExecutor.execute(expression,context);
        Assert.assertEquals(str,"hello word");
    }

    @Test
    public void testFieldExpression(){
        ExpressionContext context = new ExpressionContext();
        context.putObject("str1","hello");

        String expression = "var chars = str1.value";
        Object object = ExpressionExecutor.execute(expression,context);

        Object chars = context.getObject("chars");

        Assert.assertEquals(object,chars);

        Assert.assertArrayEquals((char[])object,new char[]{'h','e','l','l','o'});
    }

    @Test
    public void testMethodExpression(){
        ExpressionContext context = new ExpressionContext();
        context.putObject("str1","hello");

        String expression = "var chars = str1.length()";
        Object object = ExpressionExecutor.execute(expression,context);

        Assert.assertEquals(5,object);
    }

    @Test
    public void testNumberExpression(){
        ExpressionContext context = new ExpressionContext();

        String expression = "var n = 345";
        Object object = ExpressionExecutor.execute(expression,context);

        Assert.assertEquals(345,object);
    }

    @Test
    public void testStrExpression(){
        ExpressionContext context = new ExpressionContext();

        String expression = "var n = \"hello\".length()";
        Object object = ExpressionExecutor.execute(expression,context);

        Assert.assertEquals(5,object);
    }


    @Test
    public void testTernaryExpression(){
        ExpressionContext context = new ExpressionContext();

        String expression = "var n = true? 1 : 3";
        Object object = ExpressionExecutor.execute(expression,context);

        Assert.assertEquals(1,object);
    }

    @Test
    public void testEqualsExpression(){
        ExpressionContext context = new ExpressionContext();

        String expression = "var n = 1==1 ? 77 : 88";
        Object object = ExpressionExecutor.execute(expression,context);

        Assert.assertEquals(77,object);
    }

    @Test
    public void testMultiExpression(){
        ExpressionContext context = new ExpressionContext();

        String expression   = "var test1 = true? 1 : 3;";
        expression         += "var test2 = test1.byteValue();";

        Object object = ExpressionExecutor.execute(expression,context);
        Assert.assertEquals(object,context.getObject("test2"));
        Assert.assertEquals((byte)1,object);
    }
}
