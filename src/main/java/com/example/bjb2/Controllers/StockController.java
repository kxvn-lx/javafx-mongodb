package com.example.bjb2.Controllers;

import com.example.Database.Models.Langganan;
import com.example.Database.Models.Stock;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private final ObservableList<Stock> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        kodeCol.setCellValueFactory(new PropertyValueFactory<>("kode"));
        namaCol.setCellValueFactory(new PropertyValueFactory<>("nama"));
        merekCol.setCellValueFactory(new PropertyValueFactory<>("merek"));
        hargaCol.setCellValueFactory(new PropertyValueFactory<>("harga"));
        satuanCol.setCellValueFactory(new PropertyValueFactory<>("satuan"));

        initDummyData();
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
                tableView.getItems().add(c.getStock());
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
                tableView.getItems().set(index, c.getStock());
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
                tableView.getItems().remove(selectedItem);
            }

            tableView.getSelectionModel().clearSelection();
        });
    }

    /* PRIVATE METHODS */
    private void initDummyData() {
        List<Stock> list = new ArrayList<>(
                Arrays.asList(
                    new Stock("HD15BP", "Tas 15 /50/20L Ramah Lingkungan", "BAJA", 1011000, "Karung"),
                        new Stock("PE820", "PE 8x20 @0.2KG/PAK", "Hijau Daun", 360000, "Rol"),
                        new Stock("PP1000", "PP 1KG 15X27 @5PAK @10KG", "Hijau Daun", 350000, "Kg"),
                        new Stock("PP6", "PP Rol 6x0.3 @ 30ROL", "Malaikat jatuh", 297000, "Rol"),
                        new Stock("PP1500", "PE 1 1/2 KG 18x30 @5PAK @10KG", "Hijau Daun", 350000, "Rol"),
                        new Stock("PE2548", "PE 25x48x0.5 / 5 PAK @10KG", "Hijau Daun", 355000, "Rol"),
                        new Stock("PE1045", "PE 10x45x0.3 / 5 PAK @10KG @0.2KG/PAK", "Hijau Daun", 350000, "Rol"),
                        new Stock("PP1400", "PP 1/4 KG 10x17 @5PAK @10KG", "Ramayana", 355000, "Rol"),
                        new Stock("PE1227", "PE 12x27x0.3 / 5 PAK @10KG", "Hijau Daun", 355000, "Rol"),
                        new Stock("PP5000", "PE 1/2 KG 12x22 @ 5PAK @10KG", "Hijau Daun", 300000, "Rol")
                )
        );
        data.setAll(list);
        tableView.getItems().setAll(data);
    }
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
