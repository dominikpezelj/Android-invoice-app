package com.mbozic;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Dominik on 22.7.2018..
 */

public class UrediTrupceSwipe implements Parcelable {
    private Long ID;
    private String BrPlocice;
    private Float Duzina;
    private Float Promjer;
    private String Vrsta;
    private String Boja;
    private Date Datum;
    private String Opis;
    private Float Total;

    public UrediTrupceSwipe() {
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getBrPlocice() {
        return BrPlocice;
    }

    public void setBrPlocice(String brPlocice) {
        BrPlocice = brPlocice;
    }

    public Float getDuzina() {
        return Duzina;
    }

    public void setDuzina(Float duzina) {
        Duzina = duzina;
    }

    public Float getPromjer() {
        return Promjer;
    }

    public void setPromjer(Float promjer) {
        Promjer = promjer;
    }

    public String getVrsta() {
        return Vrsta;
    }

    public void setVrsta(String vrsta) {
        Vrsta = vrsta;
    }

    public String getBoja() {
        return Boja;
    }

    public void setBoja(String boja) {
        Boja = boja;
    }

    public Date getDatum() {
        return Datum;
    }

    public void setDatum(Date datum) {
        Datum = datum;
    }

    public String getOpis() {
        return Opis;
    }

    public void setOpis(String opis) {
        Opis = opis;
    }

    protected UrediTrupceSwipe(Parcel in) {
        ID = in.readByte() == 0x00 ? null : in.readLong();
        BrPlocice = in.readString();
        Duzina = in.readByte() == 0x00 ? null : in.readFloat();
        Promjer = in.readByte() == 0x00 ? null : in.readFloat();
        Vrsta = in.readString();
        Boja = in.readString();
        long tmpDatum = in.readLong();
        Datum = tmpDatum != -1 ? new Date(tmpDatum) : null;
        Opis = in.readString();
        Total = in.readByte() == 0x00 ? null : in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (ID == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(ID);
        }
        dest.writeString(BrPlocice);
        if (Duzina == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(Duzina);
        }
        if (Promjer == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(Promjer);
        }
        dest.writeString(Vrsta);
        dest.writeString(Boja);
        dest.writeLong(Datum != null ? Datum.getTime() : -1L);
        dest.writeString(Opis);
        if (Total == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(Total);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UrediTrupceSwipe> CREATOR = new Parcelable.Creator<UrediTrupceSwipe>() {
        @Override
        public UrediTrupceSwipe createFromParcel(Parcel in) {
            return new UrediTrupceSwipe(in);
        }

        @Override
        public UrediTrupceSwipe[] newArray(int size) {
            return new UrediTrupceSwipe[size];
        }
    };
}