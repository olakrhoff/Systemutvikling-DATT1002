package ControlsAndLaunch;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.stream.Collector;

public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent homepage = FXMLLoader.load(getClass().getResource("/FXML/homepage/homepageGalleryView.fxml"));
        primaryStage.setTitle("Image gallery");
        primaryStage.setScene(new Scene(homepage));
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}