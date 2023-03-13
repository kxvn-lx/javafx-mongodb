package com.example.bjb2.Controllers;

import com.example.Database.Langganan;
import com.example.Database.DAO.LanggananDAO;
import com.example.bjb2.Controllers.Dialogs.AddLanggananController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class LanggananController implements Initializable {
    @FXML private TableView<Langganan> tableView;
    @FXML private TableColumn<Langganan, String> noLanggananCol;
    @FXML private TableColumn<Langganan, String> namaCol;
    @FXML private TableColumn<Langganan, String> alamatCol;
    @FXML private Button rubahBtn;
    @FXML private Button hapusBtn;
    private LanggananDAO dao;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        noLanggananCol.setCellValueFactory(new PropertyValueFactory<>("no_langganan"));
        namaCol.setCellValueFactory(new PropertyValueFactory<>("nama"));
        alamatCol.setCellValueFactory(new PropertyValueFactory<>("alamat"));

        dao = new LanggananDAO();
        dao.addListener(tableView);

        tableView.getItems().setAll(dao.getAll());

        applyTableViewListeners();
        setupContextMenu();

    }

    /* EVENT LISTENERS */
    public void handleTambahBtn() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/example/bjb2/AddLanggananDialog.fxml"));
            DialogPane addLanggananPane = fxmlLoader.load();

            AddLanggananController c = fxmlLoader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(addLanggananPane);
            dialog.setTitle("Form Tambah Langganan");

            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.get() == ButtonType.OK) {
                dao.add(c.getLangganan());
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleRubahBtn() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/example/bjb2/AddLanggananDialog.fxml"));
            DialogPane addLanggananPane = fxmlLoader.load();
            addLanggananPane.setHeaderText("Rubah Langganan");

            AddLanggananController c = fxmlLoader.getController();
            // Get the selection model
            TableView.TableViewSelectionModel<Langganan> selectionModel = tableView.getSelectionModel();
            // Get the selected item
            Langganan selectedItem = selectionModel.getSelectedItem();
            int index = selectionModel.getSelectedIndex();

            c.setTFs(selectedItem);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(addLanggananPane);
            dialog.setTitle("Form Rubah Langganan");

            tableView.getSelectionModel().clearSelection();
            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.get() == ButtonType.OK) {
                dao.update(index, c.getLangganan());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleHapusBtn() {
        // Get the selection model
        TableView.TableViewSelectionModel<Langganan> selectionModel = tableView.getSelectionModel();
        // Get the selected item
        Langganan selectedItem = selectionModel.getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi penghapusan Langganan");
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
        TableView.TableViewSelectionModel<Langganan> selectionModel = tableView.getSelectionModel();
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
