package com.primeton.monitor;

import com.primeton.expression.VMClassFunction;
import com.sun.jdi.Location;
import com.sun.jdi.event.BreakpointEvent;
import org.jdiscript.JDIScript;
import org.wltea.expression.ExpressionEvaluator;
import org.wltea.expression.datameta.Variable;
import org.wltea.expression.function.FunctionLoader;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by clg on 2018/1/23.
 */
public class SessionMonitor extends Monitor{

    public SessionMonitor(JDIScript j) {
        super(j);
    }

    @Override
    public Location setBreakPoint() throws Exception {
        return j.vm().classesByName("org.springframework.web.servlet.DispatcherServlet").get(0).locationsOfLine(895).get(0);
    }

    @Override
    public void operate(BreakpointEvent breakpoint) {
        List<Variable> variables = new ArrayList();
        variables.add(Variable.createVariable("event",breakpoint));
        Object obj = ExpressionEvaluator.evaluate("$C(event,\"org.springframework.web.context.request.RequestContextHolder\",\"getRequestAttributes\")",variables);
        System.out.println(obj);

    }
}
