package com.example.bjb2.Controllers.Dialogs;

import com.example.Database.DAO.StockDAO;
import com.example.Database.PenjualanStock;
import com.example.Database.Stock;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import org.bson.types.ObjectId;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.example.bjb2.HelloApplication.print;

public class AddPenjualanStockController implements Initializable {
    @FXML private DialogPane dialogPane;
    @FXML private TextField kdStockTF;
    @FXML private Text namaText;
    @FXML private Text merekText;
    @FXML private TextField qtyTF;
    @FXML private Text satuanText;
    @FXML private Text hargaText;
    private StockDAO stockDAO;
    @FXML private ListView<Stock> listView;
    private PenjualanStock penjualanStock;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stockDAO = new StockDAO();
        dialogPane.lookupButton(ButtonType.OK).setDisable(true);
        dialogPane.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) { return; }
            listView.setItems(stockDAO.getAll());
        });

        applyTFsListeners();
        setupSuggestionsTF();
    }

    public PenjualanStock getPenjualanStock() {
        if (stockDAO.findByNo(kdStockTF.getText()).isPresent()) {
            return new PenjualanStock(penjualanStock == null ? penjualanStock.getId() : new ObjectId(), stockDAO.findByNo(kdStockTF.getText()).get(), Integer.parseInt(qtyTF.getText()));
        } else {
            throw new RuntimeException();
        }
    }
    public boolean isNull() {
        return kdStockTF.getText().isEmpty() || qtyTF.getText().isEmpty();
    }
    public void setTFs(PenjualanStock s) {
        dialogPane.setHeaderText("Rubah Barang Terjual");
        this.penjualanStock = s;
        kdStockTF.setText(s.getStock().getKode());
        qtyTF.setText(Integer.toString(s.getQty()));
    }

    private void applyTFsListeners() {
        TextField[] tfs = {kdStockTF, qtyTF};

        for (TextField tf : tfs) {
            tf.textProperty().addListener((observable, oldValue, newValue) -> validateForm());
        }


        qtyTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                qtyTF.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        kdStockTF.textProperty().addListener((observableValue, s, t1) -> {
            if (t1.isEmpty()) {
                return;
            }
            Optional<Stock> stock = stockDAO.findByNo(kdStockTF.getText());
            if (stock.isEmpty()) { return; }

            namaText.setText(stock.get().getNama());
            merekText.setText(stock.get().getMerek());
            hargaText.setText(Integer.toString(stock.get().getHarga()));
            satuanText.setText(stock.get().getSatuan());
        });

        kdStockTF.focusedProperty().addListener((observableValue, s, t1) -> {
            if (!t1) {
                Optional<Stock> stock = stockDAO.findByNo(kdStockTF.getText());
                if (stock.isEmpty()) { return; }

                namaText.setText(stock.get().getNama());
                merekText.setText(stock.get().getMerek());
                hargaText.setText(Integer.toString(stock.get().getHarga()));
                satuanText.setText(stock.get().getSatuan());
            }
        });
    }
    private void setupSuggestionsTF() {
        listView.setItems(stockDAO.getAll());
        listView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Stock> call(ListView<Stock> stockListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Stock s, boolean empty) {
                        super.updateItem(s, empty);
                        if (s == null || empty) {
                            setText(null);
                        } else {
                            setText(s.getKode() + ":" + s.getNama());
                        }
                    }
                };
            }
        });

        kdStockTF.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue.length() > 0) {
                ObservableList<Stock> filteredList = FXCollections.observableArrayList();
                for (Stock stock : stockDAO.getAll()) {
                    if (stock.getKode().toLowerCase().startsWith(newValue.toLowerCase())) {
                        filteredList.add(stock);
                    }
                }
                listView.setItems(filteredList);
            } else {
                listView.setItems(stockDAO.getAll());
            }
        });

        listView.setOnMouseClicked(mouseEvent -> {
            kdStockTF.setText(listView.getSelectionModel().getSelectedItem().getKode());
            namaText.setText(listView.getSelectionModel().getSelectedItem().getNama());
            merekText.setText(listView.getSelectionModel().getSelectedItem().getMerek());
            hargaText.setText(Integer.toString(listView.getSelectionModel().getSelectedItem().getHarga()));
            satuanText.setText(listView.getSelectionModel().getSelectedItem().getSatuan());
        });
    }
    private void validateForm() {
        dialogPane.lookupButton(ButtonType.OK).setDisable(isNull());
    }
}
