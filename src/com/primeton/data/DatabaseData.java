package com.primeton.data;

import com.primeton.monitor.sql.SqlConvert;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by clg on 2018/1/31.
 */
public class DatabaseData {
    private List<String[]> originalData = null;

    private List<String[]> data = null;

    private Map<String,String> tableMetaData;

    private String sql;

    private String psSql;

    private Object[] sqlParams;

    private String type;


    public List<String[]> getOriginalData() {
        return originalData;
    }

    public void setOriginalData(List<String[]> originalData) {
        this.originalData = originalData;
    }

    public List<String[]> getData() {
        return data;
    }

    public void setData(List<String[]> data) {
        this.data = data;
    }

    public Map<String, String> getTableMetaData() {
        return tableMetaData;
    }

    public void setTableMetaData(Map<String, String> tableMetaData) {
        this.tableMetaData = tableMetaData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        StringBuilder sb = new StringBuilder();
        char[] chars = sql.replaceAll("\\s+"," ").toCharArray();
        int index = 1;
        for(char c:chars){
            if(c=='?'){
                sb.append("["+index+++"]");
            }else{
                sb.append(c);
            }
        }

        this.sqlParams = new Object[index-1];

        this.sql = sb.toString();
    }

    public String getPsSql() {
        return psSql;
    }

    public void setPsSql(String psSql) {
        this.psSql = psSql;
    }


    public void setParam(int index,Object obj){
        int i = this.sql.indexOf("[" + index + "]");
        if (i != -1) {
            this.sql = this.sql.substring(0, i) + getParam(obj) + this.sql.substring(i + (3 + index/10));
            this.sqlParams[index-1] = obj;
        }
    }

    private String getParam(Object obj){
        if(obj==null){
            return " null ";
        }
        if(obj instanceof Date){
            return "to_timestamp('" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format((Date)obj)+"','yyyy-MM-dd HH24:mi:ss ff')";
        }
        return "'"+obj.toString()+"'";
    }

}
