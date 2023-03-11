package com.example.bjb2.Controllers;

import com.example.Database.DAO.PenjualanDAO;
import com.example.Database.Penjualan;
import com.example.bjb2.Models.Model;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SetoranController implements Initializable {
    @FXML private Button rubahBtn;
    @FXML private TableColumn<Penjualan, Integer> noFakturCol;
    @FXML private TableColumn<Penjualan, Integer> jumlahCol;
    @FXML private TableColumn<Penjualan, Integer> setoranCol;
    @FXML private TableView<Penjualan> tableView;
    private final PenjualanDAO dao = new PenjualanDAO();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTVCellValueFactory();
        getNotPaid();

        Model.getInstance().getVF().getClientSelectedMenuItem().addListener((observableValue, oldVal, newVal) -> {
            if (newVal.equals("Setoran")) {
                getNotPaid();
            }
        });
    }

    public void handleRubahBtn() {

    }

    private void setupTVCellValueFactory() {
        noFakturCol.setCellValueFactory(new PropertyValueFactory<>("noFaktur"));
        jumlahCol.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        setoranCol.setCellValueFactory(new PropertyValueFactory<>("setoran"));
    }
    private void getNotPaid() {
        List<Penjualan> allList = dao.get();
        // By Default, get just not paid
        List<Penjualan> notPaidList = allList.stream()
                .filter(penjualan -> penjualan.getSetoran() < penjualan.getJumlah())
                .collect(Collectors.toList());

        tableView.getItems().setAll(notPaidList);
    }
}
