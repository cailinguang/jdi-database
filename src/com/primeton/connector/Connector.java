package com.primeton.connector;

import com.sun.jdi.VirtualMachine;
import org.jdiscript.util.VMSocketAttacher;

/**
 */
public class Connector {
    private VirtualMachine vm = null;

    private String hostName;
    private int port;
    private int timeout;

    /**
     * 私有构造器
     */
    private Connector(){ }
    /**
     * 私有构造器
     */
    private Connector(String hostName, int port){
        this.hostName = hostName;
        this.port = port;
    }

    //单例变量
    private static Connector instance = new Connector();

    /**
     * 获取单例变量
     * @return
     */
    public static Connector getInstance(){
        return instance;
    }

    /**
     * 获取调试虚拟机
     * @return
     */
    public synchronized VirtualMachine getVM(){
        if(vm==null){
            vm = new EclipseVMSocketAttacher(this.hostName, this.port, this.timeout).attach();
        }
        return vm;
    }

    /**
     * 释放连接
     */
    public void disconnectVM(){
        if(vm!=null){
            vm.dispose();
        }
    }


    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
