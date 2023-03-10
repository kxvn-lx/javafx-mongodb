package com.example.bjb2.Controllers.Dialogs;

import com.example.Database.Salesman;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.bson.types.ObjectId;

import java.net.URL;
import java.util.ResourceBundle;

public class AddsalesController implements Initializable {

    @FXML private TextField noSalesmanTF;
    @FXML private TextField namaTF;
    @FXML private TextField alamatTF;
    @FXML private DialogPane dialogPane;
    private Button okButton;
    @FXML private Text dialogTitle;
    private Salesman salesman;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // force the field to be numeric only
        noSalesmanTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                noSalesmanTF.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setDisable(true);

        applyTFsListeners();
    }

    private void applyTFsListeners() {
        // Listen to textfield
        noSalesmanTF.textProperty().addListener((observable, oldValue, newValue) -> {
            validateForm();
        });
        namaTF.textProperty().addListener((observable, oldValue, newValue) -> {
            validateForm();
        });
        alamatTF.textProperty().addListener((observable, oldValue, newValue) -> {
            validateForm();
        });
    }

    public Salesman getSales() {
        if (this.salesman == null) {
            return new Salesman(new ObjectId(), namaTF.getText().trim(), Integer.parseInt(noSalesmanTF.getText().trim()), alamatTF.getText().trim());
        } else {
            return new Salesman(this.salesman.getId(), namaTF.getText().trim(), Integer.parseInt(noSalesmanTF.getText().trim()), alamatTF.getText().trim());
        }
    }
    public void setTFs(Salesman sales) {
        dialogTitle.setText("Rubah Salesman");
        this.salesman = sales;
        noSalesmanTF.setText( Integer.toString(sales.getNo_salesman()));
        namaTF.setText(sales.getNama());
        alamatTF.setText(sales.getAlamat());
    }
    public boolean isNull() {
       return namaTF.getText().isEmpty() || noSalesmanTF.getText().isEmpty() || alamatTF.getText().isEmpty();
    }

    private void validateForm() {
        okButton.setDisable(isNull());
    }

}
