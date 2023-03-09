package com.example.bjb2.Controllers.Dialogs;

import com.example.Database.*;
import com.example.Database.DAO.LanggananDAO;
import com.example.Database.DAO.PenjualanDAO;
import com.example.Database.DAO.SalesDAO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AddPenjualanController implements Initializable {
    @FXML private TextField noFakturTF;
    @FXML private TextField noSalesmanTF;
    @FXML private Text salesman_namaText;
    @FXML private TextField noLanggananTF;
    @FXML private Text langganan_namaText;
    @FXML private TextField tanggalTF;
    @FXML private TextField statusTF;
    @FXML private TextField kreditTF;
    @FXML private HBox kreditHBox;
    @FXML private DialogPane dialogPane;
    @FXML private TableColumn<PenjualanStock, String> kdStockCol;
    @FXML private TableColumn<PenjualanStock, String> namaStockCol;
    @FXML private TableColumn<PenjualanStock, String> qtyCol;
    @FXML private TableColumn<PenjualanStock, String> hargaCol;
    @FXML private TableColumn<PenjualanStock, String> jumlahCol;
    @FXML private TableView<PenjualanStock> tableView;
    @FXML private Button rubahBtn;
    @FXML private Button hapusBtn;
    private PenjualanDAO penjualanDAO;
    private SalesDAO salesDAO;
    private LanggananDAO langgananDAO;
    private ObjectProperty<Optional<Penjualan>> p = new SimpleObjectProperty<>(Optional.empty());;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        penjualanDAO = new PenjualanDAO();
        salesDAO = new SalesDAO();
        langgananDAO = new LanggananDAO();
        dialogPane.lookupButton(ButtonType.OK).setDisable(true);

        // create a change listener to monitor the optional value
        p.addListener((observable, oldValue, newValue) -> {
            if (newValue.isPresent()) {
                updateWhenPFound();
            }
        });


        applyTFsListeners();
        applyColCellFactory();

        // Listen to tableView updates
        tableView.getItems().addListener((ListChangeListener.Change<? extends PenjualanStock> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<? extends PenjualanStock> addedItems = change.getAddedSubList();
                    System.out.println("New row(s) added: " + addedItems);
                }
            }
        });
    }

    public void handleTambahBtn() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/example/bjb2/AddPenjualanStock.fxml"));
            DialogPane pane = fxmlLoader.load();

            AddPenjualanStockController c = fxmlLoader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(pane);
            dialog.setTitle("Form Barang Terjual");

            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.get() == ButtonType.OK) {
                tableView.getItems().add(c.getPenjualanStock());
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleRubahBtn(ActionEvent event) {
//        try {
//            FXMLLoader fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(getClass().getResource("/com/example/bjb2/AddStockDialog.fxml"));
//            DialogPane pane = fxmlLoader.load();
//            pane.setHeaderText("Rubah Stock");
//
//            AddStockController c = fxmlLoader.getController();
//            // Get the selection model
//            TableView.TableViewSelectionModel<Stock> selectionModel = tableView.getSelectionModel();
//            // Get the selected item
//            Stock selectedItem = selectionModel.getSelectedItem();
//            int index = selectionModel.getSelectedIndex();
//
//            c.setTFs(selectedItem);
//
//            Dialog<ButtonType> dialog = new Dialog<>();
//            dialog.setDialogPane(pane);
//            dialog.setTitle("Form Rubah Stock");
//
//            tableView.getSelectionModel().clearSelection();
//            Optional<ButtonType> clickedButton = dialog.showAndWait();
//            if (clickedButton.get() == ButtonType.OK) {
//                dao.update(index, c.getStock());
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
    public void handleHapusBtn(ActionEvent event) {
        // Get the selection model
//        TableView.TableViewSelectionModel<Stock> selectionModel = tableView.getSelectionModel();
//        // Get the selected item
//        Stock selectedItem = selectionModel.getSelectedItem();
//
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Konfirmasi penghapusan Stock");
//        alert.setHeaderText("Anda yakin akan menghapus: " + selectedItem.toString());
//
//        // show the dialog and wait for a response
//        alert.showAndWait().ifPresent(response -> {
//            if (response == ButtonType.OK) {
//                dao.delete(selectedItem);
//            }
//
//            tableView.getSelectionModel().clearSelection();
//        });
    }

    public boolean isNull() {
        return noFakturTF.getText().isEmpty() || noSalesmanTF.getText().isEmpty() || noLanggananTF.getText().isEmpty() || tanggalTF.getText().isEmpty() || statusTF.getText().isEmpty() || tableView.getItems().isEmpty();
    }

    public Penjualan getPenjualan() {
        List<PenjualanStock> pjs = tableView.getItems();

        return new Penjualan(Integer.parseInt(noFakturTF.getText()), Integer.parseInt(noSalesmanTF.getText()), noLanggananTF.getText(), tanggalTF.getText(), Status.get(statusTF.getText()), (PenjualanStock[]) pjs.toArray());
    }

    private void applyColCellFactory() {
        kdStockCol.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getStock().getKode()));
        namaStockCol.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getStock().getNama()));
        qtyCol.setCellValueFactory(cd -> new SimpleStringProperty(Integer.toString(cd.getValue().getQty())));
        hargaCol.setCellValueFactory(cd -> new SimpleStringProperty(Integer.toString(cd.getValue().getStock().getHarga())));
        jumlahCol.setCellValueFactory(cd -> {
            PenjualanStock pj = cd.getValue();
            Integer jumlah = pj.getStock().getHarga() * pj.getQty();
            return new SimpleStringProperty(Integer.toString(jumlah));
        });
    }

    private void applyTFsListeners() {
        TextField[] tfs = {noFakturTF, noSalesmanTF, noLanggananTF, tanggalTF, statusTF};
        for (TextField tf : tfs) {
            tf.textProperty().addListener((observable, oldValue, newValue) -> validateForm());
        }

        // Listen to NoFaktur textfield
        List<Penjualan> penjualanList = penjualanDAO.get();
        noFakturTF.textProperty().addListener(((observableValue, s, t1) -> {
            if (noFakturTF.getText().isEmpty()) return;

            List<Penjualan> filtered = penjualanList.stream()
                    .filter(penjualan -> penjualan.getNoFaktur() == Integer.parseInt(noFakturTF.getText()))
                    .collect(Collectors.toList());
            if (filtered.size() != 0) p.setValue(Optional.ofNullable(filtered.get(0)));
            else {
                noSalesmanTF.setText("");
                noLanggananTF.setText("");
                tanggalTF.setText("");
                statusTF.setText("");
            }

            if (p.get().isPresent()) {
                noSalesmanTF.setText(Integer.toString(p.get().get().getNoSalesman()));
                noLanggananTF.setText(p.get().get().getNoLangganan());
                tanggalTF.setText(p.get().get().getTanggal());
                statusTF.setText(p.get().get().getStatus());

            }
        }));

        noSalesmanTF.textProperty().addListener(((observableValue, s, t1) -> {
            Optional<Salesman> salesman = salesDAO.find(Integer.parseInt(t1));
            if (salesman.isPresent()) {
                salesman_namaText.setText(salesman.get().getNama());
            } else {
                salesman_namaText.setText("");
            }
        }));

        noLanggananTF.textProperty().addListener(((observableValue, s, t1) -> {
            Optional<Langganan> l = langgananDAO.find(noLanggananTF.getText());
            if (l.isPresent()) {
                langganan_namaText.setText(l.get().getNama());
            } else {
                langganan_namaText.setText("");
            }
        }));
    }

    /**
     * Update the UI when Penjualan is found
     */
    private void updateWhenPFound() {
        Optional<Salesman> s = salesDAO.find(p.get().get().getNoSalesman());
        salesman_namaText.setText(s.get().getNama());

        Optional<Langganan> l = langgananDAO.find(p.get().get().getNoLangganan());
        langganan_namaText.setText(l.get().getNama());

    }


    private void validateForm() {
        dialogPane.lookupButton(ButtonType.OK).setDisable(isNull());
    }
}
