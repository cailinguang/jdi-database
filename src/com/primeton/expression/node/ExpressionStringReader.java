package com.primeton.expression.node;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by clg on 2018/1/25.
 */
public class ExpressionStringReader extends StringReader{
    /**
     * Creates a new string reader.
     *
     * @param s String providing the character stream.
     */
    public ExpressionStringReader(String s) {
        super(s);
    }

    /**
     * 读取到s，包括s
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
            if(i==-1) break;
            char c = (char)i;
            sb.append(c);
            if(s.indexOf(c)!=-1){
                break;
            }
        }
        reset();
        return sb.toString();
    }

    public void skipTo(String s) throws IOException{
        while(true){
            mark(0);
            int i = read();
            if(i==-1) break;
            char c = (char)i;
            if(s.indexOf(c)==-1){
                reset();
                break;
            }
        }
    }

    public String readStrUntil(char c) throws IOException {
        return readStrUntil(c+"");
    }

    public static void main(String[] args) throws Exception{
        ExpressionStringReader stringReader = new ExpressionStringReader("dsafdsafbb33aa");
        char c1 = (char)stringReader.read();
        System.out.println(c1);
        String str1 = stringReader.readStrUntil("0123456789");
        System.out.println(str1);

        stringReader.mark(0);
        stringReader.reset();
        System.out.println((char)stringReader.read());
        System.out.println((char)stringReader.read());
    }
}
