package com.example.Database.DAO;

import com.example.Database.Salesman;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SalesDAO {
    private static final ObservableList<Salesman> data = FXCollections.observableArrayList();

    public SalesDAO() {
        if (data.isEmpty()) data.setAll(fetchFromMongo());
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

    public Optional<Salesman> find(Integer noSalesman) {
        List<Salesman> l = data.stream()
                .filter(salesman -> salesman.getNo_salesman() == noSalesman)
                .collect(Collectors.toList());

        if (l.size() > 0) return Optional.of(l.get(0));
        else return Optional.empty();
    }

    public List<Salesman> findByN(String noSalesman) {
        List<Salesman> arr = new ArrayList<>();

        for (Salesman s : data) {
            String numberAsString = String.valueOf(s.getNo_salesman());
            if (numberAsString.contains(noSalesman)) {
                arr.add(s);
            }
        }

        return arr;
    }

    private List<Salesman> fetchFromMongo() {
        List<Salesman> salesmanList = new ArrayList<>(
                Arrays.asList(
                        new Salesman("R", 01, "Perkamil"),
                        new Salesman("C", 02, "Tuminting"),
                        new Salesman("E-BJ", 04, "Bahu"),
                        new Salesman("ADMIN", 14, "Interweb"))
        );
        return salesmanList;
    }

}
