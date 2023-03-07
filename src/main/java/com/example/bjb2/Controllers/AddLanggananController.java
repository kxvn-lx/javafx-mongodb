package com.example.bjb2.Controllers;

import com.example.Database.Models.Langganan;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AddLanggananController implements Initializable {
    @FXML
    private DialogPane dialogPane;
    @FXML private TextField noLanggananTF;
    @FXML private TextField namaTF;
    @FXML private TextField alamatTF;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dialogPane.lookupButton(ButtonType.OK).setDisable(true);
        applyTFsListeners();
    }

    public Langganan getLangganan() {
        return new Langganan(noLanggananTF.getText(), namaTF.getText(), alamatTF.getText());
    }

    public boolean isNull() {
        return namaTF.getText().isEmpty() || noLanggananTF.getText().isEmpty() || alamatTF.getText().isEmpty();
    }

    public void setTFs(Langganan lg) {
        dialogPane.setHeaderText("Rubah Langganan");

        noLanggananTF.setText(lg.getNo_langganan());
        namaTF.setText(lg.getNama());
        alamatTF.setText(lg.getAlamat());
    }

    private void applyTFsListeners() {
        // Listen to textfield
        noLanggananTF.textProperty().addListener((observable, oldValue, newValue) -> validateForm());
        namaTF.textProperty().addListener((observable, oldValue, newValue) -> validateForm());
        alamatTF.textProperty().addListener((observable, oldValue, newValue) -> validateForm());
    }

    private void validateForm() {
        dialogPane.lookupButton(ButtonType.OK).setDisable(isNull());
    }
}