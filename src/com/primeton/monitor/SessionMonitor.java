package com.primeton.monitor;

import com.primeton.expression.ExpressionExecutor;
import com.primeton.expression.VMClassFunction;
import com.sun.jdi.Location;
import com.sun.jdi.StackFrame;
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
        return j.vm().classesByName("org.springframework.web.servlet.DispatcherServlet")
                .get(0).locationsOfLine(895).get(0);
    }

    @Override
    public void operate(BreakpointEvent breakpoint) throws Exception{
        StackFrame stackFrame = breakpoint.thread().frame(0);

        Map<String, Object> context = new HashMap();
        context.put("stackFrame",stackFrame);
        String expression = "";
        ExpressionExecutor.execute(expression,context);
    }
}
