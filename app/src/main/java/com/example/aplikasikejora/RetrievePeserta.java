package com.example.aplikasikejora;

import java.util.Date;

public class RetrievePeserta {
    String ICNumber,NamaPeserta,Kehadiran,HouseID;

    private RetrievePeserta() {
    }

    private RetrievePeserta(String ICNumber, String NamaPeserta,String Kehadiran, String HouseID) {
        this.ICNumber = ICNumber;
        this.NamaPeserta = NamaPeserta;
        this.Kehadiran = Kehadiran;
        this.HouseID = HouseID;
    }

    public String getNamaPeserta() {
        return NamaPeserta;
    }

    public void setNamaPeserta(String namaPeserta) {
        NamaPeserta = namaPeserta;
    }

    public String getICNumber() {
        return ICNumber;
    }

    public void setICNumber(String ICNumber) {
        this.ICNumber = ICNumber;
    }

    public String getKehadiran() {
        return Kehadiran;
    }

    public void setKehadiran(String kehadiran) {
        Kehadiran = kehadiran;
    }

    public String getHouseID() {
        return HouseID;
    }

    public void setHouseID(String houseID) {
        HouseID = houseID;
    }
}
