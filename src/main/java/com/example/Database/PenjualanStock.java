package com.example.Database;

import org.bson.types.ObjectId;

public class PenjualanStock {
    private ObjectId id;
    private Stock stock;
    private Integer qty;

    public PenjualanStock(Stock stock, Integer qty) {
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
}
