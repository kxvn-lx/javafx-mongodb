package com.example.Database;

import org.bson.Document;
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
    private Integer jumlah;
    private Integer setoran;

    public Penjualan(ObjectId id, Integer noFaktur, Integer noSalesman, String noLangganan, String tanggal, Status status, PenjualanStock[] pjs, Integer jumlah, Integer setoran) {
        this.id = id;
        this.noFaktur = noFaktur;
        this.noSalesman = noSalesman;
        this.noLangganan = noLangganan;
        this.tanggal = tanggal;
        this.status = status;
        this.pjs = pjs;
        this.jumlah = jumlah;
        this.setoran = setoran;
    }

    @Override
    public String toString() {
        return "Penjualan{" +
                "noFaktur=" + noFaktur +
                ", noSalesman=" + noSalesman +
                ", noLangganan='" + noLangganan + '\'' +
                ", tanggal='" + tanggal + '\'' +
                ", status=" + status +
                ", pjs=" + pjs.length +
                ", jumlah=" + jumlah +
                ", setoran=" + setoran +
                '}';
    }

    public Document toDocument() {
        Document doc = new Document("_id", this.getId());
        doc.append("no_faktur", this.getNoFaktur())
                .append("no_salesman", this.getNoSalesman())
                .append("no_langganan", this.getNoLangganan())
                .append("tanggal", this.getTanggal())
                .append("status", this.getStatus())
                .append("penjualan_stock", Arrays.asList(this.getPjs()))
                .append("jumlah", this.getJumlah())
                .append("setoran", this.getSetoran());
        return doc;
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
    public Status getStatus() {
        return status;
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
    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
    public int getSetoran() { return setoran; }
    public void setSetoran(int setoran) { this.setoran += setoran; }
    public ObjectId getId() {
        return id;
    }
    public void setId(ObjectId id) {
        this.id = id;
    }

}
