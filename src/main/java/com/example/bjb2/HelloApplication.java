package com.example.bjb2;

import com.example.bjb2.Views.VFModel;
import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    public static void print(Object o) {
        System.out.println(o);
    }
    @Override
    public void start(Stage stage) {
        VFModel.getInstance().getVF().showClientWindow();
    }

    public static void main(String[] args) {
        launch();
    }
}