package com.primeton.data;

import java.util.List;
import java.util.Vector;

/**
 * Created by clg on 2017/12/26.
 */
public class ThreadData {
    private String threadId;

    private List<String> columns = new Vector();

    private List<String> originalData = new Vector();

    private List<String> data = new Vector();

    public ThreadData(String threadId) {
        this.threadId = threadId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ThreadData that = (ThreadData) o;

        if (threadId != null ? !threadId.equals(that.threadId) : that.threadId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return threadId != null ? threadId.hashCode() : 0;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

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
}
