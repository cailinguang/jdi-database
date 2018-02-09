package com.primeton.monitor;

import com.primeton.data.ContextData;
import com.primeton.data.SessionData;
import com.primeton.expression.ExpressionContext;
import com.primeton.expression.ExpressionExecutor;
import com.sun.jdi.Location;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import org.jdiscript.JDIScript;

/**
 * Created by clg on 2018/1/23.
 */
public class SessionMonitor extends Monitor{
    public SessionMonitor(JDIScript j) {
        super(j);
    }



    private String sessionExpression;
    {
        sessionExpression  = "var session = request@getSession();";
        sessionExpression += "var sessionId = session@getId();";
        sessionExpression += "var user = session@getAttribute(\"SGE_USER\");";
        sessionExpression += "var loginName = user==null ? null : user@getLoginName();";
        sessionExpression += "var userName = user==null ? null : user@getRealName();";
    }


    @Override
    public void doMonitor() {
        Location preLocation = getClassMethodLocations("cn.com.sge.gems.base.web.filter.UserAuthorizationFilter","doFilter",3).get(0);

        j.breakpointRequest(preLocation, breakpoint -> {
            try {
                StackFrame stackFrame = breakpoint.thread().frame(0);
                ThreadReference thread = stackFrame.thread();

                System.out.println("session Monitor thread uid:" + thread.uniqueID());

                ExpressionContext context = new ExpressionContext();
                context.putObject("thread", thread);
                context.putObject("request", stackFrame.getArgumentValues().get(0));
                ExpressionExecutor.execute(sessionExpression, context);

                String sessionId = (String) context.getObject("sessionId");
                String userName = (String) context.getObject("userName");
                String loginName = (String) context.getObject("loginName");

                SessionData sessionData = ContextData.getSessionDataById(sessionId);
                if (sessionData == null) {
                    sessionData = new SessionData(sessionId);
                    sessionData.setSessionLoginName(loginName);
                    sessionData.setSessionUserName(userName);
                    ContextData.addSessionData(sessionData);
                    System.out.println("session monitor add sessionData userNaem:" + userName + ",sessionId:" + sessionId);
                }else{
                    sessionData.setSessionLoginName(loginName);
                    sessionData.setSessionUserName(userName);
                    ContextData.sessionDatas.set(ContextData.sessionDatas.indexOf(sessionData),sessionData);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }).enable();

    }

}
