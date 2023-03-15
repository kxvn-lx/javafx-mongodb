package com.example.bjb2.Controllers;

import com.example.Database.DAO.PenjualanDAO;
import com.example.Database.Penjualan;
import com.example.bjb2.Controllers.Dialogs.SetoranFormController;
import com.example.bjb2.Views.VFModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
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
        // Setup listener whenever user went to Setoran screen
        VFModel.getInstance().getVF().getClientSelectedMenuItem().addListener((observableValue, oldVal, newVal) -> {
            if (newVal.equals("Setoran")) {
                getNotPaid();
            }
        });
        setupContextMenu();
        applyListeners();
    }

    public void handleRubahBtn() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/example/bjb2/SetoranFormDialog.fxml"));
            DialogPane pane = fxmlLoader.load();
            pane.setHeaderText("Form Data Setoran Penjualan");

            SetoranFormController c = fxmlLoader.getController();
            // Get the selection model
            TableView.TableViewSelectionModel<Penjualan> selectionModel = tableView.getSelectionModel();
            // Get the selected item
            Penjualan selectedItem = selectionModel.getSelectedItem();

            c.setTFs(selectedItem);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(pane);
            dialog.setTitle("Form Data Setoran Penjualan");

            tableView.getSelectionModel().clearSelection();
            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.get() == ButtonType.OK) {
                dao.update(c.getUpdatedPenjualan());
                getNotPaid();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupTVCellValueFactory() {
        noFakturCol.setCellValueFactory(new PropertyValueFactory<>("noFaktur"));
        jumlahCol.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        setoranCol.setCellValueFactory(new PropertyValueFactory<>("setoran"));
    }
    private void getNotPaid() {
        List<Penjualan> allList = dao.getAll();
        // By Default, get just not paid
        List<Penjualan> notPaidList = allList.stream()
                .filter(penjualan -> penjualan.getSetoran() < penjualan.getJumlah())
                .collect(Collectors.toList());

        tableView.getItems().setAll(notPaidList);
    }
    private void getPaid() {
        List<Penjualan> allList = dao.getAll();
        List<Penjualan> paidList = allList.stream()
                .filter(penjualan -> penjualan.getJumlah() == penjualan.getSetoran() || penjualan.getSetoran() >= penjualan.getJumlah())
                .collect(Collectors.toList());
        tableView.getItems().setAll(paidList);
    }
    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Batal Memilih");
        menuItem1.setOnAction(event -> {
            tableView.getSelectionModel().clearSelection();
        });
        contextMenu.getItems().addAll(menuItem1);

        tableView.setContextMenu(contextMenu);
        tableView.setOnContextMenuRequested(event -> {
            contextMenu.show(tableView, event.getScreenX(), event.getScreenY());
        });
    }
    private void applyListeners() {
        // Listen to selected row
        TableView.TableViewSelectionModel<Penjualan> selectionModel = tableView.getSelectionModel();
        // Add a listener to the selection model
        selectionModel.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            rubahBtn.setDisable(newValue == null);
        });
    }
}
