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
import java.util.List;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        langgananDAO = new LanggananDAO();
    }


    public void setTFs(Penjualan p) {
        this.p = p;
        noFakturTF.setText(Integer.toString(p.getNoFaktur()));
        if (langgananDAO.find(p.getNoLangganan()).isPresent()) {
            Langganan l = langgananDAO.find(p.getNoLangganan()).get();
            noLanggananText.setText(l.getNo_langganan());
            namaLanggananText.setText(l.getNama());
        }
        tanggalTF.setText(p.getTanggal());
        setoranText.setText(Integer.toString(p.getJumlah()));
        jumlahDepositText.setText(Integer.toString(p.getSetoran()));
        sisaText.setText(Integer.toString(p.getJumlah() - p.getSetoran()));
    }

}
