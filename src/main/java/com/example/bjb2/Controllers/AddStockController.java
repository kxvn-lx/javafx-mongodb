package com.example.bjb2.Controllers;

import com.example.Database.Models.Langganan;
import com.example.Database.Models.Stock;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AddStockController implements Initializable {
    @FXML
    private DialogPane dialogPane;
    @FXML
    private TextField kodeTF;
    @FXML private TextField namaTF;
    @FXML private TextField merekTF;
    @FXML private TextField hargaTF;
    @FXML private TextField satuanTF;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dialogPane.lookupButton(ButtonType.OK).setDisable(true);
        // force the field to be numeric only
        hargaTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                hargaTF.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        applyTFsListeners();
    }


    public Stock getStock() {
        return new Stock(kodeTF.getText(), namaTF.getText(), merekTF.getText(), Integer.parseInt(hargaTF.getText()), satuanTF.getText());
    }

    public boolean isNull() {
        return namaTF.getText().isEmpty() || kodeTF.getText().isEmpty() || merekTF.getText().isEmpty() || hargaTF.getText().isEmpty() || satuanTF.getText().isEmpty();
    }

    public void setTFs(Stock s) {
        dialogPane.setHeaderText("Rubah Stock");

        kodeTF.setText(s.getKode());
        namaTF.setText(s.getNama());
        merekTF.setText(s.getMerek());
        hargaTF.setText(Integer.toString(s.getHarga()));
        satuanTF.setText(s.getSatuan());
    }

    private void applyTFsListeners() {
        TextField[] tfs = {kodeTF, namaTF, merekTF, hargaTF, satuanTF};

        for (TextField tf : tfs) {
            tf.textProperty().addListener((observable, oldValue, newValue) -> validateForm());
        }

    }

    private void validateForm() {
        dialogPane.lookupButton(ButtonType.OK).setDisable(isNull());
    }
}
