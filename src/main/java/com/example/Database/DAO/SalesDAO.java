package com.example.Database.DAO;

import com.example.Database.Connection.MongoDBConnection;
import com.example.Database.Salesman;
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
import static com.mongodb.client.model.Updates.set;

public class SalesDAO {
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
        MongoCollection<Document> collection = co.getCollection();

        Document salesDoc = new Document("_id", new ObjectId());
        salesDoc.append("no_salesman", s.getNo_salesman())
                .append("nama", s.getNama())
                .append("alamat", s.getAlamat());

        InsertOneResult result = collection.insertOne(salesDoc);
        if (result.wasAcknowledged()) Platform.runLater(() -> data.add(s));
        return result.wasAcknowledged();
    }
    public ObservableList<Salesman> get() {
        return data;
    }
    public boolean update(int index, Salesman s) {
        MongoCollection<Document> collection = co.getCollection();

        // update one document
        Bson filter = eq("_id", s.getId());
        UpdateResult result = collection.replaceOne(filter, s.toDocument());
        if (result.wasAcknowledged()) Platform.runLater(() -> data.set(index, s));
        return result.wasAcknowledged();
    }
    public boolean delete(Salesman s) {
        MongoCollection<Document> collection = co.getCollection();

        // delete one document
        Bson filter = eq("_id", s.getId());
        DeleteResult result = collection.deleteOne(filter);
        if (result.wasAcknowledged()) Platform.runLater(() -> data.remove(s));
        return result.wasAcknowledged();
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
        List<Salesman> salesmanList = new ArrayList<>();
        MongoCollection<Document> collection = co.getCollection();
        for (Document doc : collection.find()) {
            Salesman s = new Salesman(doc.getObjectId("_id"), doc.get("nama").toString(), (int) doc.get("no_salesman"), doc.getString("alamat"));
            salesmanList.add(s);
        }
        return salesmanList;
    }

}
