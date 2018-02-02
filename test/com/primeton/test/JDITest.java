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

        SessionMonitor sessionMonitor = new SessionMonitor(j);
        sessionMonitor.doMonitor();
        SqlMonitor sqlMonitor = new SqlMonitor(j);
        sqlMonitor.doMonitor();

        //处理任务超时3s
        j.run();
    }

    @Test
    public void testGetSql(){
        Connector connector = Connector.getInstance();
        connector.setHostName("180.3.13.118");
        connector.setPort(8787);
        connector.setTimeout(10000);//连接超时时间10s
        VirtualMachine vm = connector.getVM();
        JDIScript j = new JDIScript(vm);

        j.methodEntryRequest().addClassFilter("oracle.jdbc.driver.PhysicalConnection").addHandler(e->{
           if("prepareStatement".equals(e.method().name())){
               System.out.println(e.method().name());
           }
        }).enable().setSuspendPolicy(MethodEntryRequest.SUSPEND_EVENT_THREAD);



        //处理任务超时3s
        j.run(0);
    }

    public static void main(String[] args) throws Exception{
        new JDITest().testJDI();
    }
}
