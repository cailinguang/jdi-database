package com.primeton.expression.operate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by clg on 2018/1/24.
 */
public enum Operator {
    NOT("!", 80, 1),
    BNOT("~", 80, 1),
    NG("-", 80, 1),
    MUTI("*", 70, 2),
    DIV("/", 70, 2),
    MOD("%", 70, 2),
    PLUS("+", 60, 2),
    MINUS("-", 60, 2),
    BAND("&", 55, 2),
    BOR("|", 55, 2),
    LT("<", 50, 2),
    LE("<=", 50, 2),
    GT(">", 50, 2),
    GE(">=", 50, 2),
    EQ("==", 40, 2),
    NEQ("!=", 40, 2),
    AND("&&", 30, 2),
    OR("||", 20, 2),
    APPEND("#", 10, 2),
    QUES("?", 0, 0),
    COLON(":", 0, 0),
    SELECT("?:", 0, 3);

    private static final Set<String> OP_RESERVE_WORD = new HashSet();
    private String token;
    private int priority;
    private int opType;

    public static boolean isLegalOperatorToken(String tokenText) {
        return OP_RESERVE_WORD.contains(tokenText);
    }

    private Operator(String token, int priority, int opType) {
        this.token = token;
        this.priority = priority;
        this.opType = opType;
    }

    public String getToken() {
        return this.token;
    }

    public int getPiority() {
        return this.priority;
    }

    public int getOpType() {
        return this.opType;
    }

    static {
        OP_RESERVE_WORD.add(NOT.getToken());
        OP_RESERVE_WORD.add(NG.getToken());
        OP_RESERVE_WORD.add(MUTI.getToken());
        OP_RESERVE_WORD.add(DIV.getToken());
        OP_RESERVE_WORD.add(MOD.getToken());
        OP_RESERVE_WORD.add(PLUS.getToken());
        OP_RESERVE_WORD.add(MINUS.getToken());
        OP_RESERVE_WORD.add(LT.getToken());
        OP_RESERVE_WORD.add(LE.getToken());
        OP_RESERVE_WORD.add(GT.getToken());
        OP_RESERVE_WORD.add(GE.getToken());
        OP_RESERVE_WORD.add(EQ.getToken());
        OP_RESERVE_WORD.add(NEQ.getToken());
        OP_RESERVE_WORD.add(AND.getToken());
        OP_RESERVE_WORD.add(OR.getToken());
        OP_RESERVE_WORD.add(APPEND.getToken());
        OP_RESERVE_WORD.add(SELECT.getToken());
        OP_RESERVE_WORD.add(QUES.getToken());
        OP_RESERVE_WORD.add(COLON.getToken());
    }
}
