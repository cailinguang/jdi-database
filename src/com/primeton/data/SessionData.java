package com.primeton.data;

import java.util.List;
import java.util.Vector;

/**
 * Created by clg on 2017/12/26.
 */
public class SessionData {
    private String sessionId;
    private String sessionUserName;

    private List<ThreadData> threadDatas = new Vector();

    public SessionData() {

    }

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

    public SessionData(String sessionId, String sessionUserName, List<ThreadData> threadDatas) {
        this.sessionId = sessionId;
        this.sessionUserName = sessionUserName;
        this.threadDatas = threadDatas;
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

    public void setThreadDatas(List<ThreadData> threadDatas) {
        this.threadDatas = threadDatas;
    }
}
