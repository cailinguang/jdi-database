package com.primeton.client.page;

import com.primeton.client.JavafxControl;
import com.primeton.client.MainApp;
import com.primeton.connector.Connector;
import com.primeton.data.ContextData;
import com.primeton.data.SessionData;
import com.primeton.monitor.SessionMonitor;
import com.primeton.monitor.SqlMonitor;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.event.VMStartEvent;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.jdiscript.JDIScript;
import org.jdiscript.handlers.DebugEventHandler;
import org.jdiscript.handlers.OnVMDeath;
import org.jdiscript.handlers.OnVMDisconnect;
import org.jdiscript.handlers.OnVMStart;
import java.net.URL;
import java.util.*;

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
                c.setCellValueFactory(new Callback() {
                    @Override
                    public Object call(Object p) {
                        TableColumn.CellDataFeatures<SessionData, String> features = (TableColumn.CellDataFeatures<SessionData, String>)p;
                        return new SimpleObjectProperty(features.getValue());
                    }
                });

                c.setCellFactory(new Callback() {
                    @Override
                    public Object call(Object param) {
                        TableColumn column = (TableColumn)param;
                        return new AddSessionButton();
                    }
                });

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
            e.printStackTrace();
            alertError(e);
            app.stopLodding(connectPanel);
            return;
        }

    }

    public void disConnect(){
        Connector.getInstance().disconnectVM();
        sessionTable.setItems(null);

        sqlStageMap.clear();
        ContextData.sessionDatas.clear();
    }

    @Override
    public void setApp(MainApp app) {
        super.setApp(app);
        app.stage.setOnCloseRequest(event -> {
            disConnect();
        });
    }

    public void canConnect(boolean can){
        connectBtn.setDisable(!can);
        disconnectBtn.setDisable(can);
    }


    private Map<String,Stage> sqlStageMap = new HashMap();

    public void close(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void about(ActionEvent actionEvent) {
        alertInfo("desc : insert/update/delete database operate monitory.\n\nversion : 1.0\n\n");
    }

    private class AddSessionButton extends TableCell<SessionData, SessionData> {
        // a button for adding a new person.
        final Button monitorButton = new Button("监控");

        final Button showButton = new Button("show");

        // pads and centers the add button in the cell.
        final HBox hboxPane = new HBox();
        /**
         * AddPersonCell constructor
         * @param stage the stage in which the table is placed.
         * @param table the table to which a new person can be added.
         */
        AddSessionButton() {
            //hboxPane.setPadding(new Insets(3));
            monitorButton.setStyle("-fx-base:blue;");
            hboxPane.getChildren().add(monitorButton);
            hboxPane.getChildren().add(showButton);
            hboxPane.setSpacing(5);

            //click
            monitorButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent actionEvent) {
                    boolean isMonitor = !getItem().isMonitor();
                    ((SessionData) getTableRow().getItem()).setMonitor(isMonitor);
                    if(isMonitor){
                        monitorButton.setStyle("-fx-base:yellow;");
                        monitorButton.setText("停止");
                        showSqlDialog();
                    }else{
                        monitorButton.setStyle("-fx-base:blue;");
                        monitorButton.setText("监控");
                    }
                }
            });


            showButton.setOnAction(actionEvent->{
                showSqlDialog();
            });
        }

        private void showSqlDialog(){
            String sessionId = getItem().getSessionId();
            Stage dialog = sqlStageMap.get(sessionId);
            if(dialog==null){
                dialog = new Stage();
                sqlStageMap.put(sessionId,dialog);

                // initialize the dialog.
                dialog.setTitle("sql modify view session:"+(sessionId));
                //dialog.initOwner(stage);  //对话框永远在前面
                //dialog.initModality(Modality.WINDOW_MODAL);  //必须关闭对话框后才能操作其他的
                //dialog.initStyle(StageStyle.UTILITY); //对话框-只保留关闭按钮
                dialog.setX(app.stage.getX() + 100);
                dialog.setY(200);

                SqlController controller = (SqlController) app.loadXmlPane("page/sql.fxml");
                //set sessionData and bind event
                controller.setSessionData((SessionData)getTableRow().getItem());
                dialog.setScene(new Scene(controller.getPane()));
                app.setTheme(dialog);
            }
            dialog.show();
        }

        /** places an add button in the row only if the row is not empty. */
        @Override
        protected void updateItem(SessionData item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                boolean isMonitor = item.isMonitor();
                if(isMonitor){
                    monitorButton.setStyle("-fx-base:yellow;");
                    monitorButton.setText("停止");
                }else{
                    monitorButton.setStyle("-fx-base:blue;");
                    monitorButton.setText("监控");
                }

                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setGraphic(hboxPane);
            } else {
                setGraphic(null);
            }
        }
    }
}
