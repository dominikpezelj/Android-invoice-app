package com.mbozic;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Dominik on 14.7.2018..
 */

@Entity
public class TrupciKlasa {
    @Id(autoincrement = true)
    private Long ID;
    @NotNull
    private String Opis;
    @NotNull
    private Date Datum;
    @NotNull
    private String BrojPlocice;
    @NotNull
    private Float Duzina;
    @NotNull
    private Float Promjer;
    @NotNull
    private Float Kubikaza;
    @NotNull
    private Float Total;
    private String Roba;
    private String Boja;
    @Generated(hash = 642122621)
    public TrupciKlasa(Long ID, @NotNull String Opis, @NotNull Date Datum,
            @NotNull String BrojPlocice, @NotNull Float Duzina,
            @NotNull Float Promjer, @NotNull Float Kubikaza, @NotNull Float Total,
            String Roba, String Boja) {
        this.ID = ID;
        this.Opis = Opis;
        this.Datum = Datum;
        this.BrojPlocice = BrojPlocice;
        this.Duzina = Duzina;
        this.Promjer = Promjer;
        this.Kubikaza = Kubikaza;
        this.Total = Total;
        this.Roba = Roba;
        this.Boja = Boja;
    }

    @Generated(hash = 1400988416)
    public TrupciKlasa() {
    }
    public Long getID() {
        return this.ID;
    }
    public void setID(Long ID) {
        this.ID = ID;
    }
    public String getOpis() {
        return this.Opis;
    }
    public void setOpis(String Opis) {
        this.Opis = Opis;
    }
    public Date getDatum() {
        return this.Datum;
    }
    public void setDatum(Date Datum) {
        this.Datum = Datum;
    }
    public String getBrojPlocice() {
        return this.BrojPlocice;
    }
    public void setBrojPlocice(String BrojPlocice) {
        this.BrojPlocice = BrojPlocice;
    }
    public Float getDuzina() {
        return this.Duzina;
    }
    public void setDuzina(Float Duzina) {
        this.Duzina = Duzina;
    }
    public Float getPromjer() {
        return this.Promjer;
    }
    public void setPromjer(Float Promjer) {
        this.Promjer = Promjer;
    }
    public Float getKubikaza() {
        return this.Kubikaza;
    }
    public void setKubikaza(Float Kubikaza) {
        this.Kubikaza = Kubikaza;
    }
    public Float getTotal() {
        return this.Total;
    }
    public void setTotal(Float Total) {
        this.Total = Total;
    }
    public String getRoba() {
        return this.Roba;
    }
    public void setRoba(String Roba) {
        this.Roba = Roba;
    }
    public String getBoja() {
        return this.Boja;
    }
    public void setBoja(String Boja) {
        this.Boja = Boja;
    }
}
