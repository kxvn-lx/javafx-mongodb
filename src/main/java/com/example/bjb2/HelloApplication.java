package com.example.bjb2;

import com.example.bjb2.Models.Model;
import com.example.bjb2.Views.ViewFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    public static void print(Object o) {
        System.out.println(o);
    }
    @Override
    public void start(Stage stage) {
        Model.getInstance().getVF().showClientWindow();
    }

    public static void main(String[] args) {
        launch();
    }
}