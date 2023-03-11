package com.example.bjb2.Views;

import com.example.bjb2.Controllers.ClientController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewFactory {
    private BorderPane salesView;
    private BorderPane langgananView;
    private AnchorPane stockView;
    private BorderPane penjualanView;
    private BorderPane setoranView;
    private final StringProperty clientSelectedMenuItem;

    public ViewFactory() {
        this.clientSelectedMenuItem = new SimpleStringProperty("");
    }

    public StringProperty getClientSelectedMenuItem() {
        return clientSelectedMenuItem;
    }
    public BorderPane getSalesView() {
        if (salesView == null) {
            try {salesView = new FXMLLoader(getClass().getResource("/com/example/bjb2/Sales.fxml")).load();}
            catch(Exception e) {e.printStackTrace();}
        }

        return salesView;
    }
    public BorderPane getLanggananView() {
        if (langgananView == null) {
            try {langgananView = new FXMLLoader(getClass().getResource("/com/example/bjb2/Langganan.fxml")).load();}
            catch(Exception e) {e.printStackTrace();}
        }
        return langgananView;
    }
    public AnchorPane getStockView() {
        if (stockView == null) {
            try { stockView = new FXMLLoader(getClass().getResource("/com/example/bjb2/Stock.fxml")).load(); }
            catch(Exception e) { e.printStackTrace(); }
        }
        return stockView;
    }
    public BorderPane getPenjualanView() {
        if (penjualanView == null) {
            try { penjualanView = new FXMLLoader(getClass().getResource("/com/example/bjb2/Penjualan.fxml")).load(); }
            catch(Exception e) { e.printStackTrace(); }
        }
        return penjualanView;
    }
    public BorderPane getSetoranView() {
        if (setoranView == null) {
            try { setoranView = new FXMLLoader(getClass().getResource("/com/example/bjb2/Setoran.fxml")).load(); }
            catch(Exception e) { e.printStackTrace(); }
        }
        return setoranView;
    }
    public void showClientWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bjb2/Client.fxml"));
        ClientController clientController = new ClientController();
        loader.setController(clientController);

        createStage(loader);
    }

    private void createStage(FXMLLoader loader) {
        Scene scene = null;
        try { scene = new Scene(loader.load()); } catch (Exception e) { e.printStackTrace(); }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("BJB2");
        stage.show();
    }
    public void closeStage(Stage stage) {
        stage.close();
    }
}
