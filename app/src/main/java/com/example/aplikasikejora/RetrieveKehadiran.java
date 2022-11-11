package com.example.aplikasikejora;

public class RetrieveKehadiran {

    String NamaPeserta;
    String Kehadiran;
    String HouseID;

    private RetrieveKehadiran(){

    }

    private RetrieveKehadiran(String NamaPeserta, String Kehadiran, String HouseID){
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
