package com.example.Database.DAO;

import com.example.Database.Connection.MongoDBConnection;
import com.example.Database.Salesman;
import com.example.Database.Stock;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

public class StockDAO implements DataAcccessObject<Stock> {
    private static final ObservableList<Stock> data = FXCollections.observableArrayList();
    private final MongoDBConnection co = new MongoDBConnection("entity", "stock");

    public StockDAO() { if (data.isEmpty()) data.setAll(fetchFromMongo()); }

    public void addListener(TableView<Stock> tv) {
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

    public boolean add(Stock s) {
        try {
            MongoCollection<Document> collection = co.getCollection();

            Document doc = new Document("_id", s.getId());
                    doc.append("kode", s.getKode())
                    .append("nama", s.getNama())
                    .append("merek", s.getMerek())
                    .append("harga", s.getHarga())
                    .append("satuan", s.getSatuan());

            InsertOneResult result = collection.insertOne(doc);
            if (result.wasAcknowledged() && result.getInsertedId() != null) Platform.runLater(() -> data.add(s));
            return result.wasAcknowledged() && result.getInsertedId() != null;
        } catch (MongoException e) {
            e.printStackTrace();
        } finally {
            co.close();
        }
        return false;
    }
    public ObservableList<Stock> getAll() {
        return data;
    }
    public Optional<Stock> findByNo(String kdStock) {
        List<Stock> l = data.stream()
                .filter(stock -> stock.getKode().equals(kdStock.toUpperCase()))
                .collect(Collectors.toList());

        if (l.size() > 0) return Optional.of(l.get(0));
        else return Optional.empty();
    }
    @Override
    public List<Stock> findContains(String keyword) {
        return null;
    }
    public boolean update(int index, Stock s) {
        try {
            MongoCollection<Document> collection = co.getCollection();
            // update one document
            Bson filter = eq("_id", s.getId());
            UpdateResult result = collection.replaceOne(filter, s.toDocument());
            if (result.wasAcknowledged() && result.getModifiedCount() > 0) Platform.runLater(() -> data.set(index, s));
            return result.wasAcknowledged() && result.getModifiedCount() > 0;
        } catch (MongoException e) {
            e.printStackTrace();
        } finally {
            co.close();
        }
        return false;
    }
    public boolean delete(Stock s) {
        try {
            MongoCollection<Document> collection = co.getCollection();

            // delete one document
            Bson filter = eq("_id", s.getId());
            DeleteResult result = collection.deleteOne(filter);
            if (result.wasAcknowledged() && result.getDeletedCount() > 0) Platform.runLater(() -> data.remove(s));
            return result.wasAcknowledged() && result.getDeletedCount() > 0;
        } catch (MongoException e) {
            e.printStackTrace();
        } finally {
            co.close();
        }
        return false;
    }

    private List<Stock> fetchFromMongo() {
        List<Stock> list = new ArrayList<>();
        MongoCollection<Document> collection = co.getCollection();
        for (Document doc : collection.find()) {
            Stock s = new Stock(
                    doc.getObjectId("_id"),
                    doc.getString("kode"),
                    doc.getString("nama"),
                    doc.getString("merek"),
                    doc.getInteger("harga"),
                    doc.getString("satuan")
                    );
            list.add(s);
        }

        return list;
    }

}
