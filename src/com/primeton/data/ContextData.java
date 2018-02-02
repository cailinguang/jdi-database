package com.primeton.data;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by clg on 2017/12/26.
 */
public class ContextData {
    /**
     * session data
     */
    public static final SimpleListProperty<SessionData> sessionDatas = new SimpleListProperty(FXCollections.observableArrayList());

    public static SessionData getSessionDataById(String sessionId){
        int index = sessionDatas.indexOf(new SessionData(sessionId));
        return index!=-1 ? sessionDatas.get(index) : null;
    }

    public static void addSessionData(SessionData sessionData){
        ContextData.sessionDatas.add(sessionData);
    }

    //表列备注信息缓存
    public static Map<String,Map<String,String>> tableMetaDataCache = new HashMap();
}
