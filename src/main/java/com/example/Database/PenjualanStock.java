package com.example.Database;

import org.bson.Document;
import org.bson.types.ObjectId;

public class PenjualanStock {

    private ObjectId id;
    private Stock stock;
    private Integer qty;

    public PenjualanStock(ObjectId id, Stock stock, Integer qty) {
        this.id = id;
        this.stock = stock;
        this.qty = qty;
    }


    public Stock getStock() {
        return stock;
    }
    public void setStock(Stock stock) {
        this.stock = stock;
    }
    public Integer getQty() {
        return qty;
    }
    public void setQty(Integer qty) {
        this.qty = qty;
    }
    public ObjectId getId() {
        return id;
    }
    public void setId(ObjectId id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PenjualanStock{" +
                "stock=" + stock +
                ", qty=" + qty +
                '}';
    }
}
