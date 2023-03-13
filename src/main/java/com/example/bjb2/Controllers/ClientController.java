package com.example.bjb2.Controllers;

import com.example.bjb2.Views.VFModel;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    public BorderPane client_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        VFModel.getInstance().getVF().getClientSelectedMenuItem().addListener((observableValue, oldVal, newVal) -> {
            switch (newVal) {
                case "Sales": client_parent.setCenter(VFModel.getInstance().getVF().getSalesView()); break;
                case "Langganan": client_parent.setCenter(VFModel.getInstance().getVF().getLanggananView()); break;
                case "Stock": client_parent.setCenter(VFModel.getInstance().getVF().getStockView()); break;
                case "Penjualan": client_parent.setCenter(VFModel.getInstance().getVF().getPenjualanView()); break;
                case "Setoran": client_parent.setCenter(VFModel.getInstance().getVF().getSetoranView()); break;
                default: client_parent.setCenter(null);
            }
        });
    }
}
