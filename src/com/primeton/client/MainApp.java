package com.primeton.client;

import com.primeton.client.page.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
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
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(createContent()));

        setUserAgentStylesheet(STYLESHEET_MODENA);
        primaryStage.getScene().getStylesheets().add(Thread.class.getResource("/com/sun/javafx/scene/control/skin/modena/yellowOnBlack.css").toExternalForm());
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

    public static void main(String[] args) {
        Application.launch(args);
    }
}
