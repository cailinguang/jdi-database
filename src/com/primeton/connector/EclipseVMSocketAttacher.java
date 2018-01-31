package com.primeton.connector;

import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector.Argument;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class EclipseVMSocketAttacher {
    private final String host;
    private final int port;
    private final int timeout;

    public EclipseVMSocketAttacher(int port) {
        this(null, port);
    }

    public EclipseVMSocketAttacher(int port, int timeout) {
        this(null, port, timeout);
    }

    public EclipseVMSocketAttacher(String host, int port) {
        this(host, port, 0);
    }

    public EclipseVMSocketAttacher(String host, int port, int timeout) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
    }

    public VirtualMachine safeAttach() throws IOException, IllegalConnectorArgumentsException
    {
        VirtualMachineManager manager = org.eclipse.jdi.Bootstrap.virtualMachineManager();
        List<AttachingConnector> connectors = manager.attachingConnectors();
        AttachingConnector connector = connectors.get(0);
        Map<String, com.sun.jdi.connect.Connector.Argument> args = connector.defaultArguments();
        args.get("port").setValue(String.valueOf(port));
        args.get("hostname").setValue(host);
        args.get("timeout").setValue(Integer.toString(timeout));

        final VirtualMachine vm = connector.attach(args);
        return vm;
    }
    
    /**
     * Like safeAttach but wraps any checked exceptions in a RuntimeException.
     * 
     * @return
     */
    public VirtualMachine attach() {
        try {
            return safeAttach();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
