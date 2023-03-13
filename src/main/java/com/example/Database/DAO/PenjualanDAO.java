package com.example.Database.DAO;

import com.example.Database.*;
import com.example.Database.Connection.MongoDBConnection;
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

import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

public class PenjualanDAO implements DataAcccessObject<Penjualan> {
    private static final ObservableList<Penjualan> data = FXCollections.observableArrayList();
    private final MongoDBConnection co = new MongoDBConnection("entity", "penjualan");

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
    public boolean add(Penjualan s) {
        Optional<Penjualan> matchedPenjualan = data.stream()
                // Use the filter() method to keep only Penjualan objects with a matching NoFaktur value
                .filter(penjualan -> Objects.equals(penjualan.getNoFaktur(), s.getNoFaktur()))
                .findFirst();

        if (matchedPenjualan.isPresent()) {
            return update(matchedPenjualan.get());
        }

        try {
            MongoCollection<Document> collection = co.getCollection();

            Document doc = new Document("_id", s.getId());
            doc.append("no_faktur", s.getNoFaktur())
                    .append("no_salesman", s.getNoSalesman())
                    .append("no_langganan", s.getNoLangganan())
                    .append("tanggal", s.getTanggal())
                    .append("status", s.getStatus())
                    .append("penjualan_stock", s.getPjs())
                    .append("jumlah", s.getJumlah())
                    .append("setoran", s.getSetoran());

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
    public ObservableList<Penjualan> getAll() {
        return data;
    }
    public boolean update(int index, Penjualan s) {
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
    public boolean update(Penjualan p) {
        try {
            MongoCollection<Document> collection = co.getCollection();
            // update one document
            Bson filter = eq("_id", p.getId());
            UpdateResult result = collection.replaceOne(filter, p.toDocument());
            if (result.wasAcknowledged() && result.getModifiedCount() > 0) {
                Platform.runLater(() -> {
                    if (findByNo(Integer.toString(p.getNoFaktur())).isPresent()) {
                        int index = data.indexOf(findByNo(Integer.toString(p.getNoFaktur())).get());
                        data.set(index, p);
                    } else {
                        System.out.println("oldPenjualan not present");
                    }
                });
                return findByNo(Integer.toString(p.getNoFaktur())).isPresent();
            }
            return result.wasAcknowledged() && result.getModifiedCount() > 0;
        } catch (MongoException e) {
            e.printStackTrace();
        } finally {
            co.close();
        }
        return false;
    }
    public boolean delete(Penjualan s) {
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
    @Override
    public Optional<Penjualan> findByNo(String no) {
        return data.stream()
                .filter(penjualan -> Objects.equals(penjualan.getNoFaktur(), Integer.parseInt(no)))
                .findFirst();
    }
    @Override
    public List<Penjualan> findContains(String keyword) {
        return null;
    }

    private List<Penjualan> fetchFromMongo() {
        List<Penjualan> list = new ArrayList<>();
        MongoCollection<Document> collection = co.getCollection();

        for (Document doc : collection.find()) {
            List<Document> stockList = doc.getList("penjualan_stock", Document.class);
            List<PenjualanStock> penjualanStocks = new ArrayList<>();
            for (Document sl : stockList) {
                Document obj = (Document) sl.get("stock");
                PenjualanStock pj = new PenjualanStock(
                        obj.getObjectId("_id"),
                        new Stock(
                                obj.getString("kode"),
                                obj.getString("nama"),
                                obj.getString("merek"),
                                obj.getInteger("harga"),
                                obj.getString("satuan")
                        ),
                        sl.getInteger("qty")
                );
                penjualanStocks.add(pj);
            }

            Penjualan s = new Penjualan(
                    doc.getObjectId("_id"),
                    doc.getInteger("no_faktur"),
                    doc.getInteger("no_salesman"),
                    doc.getString("no_langganan"),
                    doc.getString("tanggal"),
                    Status.valueOf(doc.getString("status")),
                    penjualanStocks.toArray(new PenjualanStock[0]),
                    doc.getInteger("jumlah"),
                    doc.getInteger("setoran")
            );
            list.add(s);
        }
        return list;

//        List<Stock> stocks = new ArrayList<>(
//                Arrays.asList(
//                        new Stock("HD15BP", "Tas 15 /50/20L Ramah Lingkungan", "BAJA", 1011000, "Karung"),
//                        new Stock("PE820", "PE 8x20 @0.2KG/PAK", "Hijau Daun", 360000, "Rol"),
//                        new Stock("PP1000", "PP 1KG 15X27 @5PAK @10KG", "Hijau Daun", 350000, "Kg"),
//                        new Stock("PP6", "PP Rol 6x0.3 @ 30ROL", "Malaikat jatuh", 297000, "Rol")
//                )
//        );
//
//        return new ArrayList<>(
//                List.of(
//                        new Penjualan(new ObjectId(),10500
//                                , 1,
//                                "MTH202",
//                                new Date().toString(),
//                                Status.C,
//                                new PenjualanStock[]{
//                                        new PenjualanStock(stocks.get(0), 2),
//                                        new PenjualanStock(stocks.get(1), 5),
//                                        new PenjualanStock(stocks.get(2), 10),
//                                        new PenjualanStock(stocks.get(3), 1),
//                                },
//                                7619000,
//                                0
//                        )
//                )
//        );
    }
}
