package com.example.Database.DAO;

import com.example.Database.Langganan;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LanggananDAO {
    private final ObservableList<Langganan> data = FXCollections.observableArrayList();

    public LanggananDAO() {
        data.setAll(fetchFromMongo());
    }

    public void addListener(TableView tv) {
        data.addListener((ListChangeListener<Langganan>) c -> {
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

    public void add(Langganan s) {
        data.add(s);
    }

    public ObservableList<Langganan> get() {
        return data;
    }

    public void update(int index, Langganan s) {
        data.set(index, s);
    }

    public void delete(Langganan s) {
        data.remove(s);
    }

    private List<Langganan> fetchFromMongo() {
        List<Langganan> list = new ArrayList<>(
                Arrays.asList(
                        new Langganan("MTH202", "Mentari Jaya", "Perkamil"),
                        new Langganan("HS", "ENAM", "Jengki"),
                        new Langganan("MRT101", "Orion", "Morotai"),
                        new Langganan("BTG050", "Girian", "Bitung") )
        );
        return list;
    }


}
