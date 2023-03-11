package com.example.Database;

import org.bson.types.ObjectId;

public class Setoran {
    private ObjectId id;
    private Integer noFaktur;
    private Integer jumlah;
    private Integer totalSetoran;

    public Setoran(Integer noFaktur, Integer jumlah, Integer totalSetoran) {
        this.noFaktur = noFaktur;
        this.jumlah = jumlah;
        this.totalSetoran = totalSetoran;
    }

    public Integer getNoFaktur() {
        return noFaktur;
    }

    public void setNoFaktur(Integer noFaktur) {
        this.noFaktur = noFaktur;
    }

    public Integer getJumlah() {
        return jumlah;
    }

    public void setJumlah(Integer jumlah) {
        this.jumlah = jumlah;
    }

    public Integer getTotalSetoran() {
        return totalSetoran;
    }

    public void setTotalSetoran(Integer totalSetoran) {
        this.totalSetoran = totalSetoran;
    }

    @Override
    public String toString() {
        return "Setoran{" +
                "noFaktur=" + noFaktur +
                ", jumlah=" + jumlah +
                ", totalSetoran=" + totalSetoran +
                '}';
    }
}
