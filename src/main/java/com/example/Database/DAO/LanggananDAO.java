package com.example.Database.DAO;

import com.example.Database.Connection.MongoDBConnection;
import com.example.Database.Langganan;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

public class LanggananDAO implements DataAcccessObject<Langganan> {
    private final MongoDBConnection co = new MongoDBConnection("entity", "langganan");
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
        try {
            MongoCollection<Document> collection = co.getCollection();
            InsertOneResult result = collection.insertOne(s.toDocument());
            if (result.wasAcknowledged() && result.getInsertedId() != null) Platform.runLater(() -> data.add(s));
            return true;
        } catch (MongoException e) {
            e.printStackTrace();
        } finally {
            co.close();
        }
        return false;
    }
    public ObservableList<Langganan> getAll() {
        return data;
    }
    public boolean update(int index, Langganan s) {
        System.out.println(s);
        try {
            MongoCollection<Document> collection = co.getCollection();
            // update one document
            Bson filter = eq("_id", s.getId());
            UpdateResult result = collection.replaceOne(filter, s.toDocument());
            if (result.wasAcknowledged() && result.getModifiedCount() > 0) {
                Platform.runLater(() -> data.set(index, s));
                return true;
            }
        } catch (MongoException e) {
            e.printStackTrace();
        } finally {
            co.close();
        }
        return false;
    }
    public boolean delete(Langganan s) {
        System.out.println(s);
        try {
            MongoCollection<Document> collection = co.getCollection();

            // delete one document
            Bson filter = eq("_id", s.getId());
            DeleteResult result = collection.deleteOne(filter);
            if (result.wasAcknowledged() && result.getDeletedCount() > 0) Platform.runLater(() -> data.remove(s));
            return true;
        } catch (MongoException e) {
            e.printStackTrace();
        } finally {
            co.close();
        }
        return false;
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
        List<Langganan> fetched = new ArrayList<>();
        MongoCollection<Document> collection = co.getCollection();
        for (Document doc : collection.find()) {
            Langganan obj = new Langganan(doc.getObjectId("_id"), doc.get("nama").toString(), doc.getString("no_langganan"), doc.getString("alamat"));
            fetched.add(obj);
        }
        return fetched;

//        return new ArrayList<>(
//                Arrays.asList(
//                        new Langganan("MTH202", "Mentari Jaya", "Perkamil"),
//                        new Langganan("HS", "ENAM", "Jengki"),
//                        new Langganan("MRT101", "Orion", "Morotai"),
//                        new Langganan("BTG050", "Girian", "Bitung") )
//        );
    }

}
