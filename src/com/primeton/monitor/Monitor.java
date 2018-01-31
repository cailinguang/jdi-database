package com.primeton.monitor;


import com.sun.jdi.Location;
import com.sun.jdi.event.BreakpointEvent;
import org.jdiscript.JDIScript;
import org.jdiscript.handlers.DebugLocatableHandler;
import org.jdiscript.requests.ChainingBreakpointRequest;
import org.jdiscript.requests.JDIScriptEventRequest;

/**
 * Created by clg on 2017/12/26.
 */
public abstract class Monitor {
    protected JDIScript j = null;

    public Monitor(JDIScript j){
        this.j = j;
    }

    /**
     * 设置断点
     */
    public abstract Location setBreakPoint() throws Exception;

    /**
     * 操作
     */
    public abstract void operate(BreakpointEvent breakpoint) throws Exception;


    /**
     * 设置事件(setBreakPoint)和处理程序(operate)
     * @author zhoujiawei
     * @throws Exception
     */
    public void monitor() {

        Location location = null;
        try {
            location = setBreakPoint();
        } catch (Exception e) {
            throw new RuntimeException(this.getClass().getSimpleName()+" location error:"+e.getMessage(),e);
        }
        ChainingBreakpointRequest chainingBreakpointRequest = j.breakpointRequest(location).addHandler(e->{
            try{
                operate(e);
            }catch (Throwable error){
                error.printStackTrace();
                System.out.println(this.getClass().getSimpleName()+" operate error:"+error.getMessage());
            }
        });
        chainingBreakpointRequest.enable();
    }
}
