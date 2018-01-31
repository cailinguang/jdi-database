package com.primeton.data;

import java.util.List;
import java.util.Vector;

/**
 * Created by clg on 2017/12/26.
 */
public class SessionData {
    private String sessionId;
    private String sessionUserName;

    private final List<ThreadData> threadDatas = new Vector();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionData that = (SessionData) o;

        if (!sessionId.equals(that.sessionId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return sessionId.hashCode();
    }

    public SessionData(String sessionId) {
        this.sessionId = sessionId;
    }

    public SessionData(String sessionId, String sessionUserName) {
        this.sessionId = sessionId;
        this.sessionUserName = sessionUserName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionUserName() {
        return sessionUserName;
    }

    public void setSessionUserName(String sessionUserName) {
        this.sessionUserName = sessionUserName;
    }

    public List<ThreadData> getThreadDatas() {
        return threadDatas;
    }

    public ThreadData getThreadDataByThreadId(String threadId){
        int index = threadDatas.indexOf(new ThreadData(threadId));
        return index!=-1 ? threadDatas.get(index) : null;
    }

    public void addThreadDataByThread(ThreadData threadData){
        threadDatas.add(threadData);
    }

}
