package com.primeton.client.page;

import com.primeton.client.JavafxControl;
import com.primeton.data.DatabaseData;
import com.primeton.data.SessionData;
import com.primeton.data.ThreadData;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by clg on 2018/2/7.
 */
public class SqlController extends Controller{

    @FXML
    TabPane tabPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    private SessionData sessionData;
    public void setSessionData(SessionData sessionData){
        this.sessionData = sessionData;

        pane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                tabPane.setPrefHeight((double)newValue);
            }
        });

        this.sessionData.getThreadDatas().addListener(new ListChangeListener<ThreadData>() {
            @Override
            public void onChanged(Change<? extends ThreadData> c) {
                //threadData only can add,so this is add item event
                //get last
                ThreadData threadData = c.getList().get(c.getList().size()-1);

                Tab tab = new Tab("tid-"+threadData.getThreadId());
                Accordion accordion = new Accordion();
                accordion.setPrefHeight(tabPane.getPrefHeight());


                threadData.getDatabaseDatas().addListener(new ListChangeListener<DatabaseData>() {
                    @Override
                    public void onChanged(Change<? extends DatabaseData> c) {
                        DatabaseData databaseData = c.getList().get(c.getList().size()-1);

                        if("CUD".indexOf(databaseData.getType())>-1){
                            TitledPane titledPane = new TitledPane();
                            titledPane.setText(databaseData.getDesc());

                            ScrollPane scrollPane = new ScrollPane();


                            TableView tableView = new TableView();
                            TableColumn column = new TableColumn();
                            column.setText("字段");
                            column.setCellValueFactory(new ArrayValueFactory(databaseData.getType(),0));
                            tableView.getColumns().add(column);

                            switch (databaseData.getType()){
                                case 'C':{
                                    int newSize = databaseData.getData().size();
                                    for(int i=0;i<newSize;i++){
                                        TableColumn data = new TableColumn();
                                        data.setText("数据"+(i+1));
                                        data.setCellValueFactory(new ArrayValueFactory(databaseData.getType(),i+1));
                                        tableView.getColumns().add(data);
                                    }
                                    break;
                                }
                                case 'U':{
                                    int oldSize = databaseData.getData().size();
                                    int newSize = databaseData.getData().size();
                                    for(int i=0;i<oldSize;i++){
                                        TableColumn data = new TableColumn();
                                        data.setText("原数据"+(i+1));
                                        data.setCellValueFactory(new ArrayValueFactory(databaseData.getType(),i*2+1));
                                        tableView.getColumns().add(data);
                                    }
                                    for(int i=0;i<newSize;i++){
                                        TableColumn data = new TableColumn();
                                        data.setText("新数据"+(i+1));
                                        data.setCellValueFactory(new ArrayValueFactory(databaseData.getType(),i*2+2));
                                        tableView.getColumns().add(data);
                                    }
                                    break;
                                }
                                case 'D':{
                                    int oldSize = databaseData.getData().size();
                                    for(int i=0;i<oldSize;i++){
                                        TableColumn data = new TableColumn();
                                        data.setText("数据"+(i+1));
                                        data.setCellValueFactory(new ArrayValueFactory(databaseData.getType(),i+1));
                                        tableView.getColumns().add(data);
                                    }
                                    break;
                                }
                            }

                            tableView.setItems(databaseData.getTableViewData());



                            Platform.runLater(()->{
                                scrollPane.setFitToWidth(true);
                                scrollPane.setContent(tableView);
                                titledPane.setContent(scrollPane);
                                accordion.getPanes().add(titledPane);

                                JavafxControl.tableColumnAutoWidth(tableView);
                            });
                        }

                    }
                });

                tab.setContent(accordion);

                Platform.runLater(()->{
                    tabPane.getTabs().add(tab);
                });
            }
        });
    }
}

class ArrayValueFactory implements Callback<TableColumn.CellDataFeatures<String[],String>, ObservableValue>{

    private char type;
    private int index;

    public ArrayValueFactory(char type,int index) {
        this.type = type;
        this.index = index;
    }

    @Override
    public ObservableValue call(TableColumn.CellDataFeatures<String[],String> param) {
        return new SimpleStringProperty(param.getValue()[index]);
    }
}
