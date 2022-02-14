package com.mbozic;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataBaseHelper extends SQLiteOpenHelper{
    public static final  String IMEBAZE = "Trupci4.db";
    public static final String IMETABLICE = "trupci_tablica";

    public static final String COL_1 = "ID";
    public static final String COL_2 = "OPIS";
    public static final String COL_3 = "DATUM";
    public static final String COL_4 = "BROJPLOCICE";
    public static final String COL_5 = "DUZINA";
    public static final String COL_6 = "PROMJER";
    public static final String COL_7 = "KUBIKAZA";
    public static final String COL_8 = "TOTAL";
    public static final String COL_9 = "ROBA";
    public static final String COL_10 = "BOJA";

    public DataBaseHelper(Context context) {
        super(context, IMEBAZE, null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + IMETABLICE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "OPIS TEXT, DATUM TEXT, BROJPLOCICE INTEGER, DUZINA REAL, PROMJER REAL, KUBIKAZA REAL, TOTAL REAL, ROBA TEXT, BOJA TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+IMETABLICE);
    }
    public boolean insertData(String opis,String datum,String brojplocice,String duzina,String promjer,String kubikaza,String total, String roba, String boja) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, opis);
        contentValues.put(COL_3, datum);
        contentValues.put(COL_4, brojplocice);
        contentValues.put(COL_5, duzina);
        contentValues.put(COL_6, promjer);
        contentValues.put(COL_7, kubikaza);
        contentValues.put(COL_8, total);
        contentValues.put(COL_9, roba);
        contentValues.put(COL_10, boja);
        long rezultat = db.insert(IMETABLICE, null, contentValues);
        db.close();
        if(rezultat==-1){
            return false;
        }
        else {
            return true;
        }
    }
    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor rezultat = db.rawQuery("Select opis, total, datum, id, sum(kubikaza) from trupci_tablica group by opis order by id DESC",null);
        return rezultat;
    }
    public Cursor pregled(final String praviopis){
        SQLiteDatabase db = this.getWritableDatabase();
        final String opis = praviopis;
        Cursor rezultat = db.rawQuery("Select brojplocice,kubikaza, roba, opis, duzina, promjer, total, datum, boja from trupci_tablica where opis = '"+opis+"'",null);
        return rezultat;
    }

    public Cursor total(final String praviopis){
        SQLiteDatabase db = this.getWritableDatabase();
        final String opis = praviopis;
        Cursor rezultat = db.rawQuery("Select sum(kubikaza), count(id) from trupci_tablica where opis = '"+opis+"'",null);
        return rezultat;
    }
    public Cursor opis(final String sid){
        SQLiteDatabase db = this.getWritableDatabase();
        final String ID = sid;
        Cursor rezultat = db.rawQuery("Select opis from trupci_tablica where id = '"+ID+"'" , null);
        return rezultat;
    }
    public Cursor pretraga(final String brpl){
        SQLiteDatabase db = this.getWritableDatabase();
        final String brojplocice = brpl;
        Cursor rezultat = db.rawQuery("Select brojplocice, opis, datum from trupci_tablica where brojplocice = '"+brojplocice+"'" , null);
        return rezultat;
    }

    public Boolean jeliRegistriranTrupac(final String unosPlocice){
        SQLiteDatabase db = this.getWritableDatabase();
        final String brojplocice = unosPlocice;
        Cursor rezultat = db.rawQuery("Select brojplocice, opis, datum from trupci_tablica where brojplocice = '"+brojplocice+"'" , null);

        return rezultat.getCount() > 0 ? true : false;
    }
}
