package com.example.Database.DAO;

import com.example.Database.Stock;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class StockDAO {
    private final ObservableList<Stock> data = FXCollections.observableArrayList();

    public StockDAO() {
        data.setAll(fetchFromMongo());
    }

    public void addListener(TableView tv) {
        data.addListener((ListChangeListener<Stock>) c -> {
            while(c.next()) {
                if (c.wasAdded()) {
                    if (c.wasReplaced()) {
                        tv.getItems().remove(c.getFrom());
                    }
                    tv.getItems().addAll(c.getAddedSubList());
                } else if(c.wasRemoved()) {
                    tv.getItems().removeAll(c.getRemoved());
                }
            }
        });
    }

    public void add(Stock s) {
        data.add(s);
    }

    public ObservableList<Stock> get() {
        return data;
    }

    public Optional<Stock> find(String kdStock) {
        for (Stock s : data) {
            if (s.getKode().equals(kdStock)) {
                return Optional.of(s);
            }
        }
    return null;
    }

    public void update(int index, Stock s) {
        data.set(index, s);
    }

    public void delete(Stock s) {
        data.remove(s);
    }

    private List<Stock> fetchFromMongo() {
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
        return list;
    }

}
