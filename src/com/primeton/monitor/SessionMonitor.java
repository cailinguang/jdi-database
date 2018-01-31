package com.primeton.monitor;

import com.primeton.data.ContextData;
import com.primeton.data.SessionData;
import com.primeton.expression.ExpressionContext;
import com.primeton.expression.ExpressionExecutor;
import com.sun.jdi.Location;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.event.BreakpointEvent;
import org.jdiscript.JDIScript;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by clg on 2018/1/23.
 */
public class SessionMonitor extends Monitor{

    public SessionMonitor(JDIScript j) {
        super(j);
    }

    @Override
    public Location setBreakPoint() throws Exception {
        return j.vm().classesByName("cn.com.sge.gems.base.web.filter.UserSessionFilter")
                .get(0).locationsOfLine(71).get(0);
    }



    private String sessionExpression;
    {
        sessionExpression  = "var session = request@getSession();";
        sessionExpression += "var sessionId = session@getId();";
        sessionExpression += "var user = session@getAttribute(\"SGE_USER\");";
        sessionExpression += "var userName = user==null ? null : user@loginName;";
    }

    @Override
    public void operate(BreakpointEvent breakpoint) throws Exception{
        StackFrame stackFrame = breakpoint.thread().frame(0);
        ThreadReference thread = stackFrame.thread();

        System.out.println("session Monitor thread uid:"+thread.uniqueID());

        ExpressionContext context = new ExpressionContext();
        context.putObject("thread",thread);
        context.putObject("request",stackFrame.getArgumentValues().get(0));
        ExpressionExecutor.execute(sessionExpression,context);

        String sessionId = (String) context.getObject("sessionId");
        String userName = (String) context.getObject("userName");

        SessionData sessionData = ContextData.getSessionDataById(sessionId);
        if(sessionData==null){
            sessionData = new SessionData(sessionId,userName);
            ContextData.addSessionData(sessionData);
            System.out.println("session monitor add sessionData userNaem:"+userName+",sessionId:"+sessionId);
        }
    }
}
