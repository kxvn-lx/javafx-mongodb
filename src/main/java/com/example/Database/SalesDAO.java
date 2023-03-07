package com.example.Database;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SalesDAO {
    private final ObservableList<Salesman> data = FXCollections.observableArrayList();

    public SalesDAO() {
        data.setAll(fetchFromMongo());
    }

    public void addListener(TableView tv) {
        data.addListener((ListChangeListener<Salesman>) c -> {
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

    public void add(Salesman s) {
        data.add(s);
    }

    public ObservableList<Salesman> get() {
        return data;
    }

    public void update(int index, Salesman s) {
        data.set(index, s);
    }

    public void delete(Salesman s) {
        data.remove(s);
    }

    private List<Salesman> fetchFromMongo() {
        List<Salesman> salesmanList = new ArrayList<Salesman>(
                Arrays.asList(
                        new Salesman("R", 01, "Perkamil"),
                        new Salesman("C", 02, "Tuminting"),
                        new Salesman("E-BJ", 04, "Bahu"),
                        new Salesman("ADMIN", 14, "Interweb") )
        );
        return salesmanList;
    }

}
