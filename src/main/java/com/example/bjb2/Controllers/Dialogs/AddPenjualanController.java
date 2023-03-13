package com.example.bjb2.Controllers.Dialogs;

import com.example.Database.*;
import com.example.Database.Connection.MongoDBConnection;
import com.example.Database.DAO.LanggananDAO;
import com.example.Database.DAO.PenjualanDAO;
import com.example.Database.DAO.SalesDAO;
import com.example.Utils.DateGenerator;
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
import org.bson.types.ObjectId;

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
    @FXML private TextField kreditTF; //TODO: finish implementing kreditTF
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
    @FXML private ListView<Object> suggestionListView;
    private PenjualanDAO penjualanDAO;
    private SalesDAO salesDAO;
    private LanggananDAO langgananDAO;
    private final ObjectProperty<Optional<Penjualan>> optionalPenjualan = new SimpleObjectProperty<>(Optional.empty());;
    private final NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        penjualanDAO = new PenjualanDAO();
        salesDAO = new SalesDAO();
        langgananDAO = new LanggananDAO();
        dialogPane.lookupButton(ButtonType.OK).setDisable(true);
        setupContextMenu();

        // Get the latest noFaktur and increment by one
        List<Penjualan> allPs = penjualanDAO.getAll();
        Penjualan latestPenjualan = allPs.stream()
                .max(Comparator.comparingInt(Penjualan::getNoFaktur))
                .orElse(null);

        assert latestPenjualan != null;
        noFakturTF.setText(Integer.toString(latestPenjualan.getNoFaktur() + 1));

        // Set to today's date
        tanggalTF.setText(DateGenerator.getCurrentDate());

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
            if (clickedButton.isPresent() && clickedButton.get() == ButtonType.OK) {
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
            dialog.setTitle("Form Rubah Barang Terjual");

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
        alert.setTitle("Konfirmasi penghapusan barang yang terjual");
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
        return new Penjualan(
                optionalPenjualan.get().isPresent() ? optionalPenjualan.get().get().getId() : new ObjectId(),
                Integer.parseInt(noFakturTF.getText().trim()),
                Integer.parseInt(noSalesmanTF.getText().trim()),
                noLanggananTF.getText().toUpperCase().trim(),
                tanggalTF.getText().trim(),
                statusCB.getValue(),
                pjs.toArray(new PenjualanStock[0]),
                calculateJumlah(),
                optionalPenjualan.get().isPresent() ? optionalPenjualan.get().get().getSetoran() : 0
        );
    }
    public void setTFs(Penjualan p) {
        dialogPane.setHeaderText("Rubah Penjualan");

        this.optionalPenjualan.setValue(Optional.ofNullable(p));

        noFakturTF.setText(Integer.toString(p.getNoFaktur()));
        noSalesmanTF.setText(Integer.toString(p.getNoSalesman()));
        noLanggananTF.setText(p.getNoLangganan());
        tanggalTF.setText(p.getTanggal());
        statusCB.getSelectionModel().select(p.getStatus());
        tableView.getItems().setAll(p.getPjs());
        totalText.setText(formatRupiah.format(Double.parseDouble(Integer.toString(p.getJumlah()))));
    }

    private void applyColCellFactory() {
        kdStockCol.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getStock().getKode()));
        namaStockCol.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getStock().getNama()));
        qtyCol.setCellValueFactory(cd -> new SimpleStringProperty(Integer.toString(cd.getValue().getQty())));
        hargaCol.setCellValueFactory(cd -> new SimpleStringProperty(Integer.toString(cd.getValue().getStock().getHarga())));
        jumlahCol.setCellValueFactory(cd -> {
            PenjualanStock pj = cd.getValue();
            int jumlah = pj.getStock().getHarga() * pj.getQty();
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
        optionalPenjualan.addListener((observable, oldValue, newValue) -> {
            if (newValue.isPresent()) {
                updateWhenPFound();
            }
        });

        // Listen to tableView updates
        tableView.getItems().addListener((ListChangeListener.Change<? extends PenjualanStock> change) -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved() || change.wasReplaced()) {
                    validateForm();


                    totalText.setText(formatRupiah.format(Double.parseDouble(Integer.toString(calculateJumlah()))));
                }
            }
        });
        // Listen to selected row
        TableView.TableViewSelectionModel<PenjualanStock> selectionModel = tableView.getSelectionModel();
        // Add a listener to the selection model
        selectionModel.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            rubahBtn.setDisable(newValue == null);
            hapusBtn.setDisable(newValue == null);
        });

        // Listen to NoFaktur textfield
        List<Penjualan> penjualanList = penjualanDAO.getAll();
        noFakturTF.textProperty().addListener(((observableValue, s, t1) -> {
            if (noFakturTF.getText().isEmpty()) {
                noSalesmanTF.setText("");
                noLanggananTF.setText("");
                tanggalTF.setText(DateGenerator.getCurrentDate());
                return;
            };

            List<Penjualan> filtered = penjualanList.stream()
                    .filter(penjualan -> penjualan.getNoFaktur() == Integer.parseInt(noFakturTF.getText()))
                    .collect(Collectors.toList());
            if (!filtered.isEmpty()) optionalPenjualan.setValue(Optional.ofNullable(filtered.get(0)));
            else {
                noSalesmanTF.setText("");
                noLanggananTF.setText("");
                tanggalTF.setText(DateGenerator.getCurrentDate());
                statusCB.getSelectionModel().selectFirst();
            }

            if (optionalPenjualan.get().isPresent()) {
                noSalesmanTF.setText(Integer.toString(optionalPenjualan.get().get().getNoSalesman()));
                noLanggananTF.setText(optionalPenjualan.get().get().getNoLangganan());
                tanggalTF.setText(optionalPenjualan.get().get().getTanggal());
                statusCB.getSelectionModel().select(optionalPenjualan.get().get().getStatus());

            }
        }));

        noSalesmanTF.textProperty().addListener(((observableValue, s, t1) -> {
            if (t1.isEmpty() || !isInteger(t1)) {
                salesman_namaText.setText("");
                return;
            }
            Optional<Salesman> salesman = salesDAO.findByNo(t1);
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
            List<Salesman> arr = salesDAO.findContains(t1);
            suggestionListView.getItems().clear();
            for (Salesman salesman: arr) {
                suggestionListView.getItems().add(salesman);
            }
        });

        noLanggananTF.textProperty().addListener(((observableValue, s, t1) -> {
            if (t1.isEmpty()) {
                langganan_namaText.setText("");
                return;
            }
            Optional<Langganan> l = langgananDAO.findByNo(t1);
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


            List<Langganan> arr = langgananDAO.findContains(t1);
            suggestionListView.getItems().clear();
            for (Langganan l: arr) {
                suggestionListView.getItems().add(l);
            }
        });

        suggestionListView.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
            if (t1 == null) { return; }
            if (t1 instanceof Salesman) {
                Salesman s = (Salesman) t1;
                if (!noSalesmanTF.getText().equals(Integer.toString(s.getNo_salesman()))) {
                    noSalesmanTF.setText(Integer.toString(s.getNo_salesman()));
                }
            } else if (t1 instanceof Langganan) {
                Langganan l = (Langganan) t1;
                if (!noLanggananTF.getText().equals(l.getNo_langganan())) {
                    noLanggananTF.setText(l.getNo_langganan());
                }
            }
            suggestionListView.getItems().clear();
            suggestionListView.setFocusTraversable(false);
        });

        // statusCB listener
        statusCB.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, status, t1) -> kreditHBox.setVisible(t1 == Status.K)
        );
    }
    /**
     * Update the UI when Penjualan is found after typing on NoFaktur
     */
    private void updateWhenPFound() {
        Optional<Salesman> s = salesDAO.findByNo(Integer.toString(optionalPenjualan.get().get().getNoSalesman()));
        s.ifPresent(salesman -> salesman_namaText.setText(salesman.getNama()));

        Optional<Langganan> l = langgananDAO.findByNo(optionalPenjualan.get().get().getNoLangganan());
        l.ifPresent(langganan -> langganan_namaText.setText(langganan.getNama()));

        optionalPenjualan.get().ifPresent(penjualan -> tableView.getItems().setAll(penjualan.getPjs()));
    }
    private int calculateJumlah() {
        int sum = 0;
        for (int i = 0; i < tableView.getItems().size(); i++) {
            sum += tableView.getItems().get(i).getQty() * tableView.getItems().get(i).getStock().getHarga(); // Replace getColumn1 with the appropriate method to get the value in the desired column
        }
        return sum;
    }
    private void validateForm() {
        dialogPane.lookupButton(ButtonType.OK).setDisable(isNull());
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
    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
