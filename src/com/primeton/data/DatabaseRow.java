package com.primeton.data;

import java.util.Arrays;

/**
 * Created by clg on 2018/2/7.
 */
public class DatabaseRow {
    private String[] columns;//
    private String[] values;

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public String getValueByColumn(String column){
        for(int i=0;i<columns.length;i++){
            if(column.equals(columns[i])){
                return values[i];
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "DatabaseRow{" +
                "columns=" + Arrays.toString(columns) +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
