package com.mbozic;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Trace;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/* import za pdf */
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
/* kraj pdf */

import java.util.ArrayList;


/**
 * Created by Dominik on 10.2.2018..
 */

public class Pregled  extends AppCompatActivity {
    DataBaseHelper myDb;
    private static final String TAG = "MainActivity";
    private TextView textView;
    String opis;
    String date;
    int rednibroj = 0;

    DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pregled);
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        //daoSession = DaoMaster.newDevSession(this, Constants.DATBASE_ORM);

        textView = (TextView) findViewById(R.id.dohvaceniid);
        baza();
        prikazopisa();


        /*btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });*/
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_izvoz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_izvoz) {
            izvozupdf();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String FILE = "";
    private void prikazopisa() {

        daoSession = DaoMaster.newDevSession(this, Constants.DATBASE_ORM);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            opis = bundle.getString("id", null); //Poslani input OPISA

            if(opis != null){
            }
            //final String sid=opis.substring(0,2);
            final String sid = opis;

            TrupciKlasa dohvatiOpisTrupca = daoSession.getTrupciKlasaDao().queryBuilder().where(TrupciKlasaDao.Properties.ID.eq(sid)).unique();

            //textView.setText(dohvatiOpisTrupca.getOpis());

            Long opisCounter = daoSession.getTrupciKlasaDao().queryBuilder().where(TrupciKlasaDao.Properties.ID.eq(sid)).count();

            if(opisCounter < 1){
                //Toast.makeText(this, "Nema podataka u bazi. Napravite novi unos!",Toast.LENGTH_LONG).show();
                startActivity(new Intent(Pregled.this, MainActivity.class));
            }else{
                TrupciKlasa trupci = daoSession.getTrupciKlasaDao().queryBuilder().where(TrupciKlasaDao.Properties.ID.eq(sid)).unique();
                String praviopis = trupci.getOpis();

                textView.setText(dohvatiOpisTrupca.getOpis());

                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"Otprema/");
                if(!file.exists()){
                    file.mkdirs();
                }

                FILE = file.getAbsolutePath() +"/"+praviopis+".pdf";
                final SwipeMenuListView listView = (SwipeMenuListView) findViewById(R.id.idListView);
                final ArrayList<String> theList = new ArrayList<>();
                final ArrayList<Long> theListIDs = new ArrayList<>();

                Long trupciOpisCounter = daoSession.getTrupciKlasaDao().queryBuilder().where(TrupciKlasaDao.Properties.Opis.eq(praviopis)).count();

                if(trupciOpisCounter < 1){
                    Toast.makeText(this, "Nema podataka u bazi. Napravite novi unos!",Toast.LENGTH_LONG).show();
                }else {
                    final java.util.List<TrupciKlasa> trupciOpis = daoSession.getTrupciKlasaDao().queryBuilder().where(TrupciKlasaDao.Properties.Opis.eq(praviopis)).list();

                    for (TrupciKlasa tk : trupciOpis) {
                        theList.add("\nBr. pločice: "+tk.getBrojPlocice() + "\nDužina: " + tk.getDuzina() + "\nPromjer: " + tk.getPromjer() +"\nKubikaža: " + tk.getKubikaza() + "\nVrsta: " + tk.getRoba() +"\nBoja pločice: " + tk.getBoja() +"\n");
                        theListIDs.add(tk.getID());
                    }

                    final ArrayAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,theList);
                    listView.setAdapter(listAdapter);
                    SwipeMenuCreator creator = new SwipeMenuCreator() {

                        @Override
                        public void create(SwipeMenu menu) {
                    // create "open" item
                    SwipeMenuItem openItem = new SwipeMenuItem(
                            getApplicationContext());
                    // set item background
                    openItem.setBackground(new ColorDrawable(Color.rgb(0xc6, 0xc6,
                            0xc6)));
                    // set item width
                    openItem.setWidth(170);
                    // set item title
                    openItem.setTitle("Uredi");
                    // set item title fontsize
                    openItem.setTitleSize(18);
                    // set item title font color
                    openItem.setTitleColor(Color.WHITE);
                    // add to menu
                    menu.addMenuItem(openItem);

                            // create "delete" item
                            SwipeMenuItem deleteItem = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xFf,
                                    0x00, 0x00)));
                            // set item width
                            deleteItem.setWidth(170);
                            // set a icon
                            deleteItem.setTitle("Izbriši");
                            deleteItem.setTitleSize(18);
                            deleteItem.setTitleColor(Color.WHITE);
                            // add to menu
                            menu.addMenuItem(deleteItem);
                        }
                    };

// set creator
                    listView.setMenuCreator(creator);

                    listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                            switch (index) {
                                case 0:
                                    Log.d(TAG, "onMenuItemClick: clicked item" + index);
                                    TrupciKlasa tkk = trupciOpis.get(position);

                                    UrediTrupceSwipe uts = new UrediTrupceSwipe();
                                    uts.setID(tkk.getID());
                                    uts.setBrPlocice(tkk.getBrojPlocice());
                                    uts.setBoja(tkk.getBoja());
                                    uts.setDuzina(tkk.getDuzina());
                                    uts.setPromjer(tkk.getPromjer());
                                    uts.setVrsta(tkk.getRoba());
                                    uts.setDatum(tkk.getDatum());
                                    uts.setOpis(tkk.getOpis());

                                    Intent intent = new Intent(Pregled.this, Upisi.class);
                                    intent.putExtra("UrediTrupac", uts);
                                    startActivity(intent);

                                    break;
                                case 1:
                                    Log.d(TAG, "onMenuItemClick: clicked item" + index);

                                    Long trupacID = theListIDs.get(position);
                                    TrupciKlasa tk = new TrupciKlasa();
                                    tk.setID(trupacID);
                                    daoSession.getTrupciKlasaDao().delete(tk);
                                    theList.remove(position);
                                    listAdapter.notifyDataSetChanged();


                                    listView.setCloseInterpolator(new BounceInterpolator());
                                    //Toast.makeText(Pregled.this, "Izbrisan je broj plocice: "+inde, Toast.LENGTH_SHORT).show();
                                    Intent intent2 = new Intent(Pregled.this, Pregled.class);
                                    intent2.putExtra("id", opis);
                                    startActivity(intent2);
                                    finish();
                                    break;
                            }
                            // false : close the menu; true : not close the menu
                            return false;
                        }
                    });
                }
            }

            
        }
    }
    private void baza() {
        myDb = new DataBaseHelper(this);

    }


   
   String danasnjidatum = null;
    private  void createTable(Document document) throws BadElementException {

        Bundle bundle = getIntent().getExtras();
        try {

            final Calendar cal = Calendar.getInstance();
            final int dd;
            final int mm;
            final int yy;
            dd = cal.get(Calendar.DAY_OF_MONTH);
            mm = cal.get(Calendar.MONTH);
            yy = cal.get(Calendar.YEAR);
            danasnjidatum = String.valueOf(new StringBuilder()
                    .append(dd).append("").append(".").append(mm + 1).append(".")
                    .append(yy).append("."));

            if (bundle != null) {
                opis = bundle.getString("id"); //Poslani input OPISA
                //final String sid = opis.substring(0, 2);
                String sid = opis;
                //textView.setText(sid);


                TrupciKlasa dohvatiOpisTrupca = daoSession.getTrupciKlasaDao().queryBuilder().where(TrupciKlasaDao.Properties.ID.eq(sid)).unique();
                Long opisCounter = daoSession.getTrupciKlasaDao().queryBuilder().where(TrupciKlasaDao.Properties.ID.eq(sid)).count();

                if (opisCounter < 1) {
                    Toast.makeText(this, "Nema podataka u bazi. Napravite novi unos!", Toast.LENGTH_LONG).show();
                } else {
                    TrupciKlasa trupci = daoSession.getTrupciKlasaDao().queryBuilder().where(TrupciKlasaDao.Properties.ID.eq(sid)).unique();
                    String praviopis = trupci.getOpis();

                    final ListView listView = (ListView) findViewById(R.id.idListView);
                    ArrayList<String> theList = new ArrayList<>();

                    Long trupciOpisCounter = daoSession.getTrupciKlasaDao().queryBuilder().where(TrupciKlasaDao.Properties.Opis.eq(praviopis)).count();

                    if (trupciOpisCounter < 1) {
                        Toast.makeText(this, "Nema podataka u bazi. Napravite novi unos!", Toast.LENGTH_LONG).show();
                    } else {
                        document.add(new Paragraph("\n"));
                        Paragraph datum = new Paragraph("           Datum ispisa: " + danasnjidatum);
                        document.add(new Paragraph("\n\n\n"));
                        datum.setAlignment(Element.ALIGN_LEFT);

                        document.add(datum);
                        Paragraph naslov = new Paragraph("SPECIFIKACIJA TRUPACA");
                        naslov.setAlignment(Element.ALIGN_CENTER);
                        document.add(naslov);
                        document.add(new Paragraph("\n\n"));

                        PdfPTable table = new PdfPTable(7);

                        PdfPCell c1 = new PdfPCell(new Phrase("No."));
                        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        c1.setFixedHeight(20);
                        table.addCell(c1);

                        c1 = new PdfPCell(new Phrase("Broj pl."));
                        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        c1.setFixedHeight(20);
                        table.addCell(c1);

                        c1 = new PdfPCell(new Phrase("Klasa"));
                        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        c1.setFixedHeight(20);
                        table.addCell(c1);

                        c1 = new PdfPCell(new Phrase("Boja pl."));
                        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        c1.setFixedHeight(20);
                        table.addCell(c1);

                        c1 = new PdfPCell(new Phrase("Dužina"));
                        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        c1.setFixedHeight(20);
                        table.addCell(c1);

                        c1 = new PdfPCell(new Phrase("Promjer"));
                        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        c1.setFixedHeight(20);
                        table.addCell(c1);

                        c1 = new PdfPCell(new Phrase("Kubikaža"));
                        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        c1.setFixedHeight(20);
                        table.addCell(c1);
                        table.setHeaderRows(1);

                        java.util.List<TrupciKlasa> trupciOpisLista = daoSession.getTrupciKlasaDao().queryBuilder().where(TrupciKlasaDao.Properties.Opis.eq(praviopis)).list();

                        Float sumiranaKubikaza = 0f;
                        Integer zbrojTrupaca = 0;

                        for (TrupciKlasa tk : trupciOpisLista) {
                            rednibroj++;
                            table.addCell(String.valueOf(rednibroj));
                            table.addCell(tk.getBrojPlocice());
                            table.addCell(tk.getRoba());
                            table.addCell(tk.getBoja());
                            table.addCell(tk.getDuzina().toString());
                            table.addCell(tk.getPromjer().toString());
                            table.addCell(tk.getKubikaza().toString());

                            sumiranaKubikaza += tk.getKubikaza();
                            zbrojTrupaca += 1;
                        }

                        document.add(table);

                        document.add(new Paragraph("\n\n"));
                        Paragraph total = new Paragraph("       Ukupna kubikaža: " + Math.round(sumiranaKubikaza*100.0)/100.0);
                        total.setAlignment(Element.ALIGN_LEFT);
                        document.add(total);
                        Paragraph zbrojtrupaca = new Paragraph("       Ukupno trupaca: " + zbrojTrupaca);
                        total.setAlignment(Element.ALIGN_LEFT);
                        document.add(zbrojtrupaca);
                    }

                }


            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void izvozupdf() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Document document = new Document();
            document.setMargins(20, 20, 50, 50);
            PdfWriter writer = PdfWriter.getInstance(document,  new FileOutputStream(FILE));
            //PdfWriter.getInstance(document, new FileOutputStream(FILE));
            HeaderFooterPageEvent event = new HeaderFooterPageEvent(this);
            writer.setPageEvent(event);
            //writer.setMarginMirroringTopBottom(true);
            //writer.setMarginMirroring(true);
            writer.setMargins(50,50,50,50);
            document.open();

            createTable(document);
            //addLogo(document);
            rednibroj = 0;
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

