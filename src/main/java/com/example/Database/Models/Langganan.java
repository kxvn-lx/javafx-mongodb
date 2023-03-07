package com.example.Database.Models;

import org.bson.types.ObjectId;

public class Langganan {
    private ObjectId id;
    private String no_langganan;
    private String nama;
    private String alamat;

    public Langganan(String no_langanan, String nama, String alamat) {
        this.no_langganan = no_langanan;
        this.nama = nama;
        this.alamat = alamat;
    }

    public Langganan(ObjectId id, String no_langanan, String nama) {
        this.no_langganan = no_langanan;
        this.nama = nama;
        this.id = id;
    }

    @Override
    public String toString() {
        super.toString();
        return no_langganan + ":" + nama;
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

}
