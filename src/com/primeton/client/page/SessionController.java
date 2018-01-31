package com.primeton.client.page;

import com.primeton.connector.Connector;
import com.primeton.data.ContextData;
import com.primeton.data.SessionData;
import com.primeton.monitor.Monitor;
import com.primeton.monitor.SessionMonitor;
import com.primeton.monitor.SqlMonitor;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.event.VMStartEvent;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.jdiscript.JDIScript;
import org.jdiscript.handlers.DebugEventHandler;
import org.jdiscript.handlers.OnVMDeath;
import org.jdiscript.handlers.OnVMDisconnect;
import org.jdiscript.handlers.OnVMStart;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by clg on 2018/1/31.
 */
public class SessionController extends Controller {

    @FXML
    TextField ipText;
    @FXML
    TextField portText;
    @FXML
    TableView<SessionData> sessionTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ipText.setText("180.3.13.118");
        portText.setText("8787");

        sessionTable.getColumns().forEach(c->{
        });

        ContextData.sessionDatas.addListener(new ListChangeListener<SessionData>() {
            @Override
            public void onChanged(Change<? extends SessionData> c) {
                sessionTable.setItems(ContextData.sessionDatas);
            }
        });
    }

    public void connect(){

        String ip = ipText.getText();
        String port = portText.getText();

        if(ip.equals("")||port.equals("")|| !port.matches("^\\d+$")){
            alertWarning("请输入正确的参数！");
            return;
        }

        Connector conn = Connector.getInstance();
        conn.setPort(Integer.valueOf(portText.getText()));
        conn.setHostName(ipText.getText());

        try {
            VirtualMachine vm = conn.getVM();

            JDIScript j = new JDIScript(vm);

            Monitor sessionMonitor = new SessionMonitor(j);
            Monitor sqlMonitor = new SqlMonitor(j);

            sessionMonitor.monitor();
            sqlMonitor.monitor();


            List<DebugEventHandler> vmEventHandlers = new ArrayList();
            vmEventHandlers.add(new OnVMStart(){
                @Override
                public void vmStart(VMStartEvent vmStartEvent) {
                    System.out.println("连接成功...");
                }
            });
            vmEventHandlers.add(new OnVMDisconnect(){
                @Override
                public void vmDisconnect(VMDisconnectEvent vmDisconnectEvent) {
                    System.out.println("连接断开了...");
                }
            });
            vmEventHandlers.add(new OnVMDeath(){
                @Override
                public void vmDeath(VMDeathEvent vmDeathEvent) {
                    System.out.println("连接死了...");
                }
            });


            //处理任务超时3s
            j.run(vmEventHandlers,3000);

        } catch (Exception e) {
            alertError(e);
            return;
        }

    }

    public void disConnect(){

    }
}
