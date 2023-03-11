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
    public Button penjualan_btn;
    public Button setoran_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }

    private void addListeners() {
        sales_btn.setOnAction(event -> onSales());
        langganan_btn.setOnAction(event -> onLangganan());
        stock_btn.setOnAction(event -> onStock());
        penjualan_btn.setOnAction(event -> onPenjualan());
        setoran_btn.setOnAction(event -> onSetoran());
    }
    private void onSales() {
        Model.getInstance().getVF().getClientSelectedMenuItem().set("Sales");
    }
    private void onLangganan() {
        Model.getInstance().getVF().getClientSelectedMenuItem().set("Langganan");
    }
    private void onStock() { Model.getInstance().getVF().getClientSelectedMenuItem().set("Stock"); }
    private void onPenjualan() { Model.getInstance().getVF().getClientSelectedMenuItem().set("Penjualan"); }
    private void onSetoran() { Model.getInstance().getVF().getClientSelectedMenuItem().set("Setoran"); }
}
