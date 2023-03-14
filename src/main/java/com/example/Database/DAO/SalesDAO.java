package com.example.Database.DAO;

import com.example.Database.Connection.MongoDBConnection;
import com.example.Database.Salesman;
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
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

public class SalesDAO implements DataAcccessObject<Salesman> {
    private final MongoDBConnection co = new MongoDBConnection("entity", "salesman");
    private static final ObservableList<Salesman> data = FXCollections.observableArrayList();

    public SalesDAO() {
        if (data.isEmpty()) data.setAll(fetchFromMongo());

    }

    public void addListener(TableView<Salesman> tv) {
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
    public boolean add(Salesman s) {
        InsertOneResult result = null;
        try {
            MongoCollection<Document> collection = co.getCollection();
            result = collection.insertOne(s.toDocument());
        } catch (MongoException e) {
            e.printStackTrace();
        } finally {
            co.close();
            if (result.wasAcknowledged() && result.getInsertedId() != null) Platform.runLater(() -> data.add(s));
            return result.wasAcknowledged() && result.getInsertedId() != null;
        }
    }
    public ObservableList<Salesman> getAll() {
        return data;
    }
    public boolean update(int index, Salesman s) {
        UpdateResult result = null;

        try {
            MongoCollection<Document> collection = co.getCollection();
            // update one document
            Bson filter = eq("_id", s.getId());
            result = collection.replaceOne(filter, s.toDocument());
        } catch (MongoException e) {
            e.printStackTrace();
        } finally {
            co.close();
            if (result.wasAcknowledged() && result.getModifiedCount() > 0) Platform.runLater(() -> data.set(index, s));
            return result.wasAcknowledged() && result.getModifiedCount() > 0;
        }
    }
    public boolean delete(Salesman s) {
        DeleteResult result = null;

        try {
            MongoCollection<Document> collection = co.getCollection();

            // delete one document
            Bson filter = eq("_id", s.getId());
            result = collection.deleteOne(filter);
        } catch (MongoException e) {
            e.printStackTrace();
        } finally {
            co.close();
            if (result.wasAcknowledged() && result.getDeletedCount() > 0) Platform.runLater(() -> data.remove(s));
            return result.wasAcknowledged() && result.getDeletedCount() > 0;
        }
    }
    @Override
    public Optional<Salesman> findByNo(String no) {
        List<Salesman> l = data.stream()
                .filter(s -> s.getNo_salesman() == Integer.parseInt(no))
                .collect(Collectors.toList());

        if (l.size() > 0) return Optional.of(l.get(0));
        else return Optional.empty();
    }
    @Override
    public List<Salesman> findContains(String keyword) {
        List<Salesman> arr = new ArrayList<>();
        for (Salesman s : data) {
            String numberAsString = String.valueOf(s.getNo_salesman());
            if (numberAsString.contains(keyword)) {
                arr.add(s);
            }
        }

        return arr;
    }
    private List<Salesman> fetchFromMongo() {
        List<Salesman> salesmanList = new ArrayList<>();
        MongoCollection<Document> collection = co.getCollection();
        for (Document doc : collection.find()) {
            Salesman s = new Salesman(doc.getObjectId("_id"), doc.get("nama").toString(), (int) doc.get("no_salesman"), doc.getString("alamat"));
            salesmanList.add(s);
        }
        return salesmanList;
    }

}
