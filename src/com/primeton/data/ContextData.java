package com.primeton.data;

import java.util.Vector;

/**
 * Created by clg on 2017/12/26.
 */
public class ContextData {
    /**
     * session data
     */
    public static final Vector<SessionData> sessionDatas = new Vector();

    public static SessionData getSessionDataById(String sessionId){
        int index = sessionDatas.indexOf(new SessionData(sessionId));
        return index!=-1 ? sessionDatas.get(index) : null;
    }

    public static void addSessionData(SessionData sessionData){
        ContextData.sessionDatas.add(sessionData);
    }

}
