package com.example.aplikasikejora;

public class RetrieveKeputusan {

    String NamaAcara;
    String No1,No2,No3;

    private RetrieveKeputusan(){}

    private RetrieveKeputusan(String NamaAcara, String No1, String No2, String No3){
        this.NamaAcara = NamaAcara;
        this.No1 = No1;
        this.No2 = No2;
        this.No3 = No3;
    }

    public String getNamaAcara() {
        return NamaAcara;
    }

    public void setNamaAcara(String namaAcara) {
        NamaAcara = namaAcara;
    }

    public String getNo1() {
        return No1;
    }

    public void setNo1(String no1) {
        No1 = no1;
    }

    public String getNo2() {
        return No2;
    }

    public void setNo2(String no2) {
        No2 = no2;
    }

    public String getNo3() {
        return No3;
    }

    public void setNo3(String no3) {
        No3 = no3;
    }




}
