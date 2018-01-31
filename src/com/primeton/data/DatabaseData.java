package com.primeton.data;

import java.util.List;
import java.util.Vector;

/**
 * Created by clg on 2018/1/31.
 */
public class DatabaseData {
    private List<String> columns = new Vector();

    private List<String> originalData = new Vector();

    private List<String> data = new Vector();

    private String sql;

    private String type;

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<String> getOriginalData() {
        return originalData;
    }

    public void setOriginalData(List<String> originalData) {
        this.originalData = originalData;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
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
        this.sql = sql;
    }
}
