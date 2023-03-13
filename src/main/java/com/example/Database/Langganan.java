package com.example.Database;

import org.bson.Document;
import org.bson.types.ObjectId;

public class Langganan {
    private ObjectId id;
    private String no_langganan;
    private String nama;
    private String alamat;

    public Langganan(ObjectId id, String no_langanan, String nama, String alamat) {
        this.no_langganan = no_langanan;
        this.nama = nama;
        this.id = id;
        this.alamat = alamat;
    }

    public Document toDocument() {
        return new Document("_id", this.getId())
                .append("no_langganan", this.getNo_langganan())
                .append("nama", this.getNama())
                .append("alamat", this.getAlamat());
    }
    public String getNo_langganan() {
        return no_langganan;
    }
    public void setNo_langganan(String no_langganan) {
        this.no_langganan = no_langganan;
    }
    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public String getAlamat() {
        return alamat;
    }
    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
    public ObjectId getId() {
        return id;
    }
    public void setId(ObjectId id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Langganan{" +
                "id=" + id +
                ", no_langganan='" + no_langganan + '\'' +
                ", nama='" + nama + '\'' +
                ", alamat='" + alamat + '\'' +
                '}';
    }
}
