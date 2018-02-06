package com.primeton.monitor.sql;


import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.nodes.TExpression;
import gudusoft.gsqlparser.nodes.TMultiTarget;
import gudusoft.gsqlparser.nodes.TMultiTargetList;
import gudusoft.gsqlparser.nodes.TResultColumn;
import gudusoft.gsqlparser.stmt.TDeleteSqlStatement;
import gudusoft.gsqlparser.stmt.TInsertSqlStatement;
import gudusoft.gsqlparser.stmt.TUpdateSqlStatement;

/**
 * Created by clg on 2018/2/6.
 */
public class SqlConvert {
    private String sql;

    public SqlConvert(String sql){
        this.sql = sql;

        analyzeSql();
    }


    public char getCRUD(){
        switch (statement.sqlstatementtype){
            case sstinsert: return 'C';
            case sstselect: return 'R';
            case sstupdate: return 'U';
            case sstdelete: return 'D';
            default: throw new RuntimeException("not support this sql:"+this.sql);
        }
    }

    public String getTableName(){
        return statement.getTables().getTable(0).getFullName();
    }

    public String getBrforeSql(){
        StringBuffer sb = new StringBuffer();
        char type = getCRUD();
        if(type=='U'){
            TUpdateSqlStatement update = (TUpdateSqlStatement)statement;
            sb.append("select * from ");
            sb.append(update.getTables().getTable(0).getFullName());
            sb.append(" where 1=1 ");
            if(update.getWhereClause()!=null){
                sb.append(" and ");
                sb.append(update.getWhereClause().getCondition());
            }
            for(int i=0;i<update.getResultColumnList().size();i++){
                TResultColumn resultColumn = update.getResultColumnList().getResultColumn(i);
                TExpression expression = resultColumn.getExpr();
                sb.append(" and ");
                sb.append(expression.getLeftOperand().toString());
                sb.append("=");
                sb.append(expression.getRightOperand().toString());
            }
        }
        else if(type=='D'){
            TDeleteSqlStatement delete = (TDeleteSqlStatement)statement;
            sb.append("select * from ");
            sb.append(delete.getTables().getTable(0).getFullName());
            if(delete.getWhereClause()!=null){
                sb.append(" where ");
                sb.append(delete.getWhereClause().getCondition());
            }
        }

        return sb.toString();
    }

    public String getAfterSql(){
        StringBuffer sb = new StringBuffer();
        char type = getCRUD();
        if(type=='C'){
            TInsertSqlStatement insert = (TInsertSqlStatement)statement;
            sb.append("select * from ");
            sb.append(insert.getTables().getTable(0).getName());
            sb.append(" where 1=1 ");

            TMultiTargetList values = insert.getValues();
            TMultiTarget value = values.getMultiTarget(0);
            for(int i=0;i<insert.getColumnList().size();i++){
                sb.append(" and ");
                sb.append(insert.getColumnList().getObjectName(i).toString());
                sb.append("=");
                sb.append(value.getColumnList().getResultColumn(i).toString());
            }
        }
        else if(type=='U'){
            TUpdateSqlStatement update = (TUpdateSqlStatement)statement;
            sb.append("select * from ");
            sb.append(update.getTables().getTable(0).getFullName());
            sb.append(" where 1=1 ");
            if(update.getWhereClause()!=null){
                sb.append(" and ");
                sb.append(update.getWhereClause().getCondition());
            }
            for(int i=0;i<update.getResultColumnList().size();i++){
                TResultColumn resultColumn = update.getResultColumnList().getResultColumn(i);
                TExpression expression = resultColumn.getExpr();
                sb.append(" and ");
                sb.append(expression.getLeftOperand().toString());
                sb.append("=");
                sb.append(expression.getRightOperand().toString());
            }
        }

        return sb.toString();
    }


    private TCustomSqlStatement statement;
    private void analyzeSql(){
        if(this.sql == null){
            throw new RuntimeException("sql is null!");
        }
        TGSqlParser sqlparser = new TGSqlParser( EDbVendor.dbvoracle );
        sqlparser.sqltext = this.sql;
        sqlparser.parse();

        if(sqlparser.sqlstatements.size()!=1){
            throw new RuntimeException("not support more statement!!");
        }
        statement = sqlparser.sqlstatements.get(0);
    }


    public static void main(String[] args){
        SqlConvert convert1 = new SqlConvert("update abc set a1='1',a2='2' where a3='3'");
        System.out.println(convert1.getBrforeSql());

        SqlConvert convert2 = new SqlConvert("delete from abc where a1='1' and a2='3' or a3='3'");
        System.out.println(convert2.getBrforeSql());

        SqlConvert convert3 = new SqlConvert("insert into abc(a3,a2,a1) values('3','2','1')");
        System.out.println(convert3.getAfterSql());

        System.out.println(convert1.getAfterSql());
    }
}
