package com.example.bjb2.Controllers;

import com.example.bjb2.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    public Button sales_btn;
    public Button langganan_btn;
    public Button stock_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }

    private void addListeners() {
        sales_btn.setOnAction(event -> onSales());
        langganan_btn.setOnAction(event -> onLangganan());
    }

    private void onSales() {
        Model.getInstance().getVF().getClientSelectedMenuItem().set("Sales");
    }

    private void onLangganan() {
        Model.getInstance().getVF().getClientSelectedMenuItem().set("Langganan");
    }
}
