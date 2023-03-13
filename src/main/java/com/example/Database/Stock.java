package com.example.Database;

import org.bson.Document;
import org.bson.types.ObjectId;

public class Stock {
    private ObjectId id;
    private String kode;
    private String nama;
    private String merek;
    private Integer harga;
    private String satuan;

    public Stock(ObjectId id, String kode, String nama, String merek, Integer harga, String satuan) {
        this.id = id;
        this.kode = kode;
        this.nama = nama;
        this.merek = merek;
        this.harga = harga;
        this.satuan = satuan;
    }

    public Stock(String kode, String nama, String merek, Integer harga, String satuan) {
        this.kode = kode;
        this.nama = nama;
        this.merek = merek;
        this.harga = harga;
        this.satuan = satuan;
    }

    @Override
    public String toString() {
        return "Stock{" +
                ", kode='" + kode + '\'' +
                ", nama='" + nama + '\'' +
                ", merek='" + merek + '\'' +
                ", harga=" + harga +
                ", satuan='" + satuan + '\'' +
                '}';
    }

    public String getKode() {
        return kode;
    }
    public void setKode(String kode) {
        this.kode = kode;
    }
    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public String getMerek() {
        return merek;
    }
    public void setMerek(String merek) {
        this.merek = merek;
    }
    public Integer getHarga() {
        return harga;
    }
    public void setHarga(Integer harga) {
        this.harga = harga;
    }
    public String getSatuan() {
        return satuan;
    }
    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }
    public ObjectId getId() {
        return id;
    }
    public void setId(ObjectId id) {
        this.id = id;
    }
    public Document toDocument() {
        return new Document("_id", this.getId())
                .append("kode", this.getKode())
                .append("nama", this.getNama())
                .append("merek", this.getMerek())
                .append("harga", this.getHarga())
                .append("satuan", this.getSatuan());
    }
}
