package com.primeton.test;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.pp.para.GFmtOpt;
import gudusoft.gsqlparser.pp.para.GFmtOptFactory;
import gudusoft.gsqlparser.pp.stmtformatter.FormatterFactory;
import org.junit.Test;

/**
 * Created by clg on 2018/2/2.
 */
public class SqlTest {

    @Test
    public void testSql(){
        TGSqlParser sqlParser = new TGSqlParser(EDbVendor.dbvoracle);
        sqlParser.sqltext = "insert into T_PPR_AUDITLOG ( F_ID, F_FORMATTED_MESSAGE, F_OPERATOR_ID, F_DEP_ID, F_OPERATION_TYPE, F_FUNCTION_NAME, F_BUSSINESS_CLASS, F_OLD_VALUE, F_NEW_VALUE, F_KEY_MAP, F_CREATE_TIMESTAMP, F_ARG02, F_ARG03, F_OPER_IMPORTANCE, F_OPER_RATE, F_SYS_ID ) values ( (select nvl(max(f_id),1)+1 from T_PPR_AUDITLOG), '修改交易员成功', '1', '0', 'MODIFY', '修改交易员', '交易员管理', '{\"交易员代码\":\"ad2_a1\",\"交易员名称\":\"32\",\"席位代码\":\"000111\",\"是否登录\":\"0\",\"状态\":\"0\",\"最大本地报单号\":null,\"前置机编号\":null,\"电话\":\"324324\",\"证件编号\":\"2332423423\",\"复核员\":null,\"操作员\":\"0001admin1\"}', '{\"交易员代码\":\"ad2_a1\",\"交易员名称\":\"32\",\"席位代码\":\"000111\",\"是否登录\":null,\"状态\":\"0\",\"最大本地报单号\":null,\"前置机编号\":null,\"电话\":\"324324\",\"证件编号\":\"2332423423\",\"复核员\":null,\"操作员\":null}', 'newAuditLog', 'java.sql.Timestamp (id=7941)', '01', '0001admin1', '3', '3', 'MSP' )";

        sqlParser.parse();

        GFmtOpt option = GFmtOptFactory.newInstance();
        String formatSQL = FormatterFactory.pp(sqlParser, option);
        System.out.println("格式化后的SQL：\n"+formatSQL);

        TCustomSqlStatement stmt = sqlParser.getSqlstatements().get(0);

        System.out.println(stmt.sqlstatementtype);

    }
}
