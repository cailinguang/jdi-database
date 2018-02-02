package com.primeton.client;

import com.primeton.client.page.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by clg on 2018/1/31.
 */
public class MainApp extends Application{

    public Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        //primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(createContent()));

        setUserAgentStylesheet(STYLESHEET_MODENA);
        primaryStage.getScene().getStylesheets().add(Thread.class.getResource("/com/sun/javafx/scene/control/skin/modena/whiteOnBlack.css").toExternalForm());
        primaryStage.show();
    }

    private Parent createContent() {
        return replaceSceneContent("page/session.fxml");
    }

    private Pane replaceSceneContent(String fxml) {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = MainApp.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(MainApp.class.getResource(fxml));
        Pane page = null;
        try {
            page =  loader.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }

        ((Controller) loader.getController()).setApp(this);

        return page;
    }

    public void lodding(Pane root){
        ProgressIndicator p = (ProgressIndicator) root.lookup("#__lodding_p");
        Rectangle veil = (Rectangle) root.lookup("#__lodding_r");
        if(p==null){
            p = new ProgressIndicator();
            p.setId("__lodding_p");
            p.setPrefSize(50,50);

            p.layoutXProperty().set(root.getPrefWidth()/2-25);
            p.layoutYProperty().set(root.getPrefHeight()/2-25);

            veil = new Rectangle(root.getPrefWidth(),root.getHeight(),new Color(0,0,0,0.5));
            veil.setId("__lodding_r");
            veil.setLayoutX(0);
            veil.setLayoutY(0);
            root.getChildren().addAll(veil,p);
        }

        p.visibleProperty().set(true);
        veil.visibleProperty().set(true);
    }

    public void stopLodding(Pane root){
        ProgressIndicator p = (ProgressIndicator) root.lookup("#__lodding_p");
        Rectangle veil = (Rectangle) root.lookup("#__lodding_r");
        if(p!=null){
            p.visibleProperty().set(false);
            veil.visibleProperty().set(false);
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
