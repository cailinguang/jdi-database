package com.primeton.test;

import com.primeton.connector.Connector;
import com.primeton.expression.VMClassFunction;
import com.primeton.monitor.Monitor;
import com.primeton.monitor.SessionMonitor;
import com.sun.jdi.VirtualMachine;
import org.jdiscript.JDIScript;
import org.junit.Test;

import java.lang.reflect.Array;

/**
 * Created by clg on 2018/1/23.
 */
public class JDITest {

    static {
    }
    @Test
    public void testJDI(){
        Connector connector = Connector.getInstance();
        connector.setHostName("180.3.13.194");
        connector.setPort(7234);
        connector.setTimeout(2000);
        VirtualMachine vm = connector.getVM();

        JDIScript j = new JDIScript(vm);

        Monitor sessionMonitor = new SessionMonitor(j);

        sessionMonitor.monitor();

        j.run();


    }
}
