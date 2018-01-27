package com.primeton.expression.parser;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by clg on 2018/1/25.
 */
public class ExpressionString extends StringReader{
    /**
     * Creates a new string reader.
     *
     * @param s String providing the character stream.
     */
    public ExpressionString(String s) {
        super(s);
    }

    /**
     * 读取到s，包括s
     * 如果后面没有s，则返回""
     * 复位next
     * @param s
     * @return
     * @throws IOException
     */
    public String readStrUntil(String s) throws IOException {
        mark(0);
        StringBuffer sb = new StringBuffer();
        while(true){
            int i = read();
            if(i==-1) {
                reset();
                return "";
            }
            char c = (char)i;
            sb.append(c);
            if(s.indexOf(c)!=-1){
                break;
            }
        }
        reset();
        return sb.toString();
    }

    /**
     * 从后面开始读，读取到s，包括s
     * 复位next
     * @param s
     * @return
     * @throws IOException
     */
    public String readLastStrUntil(String s) throws IOException {
        mark(0);
        StringBuffer sb = new StringBuffer();
        int lastIndex = 0;
        int index = 0;
        while(true){
            int i = read();
            if(i==-1) break;
            index++;
            char c = (char)i;
            if(s.indexOf(c)!=-1){
                lastIndex=index;
            }
        }
        reset();
        index=1;
        while(lastIndex!=0){
            if(index++>lastIndex){
                break;
            }
            char c = (char)read();
            sb.append(c);
        }
        reset();

        return sb.toString();
    }

    /**
     * 读取到c，不包括c
     * 如果后面没有s，则返回""
     * 复位next
     * @param c
     * @return
     * @throws IOException
     */
    public String readStrUntil(char c) throws IOException {
        mark(0);
        StringBuffer sb = new StringBuffer();
        while(true){
            int i = read();
            if(i==-1) {
                reset();
                return "";
            }
            char c1 = (char)i;
            if(c==c1){
                break;
            }
            sb.append(c1);
        }
        reset();
        return sb.toString();
    }

    /**
     * 从后面开始读，读取到c,不包括c
     * 复位next
     * @param c
     * @return
     * @throws IOException
     */
    public String readLastStrUntil(char s) throws IOException {
        mark(0);
        StringBuffer sb = new StringBuffer();
        int lastIndex = 0;
        int index = 0;
        while(true){
            int i = read();
            if(i==-1) break;
            index++;
            char c = (char)i;
            if(s==c){
                lastIndex=index;
            }
        }
        reset();
        index=1;
        while(lastIndex!=0){
            if(++index>lastIndex){
                break;
            }
            char c = (char)read();
            sb.append(c);
        }
        reset();

        return sb.toString();
    }

    public void skipTo(String s) throws IOException{
        mark(0);
        boolean isMatch = false;
        String temp = "";
        while(true){
            int i = read();
            if(i==-1) break;
            char c = (char)i;
            temp = temp+c;
            if(temp.equals(s)){
                isMatch=true;
                break;
            }
        }
        if(!isMatch) reset();
    }

    /**
     * 跳过chars
     * @param chars
     * @throws IOException
     */
    public void skipChars(char... chars) throws IOException{
        while(true){
            mark(0);
            int i = read();
            if(i==-1) break;
            char c = (char)i;
            boolean contains = false;
            for(char sc:chars){
                if(sc==c){
                    contains = true;
                    break;
                }
            }
            if(!contains){
                reset();
                break;
            }
        }
    }

    /**
     * 读取到最后
     * reset
     * @return
     * @throws IOException
     */
    public String readToEnd() throws IOException{
        mark(0);
        StringBuffer sb = new StringBuffer();
        while (true){
            int i = read();
            if(i==-1) break;
            char c = (char)i;
            sb.append(c);
        }
        reset();
        return sb.toString();
    }

    public static void main(String[] args) throws Exception{
        ExpressionString stringReader = new ExpressionString("aadcf.bb.cc()");
        String s = stringReader.readLastStrUntil(".");
        System.out.println(s);
        stringReader.skipTo(s);
        System.out.println((char)stringReader.read());

    }
}
