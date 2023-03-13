package com.example.Database.DAO;

import com.example.Database.Langganan;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LanggananDAO implements DataAcccessObject<Langganan> {
    private static final ObservableList<Langganan> data = FXCollections.observableArrayList();

    public LanggananDAO() {
        if (data.isEmpty()) data.setAll(fetchFromMongo());
    }

    public void addListener(TableView<Langganan> tv) {
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

    public boolean add(Langganan s) {
        data.add(s);
        return true;
    }
    public ObservableList<Langganan> getAll() {
        return data;
    }
    public boolean update(int index, Langganan s) {
        data.set(index, s);
        return true;
    }
    public boolean delete(Langganan s) {
        data.remove(s);
        return true;
    }
    public List<Langganan> findContains(String noLangganan) {
        List<Langganan> arr = new ArrayList<>();

        for (Langganan l : data) { if (l.getNo_langganan().contains(noLangganan.toUpperCase())) arr.add(l); }
        return arr;
    }
    public Optional<Langganan> findByNo(String noLangganan) {
        List<Langganan> l = data.stream()
                .filter(langganan -> langganan.getNo_langganan().equals(noLangganan.toUpperCase()))
                .collect(Collectors.toList());

        if (l.size() > 0) return Optional.of(l.get(0));
        else return Optional.empty();
    }

    private List<Langganan> fetchFromMongo() {
        return new ArrayList<>(
                Arrays.asList(
                        new Langganan("MTH202", "Mentari Jaya", "Perkamil"),
                        new Langganan("HS", "ENAM", "Jengki"),
                        new Langganan("MRT101", "Orion", "Morotai"),
                        new Langganan("BTG050", "Girian", "Bitung") )
        );
    }

}
