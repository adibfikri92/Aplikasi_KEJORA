package com.example.aplikasikejora;

import java.util.Date;

public class RetrieveAcara {
    String Kategori;
    String NamaAcara;
    String Jantina;
    Long BilPeserta;
    Date Tarikh;
    String Pengadil;

    private RetrieveAcara() {
    }
    private RetrieveAcara(String Kategori, String NamaAcara, Long Bil,
                          String Pengadil, Date Tarikh,String Jantina) {
        this.Kategori = Kategori;
        this.NamaAcara = NamaAcara;
        this.BilPeserta = Bil;
        this.Tarikh = Tarikh;
        this.Jantina = Jantina;
        this.Pengadil = Pengadil;
    }
    public String getJantina() {
        return Jantina;
    }

    public void setJantina(String jantina) {
        Jantina = jantina;
    }
    public Date getTarikh() {
        return Tarikh;
    }

    public void setTarikh(Date tarikh) {
        Tarikh = tarikh;
    }

    public String getKategori() {
        return Kategori;
    }

    public void setKategori(String kategori) {
        Kategori = kategori;
    }

    public String getNamaAcara() {
        return NamaAcara;
    }

    public void setNamaAcara(String namaAcara) {
        NamaAcara = namaAcara;
    }

    public Long getBilPeserta() {
        return BilPeserta;
    }

    public void setBilPeserta(Long bilPeserta) {
        BilPeserta = bilPeserta;
    }

    public String getPengadil() {
        return Pengadil;
    }

    public void setPengadil(String pengadil) {
        Pengadil = pengadil;
    }


}
