package com.example.Database;

import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.Date;

public class Penjualan {

    private ObjectId id;
    private Integer noFaktur;
    private Integer noSalesman;
    private String noLangganan;
    private String tanggal;
    private Status status;
    private PenjualanStock[] pjs;

    public Penjualan(Integer noFaktur, Integer noSalesman, String noLangganan, String tanggal, Status status, PenjualanStock[] pjs) {
        this.noFaktur = noFaktur;
        this.noSalesman = noSalesman;
        this.noLangganan = noLangganan;
        this.tanggal = tanggal;
        this.status = status;
        this.pjs = pjs;
    }

    public Penjualan(ObjectId id, Integer noFaktur, Integer noSalesman, String noLangganan, String tanggal, Status status, PenjualanStock[] pjs) {
        this.id = id;
        this.noFaktur = noFaktur;
        this.noSalesman = noSalesman;
        this.noLangganan = noLangganan;
        this.tanggal = tanggal;
        this.status = status;
        this.pjs = pjs;
    }

    @Override
    public String toString() {
        return "Penjualan{" +
                "noFaktur=" + noFaktur +
                ", salesman=" + noSalesman +
                ", langganan=" + noLangganan +
                ", tanggal=" + tanggal +
                ", status=" + status +
                ", PenjualanStocks=" + Arrays.toString(pjs) +
                '}';
    }

    public Integer getNoFaktur() {
        return noFaktur;
    }

    public void setNoFaktur(Integer noFaktur) {
        this.noFaktur = noFaktur;
    }

    public Integer getNoSalesman() {
        return noSalesman;
    }

    public void setSalesman(Salesman salesman) {
        this.noSalesman = noSalesman;
    }

    public String getNoLangganan() {
        return noLangganan;
    }

    public void setLangganan(String noLangganan) {
        this.noLangganan = noLangganan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getStatus() {
        return status.name();
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public PenjualanStock[] getPjs() {
        return pjs;
    }

    public void setPenjualanStock(PenjualanStock[] pjs) {
        this.pjs = pjs;
    }
}
