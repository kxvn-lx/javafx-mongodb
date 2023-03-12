package com.example.Database.DAO;

import com.example.Database.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.util.*;
import java.util.stream.Collectors;

public class PenjualanDAO {
    private static final ObservableList<Penjualan> data = FXCollections.observableArrayList();

    public PenjualanDAO() {
        if (data.isEmpty()) data.setAll(fetchFromMongo());
    }

    public void addListener(TableView<Penjualan> tv) {
        data.addListener((ListChangeListener<Penjualan>) c -> {
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
    public void add(Penjualan s) {
        // Create a new List called 'p' to hold the filtered Penjualan objects
        List<Penjualan> p = data.stream()
                // Use the filter() method to keep only Penjualan objects with a matching NoFaktur value
                .filter(penjualan -> Objects.equals(penjualan.getNoFaktur(), s.getNoFaktur()))
                // Collect the filtered Penjualan objects into a new List using the collect() method
                .collect(Collectors.toList());

        // If any Penjualan objects were found in the filtered List, update the first one and return
        if (p.size() > 0) {
            update(s);
            return;
        }

        data.add(s);
    }
    public ObservableList<Penjualan> get() {
        return data;
    }
    public void update(int index, Penjualan s) {
        data.set(index, s);
    }
    public void update(Penjualan p) {
        // Find the Penjualan object with the unique ID you want to update
        Optional<Penjualan> penjualanToUpdate = data.stream()
                .filter(penjualan -> Objects.equals(penjualan.getNoFaktur(), p.getNoFaktur()))
                .findFirst();

        if (penjualanToUpdate.isPresent()) {
            // Update the Penjualan object's properties
            Penjualan updatedPenjualan = penjualanToUpdate.get();
            // Notify the list that the Penjualan object has been updated
            int index = data.indexOf(penjualanToUpdate.get());
            data.set(index, p);
        } else {
            System.out.println("NOTHING TO UPDATE");
        }
    }
    public void delete(Penjualan s) {
        data.remove(s);
    }
    private List<Penjualan> fetchFromMongo() {
        List<Stock> stocks = new ArrayList<>(
                Arrays.asList(
                        new Stock("HD15BP", "Tas 15 /50/20L Ramah Lingkungan", "BAJA", 1011000, "Karung"),
                        new Stock("PE820", "PE 8x20 @0.2KG/PAK", "Hijau Daun", 360000, "Rol"),
                        new Stock("PP1000", "PP 1KG 15X27 @5PAK @10KG", "Hijau Daun", 350000, "Kg"),
                        new Stock("PP6", "PP Rol 6x0.3 @ 30ROL", "Malaikat jatuh", 297000, "Rol")
                )
        );

        List<Penjualan> list = new ArrayList<>(
                Arrays.asList(
                    new Penjualan(10500
                            , 1,
                            "MTH202",
                            new Date().toString(),
                            Status.C,
                            new PenjualanStock[]{
                                new PenjualanStock(stocks.get(0), 2),
                                    new PenjualanStock(stocks.get(1), 5),
                                    new PenjualanStock(stocks.get(2), 10),
                                    new PenjualanStock(stocks.get(3), 1),
                            },
                            7619000,
                            0
                    )
                )
        );
        return list;
    }
}
