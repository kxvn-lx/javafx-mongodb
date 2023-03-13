package com.example.bjb2.Controllers;

import com.example.Database.DAO.SalesDAO;
import com.example.Database.Salesman;
import com.example.bjb2.Controllers.Dialogs.AddsalesController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SalesController implements Initializable {
    @FXML
    private TableColumn<Salesman, Integer> noSalesmanTbCol;
    @FXML
    private TableColumn<Salesman, String> namaCol;
    @FXML
    private TableColumn<Salesman, String> alamatCol;
    @FXML
    private TableView<Salesman> tableView;
    @FXML private Button tambahBtn;
    @FXML private Button rubahBtn;
    @FXML private Button hapusBtn;
    private SalesDAO dao;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        noSalesmanTbCol.setCellValueFactory(new PropertyValueFactory<>("no_salesman"));
        namaCol.setCellValueFactory(new PropertyValueFactory<>("nama"));
        alamatCol.setCellValueFactory(new PropertyValueFactory<>("alamat"));

        dao = new SalesDAO();
        dao.addListener(tableView);

        tableView.getItems().setAll(dao.getAll());

        applyTableViewListeners();
        setupContextMenu();
    }

    // EVENT LISTENERS
    public void handleTambahBtn() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/example/bjb2/AddSalesDialog.fxml"));
            DialogPane addSalesPane = fxmlLoader.load();

            AddsalesController c = fxmlLoader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(addSalesPane);
            dialog.setTitle("Form Tambah Salesman");

            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.get() == ButtonType.OK) {
                if (dao.add(c.getSales())) {
                    System.out.println("ADD SALESMAN OK");
                } else {
                    System.out.println("ADD SALESMAN FAILED");
                }

            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleRubahBtn() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/example/bjb2/AddSalesDialog.fxml"));
            DialogPane addSalesPane = fxmlLoader.load();

            AddsalesController c = fxmlLoader.getController();
            // Get the selection model
            TableView.TableViewSelectionModel<Salesman> selectionModel = tableView.getSelectionModel();
            // Get the selected item
            Salesman selectedItem = selectionModel.getSelectedItem();
            int index = selectionModel.getSelectedIndex();

            c.setTFs(selectedItem);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(addSalesPane);
            dialog.setTitle("Form Rubah Salesman");

            tableView.getSelectionModel().clearSelection();

            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.get() == ButtonType.OK) {
                if (dao.update(index, c.getSales())) {
                    System.out.println("UPDATE SALESMAN OK");
                } else {
                    System.out.println("UPDATE SALESMAN FAILED");
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleHapusBtn() {
        // Get the selection model
        TableView.TableViewSelectionModel<Salesman> selectionModel = tableView.getSelectionModel();
        // Get the selected item
        Salesman selectedItem = selectionModel.getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi penghapusan Salesman");
        alert.setHeaderText("Anda yakin akan menghapus: " + selectedItem.toString());

        // show the dialog and wait for a response
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (dao.delete(selectedItem)) {
                    System.out.println("DELETE SALES OK");
                } else {
                    System.out.println("DELETE SALES FAILED");
                }
            }

            tableView.getSelectionModel().clearSelection();
        });

    }

    // PRIVATE FUNCTIONS
    private void applyTableViewListeners() {
        // Listen to selected row
        TableView.TableViewSelectionModel<Salesman> selectionModel = tableView.getSelectionModel();
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
        tableView.setOnContextMenuRequested(event -> {
            contextMenu.show(tableView, event.getScreenX(), event.getScreenY());
        });
    }
}
