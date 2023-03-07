package com.example.Database;

import org.bson.types.ObjectId;

public class Salesman {
    private ObjectId id;
    private int no_salesman;
    private String nama;
    private String alamat;

    /**
     * @param id Object ID pe Document
     * @param nama Salesman pe nama
     * @param no_salesman nomor salesman
     */
    public Salesman(ObjectId id, String nama, int no_salesman) {
        this.id = id;
        this.no_salesman = no_salesman;
        this.nama = nama;
    }

    /**
     * @param nama Salesman pe nama
     * @param no_salesman nomor salesman
     */
    public Salesman(String nama, int no_salesman, String alamat) {
        this.nama = nama;
        this.no_salesman = no_salesman;
        this.alamat = alamat;
    }

    @Override
    public String toString() {
        super.toString();
        return no_salesman + ":" + nama;
    }

    public ObjectId getId() {
        return id;
    }

    public int getNo_salesman() {
        return no_salesman;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }


    public void setNo_salesman(int no_salesman) {
        this.no_salesman = no_salesman;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
