package com.example.bjb2.Controllers;

import com.example.bjb2.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    public BorderPane client_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getVF().getClientSelectedMenuItem().addListener((observableValue, oldVal, newVal) -> {
            switch (newVal) {
                case "Sales": client_parent.setCenter(Model.getInstance().getVF().getSalesView()); break;
                case "Langganan": client_parent.setCenter(Model.getInstance().getVF().getLanggananView()); break;
                default: client_parent.setCenter(null);
            }
        });
    }
}
