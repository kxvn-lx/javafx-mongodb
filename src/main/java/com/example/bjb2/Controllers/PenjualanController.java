package com.example.bjb2.Controllers;

import com.example.Database.DAO.LanggananDAO;
import com.example.Database.DAO.PenjualanDAO;
import com.example.Database.Langganan;
import com.example.Database.Penjualan;
import com.example.Database.PenjualanStock;
import com.example.Database.Stock;
import com.example.bjb2.Controllers.Dialogs.AddPenjualanController;
import com.example.bjb2.Controllers.Dialogs.AddPenjualanStockController;
import com.example.bjb2.Controllers.Dialogs.AddStockController;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
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
        noFakturCol.setCellValueFactory(new PropertyValueFactory<>("noFaktur"));
        noSalesmanCol.setCellValueFactory(cd -> new SimpleStringProperty(Integer.toString(cd.getValue().getNoSalesman())));
        noLanggananCol.setCellValueFactory(cd -> {
                Penjualan p = cd.getValue();
                return new SimpleStringProperty(p.getNoLangganan());
            });
        tanggalCol.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        stocksCol.setCellValueFactory(cd -> new SimpleStringProperty(Integer.toString(cd.getValue().getPjs().length)));

        dao = new PenjualanDAO();
        dao.addListener(tableView);

        tableView.getItems().setAll(dao.get());

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
                dao.add(c.getPenjualan());
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
                dao.update(index, c.getPenjualan());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
