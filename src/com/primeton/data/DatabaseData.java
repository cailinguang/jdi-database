package com.primeton.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by clg on 2018/1/31.
 */
public class DatabaseData {
    private List<DatabaseRow> originalData = Collections.EMPTY_LIST;

    private List<DatabaseRow> data = Collections.EMPTY_LIST;

    private Map<String,String> tableMetaData;

    private String sql;

    private String psSql;

    private Object[] sqlParams;

    private char type;

    private String tableName;


    public List<DatabaseRow> getOriginalData() {
        return originalData;
    }

    public void setOriginalData(List<DatabaseRow> originalData) {
        this.originalData = originalData;
    }

    public List<DatabaseRow> getData() {
        return data;
    }

    public void setData(List<DatabaseRow> data) {
        this.data = data;
    }

    public Map<String, String> getTableMetaData() {
        return tableMetaData;
    }

    public void setTableMetaData(Map<String, String> tableMetaData) {
        this.tableMetaData = tableMetaData;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setParam(int index, Object obj){
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
        if(obj instanceof BigDecimal){
            return ((BigDecimal)obj).toPlainString();
        }
        return "'"+obj.toString()+"'";
    }

    public ObservableList getTableViewData(){
        ObservableList datas = FXCollections.observableArrayList();
        getTableMetaData().forEach((mc,mr)->{
            int oldSize = getOriginalData().size();
            int newSize = getData().size();

            switch (getType()){
                case 'C':{
                    String[] data = new String[2+newSize];
                    data[0] = mc+"("+mr+")";//column
                    int temp = 1;//newValue ...
                    for(DatabaseRow r:getData()){
                        data[temp++] = r.getValueByColumn(mc);
                    }
                    datas.add(data);
                    break;
                }
                case 'U':{
                    String[] data = new String[2+newSize*2];
                    data[0] = mc+"("+mr+")";//column
                    int tempOld = 1;//oldValue
                    int tempNew = 2;//newValue
                    for(DatabaseRow r:getOriginalData()){
                        data[tempOld] = r.getValueByColumn(mc);
                        tempOld+=2;
                    }
                    for(DatabaseRow r:getData()){
                        data[tempNew] = r.getValueByColumn(mc);
                        tempNew+=2;
                    }
                    datas.add(data);
                    break;
                }
                case 'D':{
                    String[] data = new String[2+newSize];
                    data[0] = mc+"("+mr+")";//column
                    int temp = 1;//oldValue ...
                    for(DatabaseRow r:getOriginalData()){
                        data[temp++] = r.getValueByColumn(mc);
                    }
                    datas.add(data);
                    break;
                }

            }
        });
        return datas;
    }


    public String getDesc(){
        String desc = "";
        desc += tableName;

        switch (type) {
            case 'C': desc += "新增"+data.size()+"记录";break;
            case 'U': desc += "更新"+originalData.size()+"记录";break;
            case 'R': desc += "查询"+data.size()+"记录";break;
            case 'D': desc += "删除"+originalData.size()+"记录";break;
        }
        return desc;
    }
}
