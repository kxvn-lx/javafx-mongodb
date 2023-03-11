package com.example.bjb2.Controllers.Dialogs;

import com.example.Database.*;
import com.example.Database.DAO.LanggananDAO;
import com.example.Database.DAO.PenjualanDAO;
import com.example.Database.DAO.SalesDAO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

public class AddPenjualanController implements Initializable {
    @FXML private TextField noFakturTF;
    @FXML private TextField noSalesmanTF;
    @FXML private Text salesman_namaText;
    @FXML private TextField noLanggananTF;
    @FXML private Text langganan_namaText;
    @FXML private TextField tanggalTF;
    @FXML private ChoiceBox<Status> statusCB;
    @FXML private TextField kreditTF;
    @FXML private HBox kreditHBox;
    @FXML private Text totalText;
    @FXML private DialogPane dialogPane;
    @FXML private TableColumn<PenjualanStock, String> kdStockCol;
    @FXML private TableColumn<PenjualanStock, String> namaStockCol;
    @FXML private TableColumn<PenjualanStock, String> qtyCol;
    @FXML private TableColumn<PenjualanStock, String> hargaCol;
    @FXML private TableColumn<PenjualanStock, String> jumlahCol;
    @FXML private TableView<PenjualanStock> tableView;
    @FXML private Button rubahBtn;
    @FXML private Button hapusBtn;
    @FXML private ListView<String> suggestionListView;
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
        setupContextMenu();

        // Get the latest noFaktur and increment by one
        List<Penjualan> allPs = penjualanDAO.get();
        Penjualan latestPenjualan = allPs.stream()
                .max(Comparator.comparingInt(Penjualan::getNoFaktur))
                .orElse(null);

        assert latestPenjualan != null;
        noFakturTF.setText(Integer.toString(latestPenjualan.getNoFaktur() + 1));

        // Status Choice Box
        statusCB.getItems().setAll(Status.values());
        statusCB.getSelectionModel().selectFirst();

        applyListeners();
        applyColCellFactory();
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
    public void handleRubahBtn() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/example/bjb2/AddPenjualanStock.fxml"));
            DialogPane pane = fxmlLoader.load();
            pane.setHeaderText("Rubah Barang Terjual");

            AddPenjualanStockController c = fxmlLoader.getController();
            // Get the selection model
            TableView.TableViewSelectionModel<PenjualanStock> selectionModel = tableView.getSelectionModel();
            // Get the selected item
            PenjualanStock selectedItem = selectionModel.getSelectedItem();
            c.setTFs(selectedItem);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(pane);
            dialog.setTitle("Form Rubah Stock");

            tableView.getSelectionModel().clearSelection();
            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.get() == ButtonType.OK) {
                tableView.getItems().replaceAll(penjualanStock ->
                        penjualanStock.getStock().getKode().equals(c.getPenjualanStock().getStock().getKode()) ?
                                c.getPenjualanStock() : penjualanStock
                        );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleHapusBtn() {
        // Get the selection model
        TableView.TableViewSelectionModel<PenjualanStock> selectionModel = tableView.getSelectionModel();
        // Get the selected item
        PenjualanStock selectedItem = selectionModel.getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi penghapusan stock");
        alert.setHeaderText("Anda yakin akan menghapus: " + selectedItem.toString());

        // show the dialog and wait for a response
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                tableView.getItems().remove(selectedItem);
            }

            tableView.getSelectionModel().clearSelection();
        });
    }
    public boolean isNull() {
        return noFakturTF.getText().isEmpty() || noSalesmanTF.getText().isEmpty() || noLanggananTF.getText().isEmpty() || tanggalTF.getText().isEmpty() || tableView.getItems().isEmpty();
    }
    public Penjualan getPenjualan() {
        List<PenjualanStock> pjs = tableView.getItems();
        return new Penjualan(Integer.parseInt(noFakturTF.getText()), Integer.parseInt(noSalesmanTF.getText()), noLanggananTF.getText().toUpperCase(), tanggalTF.getText(), statusCB.getValue(), pjs.toArray(new PenjualanStock[0]));
    }
    public void setTFs(Penjualan p) {
        dialogPane.setHeaderText("Rubah Penjualan");

        noFakturTF.setText(Integer.toString(p.getNoFaktur()));
        noSalesmanTF.setText(Integer.toString(p.getNoSalesman()));
        noLanggananTF.setText(p.getNoLangganan());
        tanggalTF.setText(p.getTanggal());
        statusCB.getSelectionModel().select(p.getStatus());
        tableView.getItems().setAll(p.getPjs());
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
    private void applyListeners() {
        // Major validation
        TextField[] tfs = {noFakturTF, noSalesmanTF, noLanggananTF, tanggalTF};
        for (TextField tf : tfs) {
            tf.textProperty().addListener((observable, oldValue, newValue) -> validateForm());
        }

        // create a change listener to monitor the optional value
        p.addListener((observable, oldValue, newValue) -> {
            if (newValue.isPresent()) {
                updateWhenPFound();
            }
        });

        // Listen to tableView updates
        tableView.getItems().addListener((ListChangeListener.Change<? extends PenjualanStock> change) -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved() || change.wasReplaced()) {
                    validateForm();

                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
                    totalText.setText(formatRupiah.format(Double.parseDouble(calculateJumlah())));
                }
            }
        });

        // Listen to NoFaktur textfield
        List<Penjualan> penjualanList = penjualanDAO.get();
        noFakturTF.textProperty().addListener(((observableValue, s, t1) -> {
            if (noFakturTF.getText().isEmpty()) {
                noSalesmanTF.setText("");
                noLanggananTF.setText("");
                tanggalTF.setText("");
                return;
            };

            List<Penjualan> filtered = penjualanList.stream()
                    .filter(penjualan -> penjualan.getNoFaktur() == Integer.parseInt(noFakturTF.getText()))
                    .collect(Collectors.toList());
            if (filtered.size() != 0) p.setValue(Optional.ofNullable(filtered.get(0)));
            else {
                noSalesmanTF.setText("");
                noLanggananTF.setText("");
                tanggalTF.setText("");
                statusCB.getSelectionModel().selectFirst();
            }

            if (p.get().isPresent()) {
                noSalesmanTF.setText(Integer.toString(p.get().get().getNoSalesman()));
                noLanggananTF.setText(p.get().get().getNoLangganan());
                tanggalTF.setText(p.get().get().getTanggal());
                statusCB.getSelectionModel().select(p.get().get().getStatus());

            }
        }));

        noSalesmanTF.textProperty().addListener(((observableValue, s, t1) -> {
            if (t1.isEmpty() || !isInteger(t1)) {
                salesman_namaText.setText("");
                return;
            }
            Optional<Salesman> salesman = salesDAO.find(Integer.parseInt(t1));
            if (salesman.isPresent()) {
                salesman_namaText.setText(salesman.get().getNama());
            } else {
                salesman_namaText.setText("");
            }
        }));
        // Salesman suggestion box
        noSalesmanTF.textProperty().addListener((observableValue, s, t1) -> {
            if (t1.isEmpty()) {
                suggestionListView.getItems().clear();
                return;
            }
            List<Salesman> arr = salesDAO.findByN(t1);
            suggestionListView.getItems().clear();
            for (Salesman salesman: arr) {
                suggestionListView.getItems().add(salesman.toString());
            }
        });

        noLanggananTF.textProperty().addListener(((observableValue, s, t1) -> {
            if (t1.isEmpty()) {
                langganan_namaText.setText("");
                return;
            }
            Optional<Langganan> l = langgananDAO.find(t1);
            if (l.isPresent()) {
                langganan_namaText.setText(l.get().getNama());
            } else {
                langganan_namaText.setText("");
            }
        }));
        noLanggananTF.textProperty().addListener((observableValue, s, t1) -> {
            if (t1.isEmpty()) {
                suggestionListView.getItems().clear();
                return;
            }
            List<Langganan> arr = langgananDAO.findByN(t1);
            suggestionListView.getItems().clear();
            for (Langganan l: arr) {
                suggestionListView.getItems().add(l.toString());
            }
        });

        suggestionListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                System.out.println(suggestionListView.getSelectionModel().getSelectedItem());
            }
        });

        // statusCB listener
        statusCB.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, status, t1) -> {
                    kreditHBox.setVisible(t1 == Status.K);
                }
        );
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
    private String calculateJumlah() {
        int sum = 0;
        for (int i = 0; i < tableView.getItems().size(); i++) {
            sum += tableView.getItems().get(i).getQty() * tableView.getItems().get(i).getStock().getHarga(); // Replace getColumn1 with the appropriate method to get the value in the desired column
        }
        return Integer.toString(sum);
    }
    private void validateForm() {
        dialogPane.lookupButton(ButtonType.OK).setDisable(isNull());
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
    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
