package com.example.bjb2.Controllers;

import com.example.Database.Stock;
import com.example.Database.DAO.StockDAO;
import com.example.bjb2.Controllers.Dialogs.AddStockController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class StockController implements Initializable {
    @FXML private TableView<Stock> tableView;
    @FXML private TableColumn<Stock, String> kodeCol;
    @FXML private TableColumn<Stock, String> namaCol;
    @FXML private TableColumn<Stock, String> merekCol;
    @FXML private TableColumn<Stock, Integer> hargaCol;
    @FXML private TableColumn<Stock, String> satuanCol;
    @FXML private Button rubahBtn;
    @FXML private Button hapusBtn;
    private StockDAO dao;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        kodeCol.setCellValueFactory(new PropertyValueFactory<>("kode"));
        namaCol.setCellValueFactory(new PropertyValueFactory<>("nama"));
        merekCol.setCellValueFactory(new PropertyValueFactory<>("merek"));
        hargaCol.setCellValueFactory(new PropertyValueFactory<>("harga"));
        satuanCol.setCellValueFactory(new PropertyValueFactory<>("satuan"));

        dao = new StockDAO();
        dao.addListener(tableView);

        tableView.getItems().setAll(dao.get());

        applyTableViewListeners();
        setupContextMenu();

    }

    public void handleTambahBtn(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/example/bjb2/AddStockDialog.fxml"));
            DialogPane pane = fxmlLoader.load();

            AddStockController c = fxmlLoader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(pane);
            dialog.setTitle("Form Tambah Stock");

            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.get() == ButtonType.OK) {
                dao.add(c.getStock());
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleRubahBtn(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/example/bjb2/AddStockDialog.fxml"));
            DialogPane pane = fxmlLoader.load();
            pane.setHeaderText("Rubah Stock");

            AddStockController c = fxmlLoader.getController();
            // Get the selection model
            TableView.TableViewSelectionModel<Stock> selectionModel = tableView.getSelectionModel();
            // Get the selected item
            Stock selectedItem = selectionModel.getSelectedItem();
            int index = selectionModel.getSelectedIndex();

            c.setTFs(selectedItem);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(pane);
            dialog.setTitle("Form Rubah Stock");

            tableView.getSelectionModel().clearSelection();
            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.get() == ButtonType.OK) {
                dao.update(index, c.getStock());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleHapusBtn(ActionEvent event) {
        // Get the selection model
        TableView.TableViewSelectionModel<Stock> selectionModel = tableView.getSelectionModel();
        // Get the selected item
        Stock selectedItem = selectionModel.getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi penghapusan Stock");
        alert.setHeaderText("Anda yakin akan menghapus: " + selectedItem.toString());

        // show the dialog and wait for a response
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                dao.delete(selectedItem);
            }

            tableView.getSelectionModel().clearSelection();
        });
    }

    /* PRIVATE METHODS */
    private void applyTableViewListeners() {
        // Listen to selected row
        TableView.TableViewSelectionModel<Stock> selectionModel = tableView.getSelectionModel();
        // Add a listener to the selection model
        selectionModel.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            rubahBtn.setDisable(newValue == null);
            hapusBtn.setDisable(newValue == null);
        });
    }
    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Deselect");
        menuItem1.setOnAction(event -> {
            tableView.getSelectionModel().clearSelection();
        });
        contextMenu.getItems().addAll(menuItem1);

        tableView.setContextMenu(contextMenu);
        tableView.setOnContextMenuRequested(event -> contextMenu.show(tableView, event.getScreenX(), event.getScreenY()));
    }


}
