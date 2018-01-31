package com.primeton.data;

import java.util.List;
import java.util.Vector;

/**
 * Created by clg on 2017/12/26.
 */
public class ThreadData {
    private String threadId;

    private final List<DatabaseData> databaseDatas = new Vector();

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

    public List<DatabaseData> getDatabaseDatas() {
        return databaseDatas;
    }


    public void addDatabaseData(DatabaseData databaseData){
        databaseDatas.add(databaseData);
    }
}
