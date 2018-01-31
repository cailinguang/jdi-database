package com.primeton.test;

import com.primeton.connector.Connector;
import com.primeton.monitor.Monitor;
import com.primeton.monitor.SessionMonitor;
import com.primeton.monitor.SqlMonitor;
import com.sun.jdi.StringReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.request.MethodEntryRequest;
import org.eclipse.jdi.Bootstrap;
import org.jdiscript.JDIScript;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

import static org.jdiscript.util.Utils.unchecked;

/**
 * Created by clg on 2018/1/23.
 */
public class JDITest {

    static {
    }
    @Test
    public void testJDI(){
        Connector connector = Connector.getInstance();
        connector.setHostName("180.3.13.118");
        connector.setPort(8787);

        connector.setTimeout(10000);//连接超时时间10s
        VirtualMachine vm = connector.getVM();

        JDIScript j = new JDIScript(vm);

        Monitor sessionMonitor = new SessionMonitor(j);
        Monitor sqlMonitor = new SqlMonitor(j);
        sessionMonitor.monitor();
        sqlMonitor.monitor();

        //处理任务超时3s
        j.run();
    }
}
