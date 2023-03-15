package com.example.bjb2.Controllers;

import com.example.Database.DAO.PenjualanDAO;
import com.example.Database.Penjualan;
import com.example.bjb2.Controllers.Dialogs.AddPenjualanController;
import com.example.bjb2.Views.VFModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class PenjualanController implements Initializable {
    @FXML
    private TableView<Penjualan> tableView;
    @FXML private TableColumn<Penjualan, Integer> noFakturCol;
    @FXML private TableColumn<Penjualan, String> noSalesmanCol;
    @FXML private TableColumn<Penjualan, String> noLanggananCol;
    @FXML private TableColumn<Penjualan, String> tanggalCol;
    @FXML private TableColumn<Penjualan, String> statusCol;
    @FXML private TableColumn<Penjualan, String> stocksCol;
    @FXML private Button rubahBtn;
    @FXML private Button hapusBtn;
    private PenjualanDAO dao;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dao = new PenjualanDAO();
        dao.addListener(tableView);

        VFModel.getInstance().getVF().getClientSelectedMenuItem().addListener((observableValue, oldVal, newVal) -> {
            if (newVal.equals("Penjualan")) {
                tableView.refresh();
            }
        });

        noFakturCol.setCellValueFactory(new PropertyValueFactory<>("noFaktur"));
        noSalesmanCol.setCellValueFactory(cd -> new SimpleStringProperty(Integer.toString(cd.getValue().getNoSalesman())));
        noLanggananCol.setCellValueFactory(cd -> {
                Penjualan p = cd.getValue();
                return new SimpleStringProperty(p.getNoLangganan());
            });
        tanggalCol.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        stocksCol.setCellValueFactory(cd -> new SimpleStringProperty(Integer.toString(cd.getValue().getPjs().length)));

        tableView.getItems().setAll(dao.getAll());

        applyTableViewListeners();
        setupContextMenu();
    }

    public void handleTambahBtn() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/example/bjb2/AddPenjualanDialog.fxml"));
            DialogPane pane = fxmlLoader.load();

            AddPenjualanController c = fxmlLoader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(pane);
            dialog.setTitle("Form Tambah Penjualan");

            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.get() == ButtonType.OK) {
                if (dao.add(c.getPenjualan())) {
                    System.out.println("ADD PENJUALAN OK");
                } else {
                    System.out.println("ADD PENJUALAN FAILED");
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleRubahBtn() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/example/bjb2/AddPenjualanDialog.fxml"));
            DialogPane pane = fxmlLoader.load();
            pane.setHeaderText("Form Rubah Penjualan");

            AddPenjualanController c = fxmlLoader.getController();
            // Get the selection model
            TableView.TableViewSelectionModel<Penjualan> selectionModel = tableView.getSelectionModel();
            // Get the selected item
            Penjualan selectedItem = selectionModel.getSelectedItem();
            int index = selectionModel.getSelectedIndex();

            c.setTFs(selectedItem);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(pane);
            dialog.setTitle("Form Rubah Penjualan");

            tableView.getSelectionModel().clearSelection();
            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.get() == ButtonType.OK) {
                if (dao.update(index, c.getPenjualan())) {
                    System.out.println("UPDATE PENJUALAN OK");
                } else {
                    System.out.println("UPDATE PENJUALAN FAILED");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleHapusBtn() {
        // Get the selection model
        TableView.TableViewSelectionModel<Penjualan> selectionModel = tableView.getSelectionModel();
        // Get the selected item
        Penjualan selectedItem = selectionModel.getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi penghapusan Penjualan");
        alert.setHeaderText("Anda yakin akan menghapus: " + selectedItem.getNoFaktur().toString());

        // show the dialog and wait for a response
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.out.println(selectedItem);
                if(dao.delete(selectedItem)) {
                    System.out.println("DELETE STOCK OK");
                } else {
                    System.out.println("DELETE STOCK FAILED");
                }
            }

            tableView.getSelectionModel().clearSelection();
        });
    }

    private void applyTableViewListeners() {
        // Listen to selected row
        TableView.TableViewSelectionModel<Penjualan> selectionModel = tableView.getSelectionModel();
        // Add a listener to the selection model
        selectionModel.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            rubahBtn.setDisable(newValue == null);
            hapusBtn.setDisable(newValue == null);
        });
    }
    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Batal Memilih");
        menuItem1.setOnAction(event -> {
            tableView.getSelectionModel().clearSelection();
        });
        MenuItem menuItem2 = new MenuItem("Muat Ulang");
        menuItem2.setOnAction(event -> {
            tableView.getItems().setAll(dao.getAll());
        });

        contextMenu.getItems().addAll(menuItem1,menuItem2);

        tableView.setContextMenu(contextMenu);
        tableView.setOnContextMenuRequested(event -> contextMenu.show(tableView, event.getScreenX(), event.getScreenY()));
    }
}
