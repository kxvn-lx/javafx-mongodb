package com.example.bjb2.Controllers.Dialogs;

import com.example.Database.DAO.LanggananDAO;
import com.example.Database.Langganan;
import com.example.Database.Penjualan;
import com.example.Database.PenjualanStock;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class SetoranFormController implements Initializable {
    @FXML private DialogPane dialogPane;
    @FXML private TextField noFakturTF;
    @FXML private Text noLanggananText;
    @FXML private Text namaLanggananText;
    @FXML private TextField tanggalTF;
    @FXML private Text setoranText;
    @FXML private Text jumlahDepositText;
    @FXML private Text sisaText;
    @FXML private TextField setorSekarangTF;
    @FXML private Text sisaSetorSekarangText;
    private Penjualan p;
    private LanggananDAO langgananDAO;
    private final NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        langgananDAO = new LanggananDAO();
        applyListeners();
    }


    public void setTFs(Penjualan p) {
        this.p = p;
        noFakturTF.setText(Integer.toString(p.getNoFaktur()));
        if (langgananDAO.findByNo(p.getNoLangganan()).isPresent()) {
            Langganan l = langgananDAO.findByNo(p.getNoLangganan()).get();
            noLanggananText.setText(l.getNo_langganan());
            namaLanggananText.setText(l.getNama());
        }
        tanggalTF.setText(p.getTanggal());
        setoranText.setText(formatRupiah.format(Double.parseDouble(Integer.toString(p.getJumlah()))));
        jumlahDepositText.setText(formatRupiah.format(Double.parseDouble(Integer.toString(p.getSetoran()))));
        sisaText.setText(formatRupiah.format(p.getJumlah() - p.getSetoran()));
    }
    public Penjualan getUpdatedPenjualan() {
        p.setSetoran(Integer.parseInt(setorSekarangTF.getText()));
        return p;
    }

    private void applyListeners() {
        // force the field to be numeric only
        setorSekarangTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                setorSekarangTF.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        // Update values
        setorSekarangTF.textProperty().addListener((observableValue, s, t1) -> {
            if (t1.isEmpty()) return;

            try {
                int sisa = formatRupiah.parse(sisaText.getText()).intValue();

                int sisaSetorSekarang = sisa - Integer.parseInt(t1);
                sisaSetorSekarangText.setText(formatRupiah.format(sisaSetorSekarang));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
