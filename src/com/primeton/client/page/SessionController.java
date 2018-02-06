package com.primeton.client.page;

import com.primeton.connector.Connector;
import com.primeton.data.ContextData;
import com.primeton.data.SessionData;
import com.primeton.monitor.SessionMonitor;
import com.primeton.monitor.SqlMonitor;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.event.VMStartEvent;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javafx.util.Duration;
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
    @FXML
    AnchorPane connectPanel;
    @FXML
    Button connectBtn;
    @FXML
    Button disconnectBtn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ipText.setText("180.3.13.118");
        portText.setText("8787");

        sessionTable.getColumns().forEach(c->{
            if("userColumn".equals(c.getId())){
                c.setCellValueFactory(new PropertyValueFactory("sessionUserName"));
            }
            else if("loginColumn".equals(c.getId())){
                c.setCellValueFactory(new PropertyValueFactory("sessionLoginName"));
            }
            else if("sessionColumn".equals(c.getId())){
                c.setCellValueFactory(new PropertyValueFactory("sessionId"));
            }
            else if("operaterColumn".equals(c.getId())){
                c.setCellValueFactory(new PropertyValueFactory("sessionUserName"));
            }
        });

        ContextData.sessionDatas.addListener(new ListChangeListener<SessionData>() {
            @Override
            public void onChanged(Change<? extends SessionData> c) {
                sessionTable.setItems(ContextData.sessionDatas);
            }
        });

        //宽度自适应
        JavafxControl.tableColumnAutoWidth(sessionTable,new float[]{1.0f,1.0f,1.0f,1.0f});
    }

    public void connect(){
        app.lodding(connectPanel);

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

            SessionMonitor sessionMonitor = new SessionMonitor(j);
            sessionMonitor.doMonitor();

            SqlMonitor sqlMonitor = new SqlMonitor(j);
            sqlMonitor.doMonitor();

            List<DebugEventHandler> vmEventHandlers = new ArrayList();
            vmEventHandlers.add(new OnVMStart(){
                @Override
                public void vmStart(VMStartEvent vmStartEvent) {
                    canConnect(false);
                    System.out.println("连接成功...");
                }
            });
            vmEventHandlers.add(new OnVMDisconnect(){
                @Override
                public void vmDisconnect(VMDisconnectEvent vmDisconnectEvent) {
                    canConnect(true);
                    System.out.println("连接断开了...");
                }
            });
            vmEventHandlers.add(new OnVMDeath(){
                @Override
                public void vmDeath(VMDeathEvent vmDeathEvent) {
                    canConnect(true);
                    System.out.println("连接死了...");
                }
            });


            //处理任务超时3s
            j.run(vmEventHandlers,0);

            app.stopLodding(connectPanel);
            canConnect(false);
        } catch (Exception e) {
            alertError(e);
            app.stopLodding(connectPanel);
            return;
        }

    }

    public void disConnect(){
        Connector.getInstance().disconnectVM();
    }

    public void canConnect(boolean can){
        connectBtn.setDisable(!can);
        disconnectBtn.setDisable(can);
    }
}
