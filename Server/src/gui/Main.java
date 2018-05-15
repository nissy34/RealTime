/*
 * Copyright (c) Yuval && Nissan
 */

package gui;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import BackEnd.Server770;
public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader =new FXMLLoader(getClass().getResource("GUI_server.fxml"));
        Parent root=loader.load();
        primaryStage.setTitle("scene builder");
        primaryStage.setScene(new Scene(root));
        //primaryStage.setResizable(false);
       // primaryStage.setOnCloseRequest();
        Server770 server770=new Server770(loader.getController());
        primaryStage.show();

    }



    public static void main(String[] args) {
        launch(args);
    }
}
