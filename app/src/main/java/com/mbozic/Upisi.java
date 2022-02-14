package com.mbozic;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.content.Intent;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Upisi extends AppCompatActivity {
    Button btnSave;
    Spinner spinner;
    Spinner spinnerzaboju;
    //Definirano za slanje
    TextView editText;
    TextView date;
    String dan;
    String roba;
    String boja;
    String opis;
    // kraj

    EditText promjer;
    EditText duzina;
    EditText kubikaza;
    TextView total;
    EditText brojplocice;
    int brpl;
    double kub;
    double tot;

    double a,b,res, roundres;
    double pi = 3.14;

    DataBaseHelper myDb;

    DaoSession daoSession;

    UrediTrupceSwipe uts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upisi);
        myDb = new DataBaseHelper(this);
        daoSession = DaoMaster.newDevSession(this, Constants.DATBASE_ORM);

        if(getIntent().getExtras().containsKey("UrediTrupac")){
            uts = (UrediTrupceSwipe) getIntent().getExtras().get("UrediTrupac");
        }

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        promjer = (EditText)findViewById(R.id.promjer);
        duzina = (EditText)findViewById(R.id.duzina);
        brojplocice = (EditText) findViewById(R.id.brojplocice);
        total = (TextView) findViewById(R.id.total);
        editText = (TextView) findViewById(R.id.opis);
        date = (TextView) findViewById(R.id.date);
        kubikaza = (EditText)findViewById(R.id.kubikaza);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinnerzaboju = (Spinner) findViewById(R.id.spinnerzaboju);
        btnSave = (Button) findViewById(R.id.btn_save);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            opis = bundle.getString("Opis"); //Poslani input OPISA
            editText.setText(opis);
            dan = bundle.getString("Datum");
            date.setText(dan);
        }

        if(uts != null){
            brojplocice.setText(uts.getBrPlocice());

            int duzinaBroj = Math.round(uts.getDuzina());
            int promjerBroj = Math.round(uts.getPromjer());

            duzina.setText(duzinaBroj+"");
            promjer.setText(promjerBroj+"");

            if (!promjer.getText().toString().isEmpty() && !duzina.getText().toString().isEmpty()){
                a = Double.parseDouble(duzina.getText().toString());
                b = Double.parseDouble(promjer.getText().toString());
                b = b/2;
                res = b*b*pi*a;
                res = res/1000000;
                roundres = Math.round(res*100.0)/100.0;
                kubikaza.setText(String.valueOf(roundres));
            }
        }

        brojplocice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //String s = editText.getText().toString();
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                //
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        duzina.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!promjer.getText().toString().isEmpty() && !duzina.getText().toString().isEmpty()){
                    a = Double.parseDouble(duzina.getText().toString());
                    b = Double.parseDouble(promjer.getText().toString());
                    b = b/2;
                    res = b*b*pi*a;
                    res = res/1000000;
                    roundres = Math.round(res*100.0)/100.0;
                    kubikaza.setText(String.valueOf(roundres));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });

        promjer.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(@NonNull Editable s) {
                if (!promjer.getText().toString().isEmpty() && !duzina.getText().toString().isEmpty()){
                    a = Double.parseDouble(duzina.getText().toString());
                    b = Double.parseDouble(promjer.getText().toString());
                    b = b/2;
                    res = b*b*pi*a;
                    res = res/1000000;
                    roundres = Math.round(res*100.0)/100.0;
                    kubikaza.setText(String.valueOf(roundres));
                }




            }
        });



        String [] vrstarobe = {" ","Hrast lužnjak", "Hrast kitnjak","Hrast cer, medunac i ost.", "Bukva obicna - preborana",
                "Jasen poljski i ost.", "Javor gorski", "Javor ostali", "Brijest", "Grab obicni", "Kesten", "Bagrem", "Trešnja",
                "Kruška i ostale vockarice", "Breza obicna", "Euroamericke topole", "Domace topole", "Vrba", "Jela", "Smreka",
                "Crni bor", "Borovac i ostali", "Ariš", "Bukva obicna", "Jela-okorana", "Smreka-okorana"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,vrstarobe);
        spinner.setAdapter(adapter);

        if(uts != null){
            int brojacVrsteRobe = 0;
            for(String roba : vrstarobe){
                if(roba.equals(uts.getVrsta())){
                    break;
                }
                brojacVrsteRobe++;
            }
            spinner.setSelection(brojacVrsteRobe);
        }

        String [] bojaplocice = {" ","CRNA", "BIJELA","CRVENA", "SMEDA", "LJUBICASTA", "PLAVA", "ZELENA", "NARANCASTA", "ŽUTA"};
        ArrayAdapter<String> adapterzaboje = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,bojaplocice);
        spinnerzaboju.setAdapter(adapterzaboje);

        if(uts != null){
            int brojacBoje = 0;
            for(String boja : bojaplocice){
                if(boja.equals(uts.getBoja())){
                    break;
                }
                brojacBoje++;
            }
            spinnerzaboju.setSelection(brojacBoje);
        }

        /*vrstarobe = String.valueOf(adapter);*/



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String brplo = null;

                if (kubikaza.getText().toString().isEmpty() || promjer.getText().toString().isEmpty() || duzina.getText().toString().isEmpty() || brojplocice.getText().toString().isEmpty()){
                    Toast.makeText(Upisi.this, "Unesite sve vrijednosti!", Toast.LENGTH_SHORT).show();
                }else {
                    brplo = String.valueOf(brpl);

                    kub = Double.parseDouble(kubikaza.getText().toString());
                    tot += kub;
                    double roundtot = Math.round(tot*100.0)/100.0;
                    brpl = Integer.parseInt( brojplocice.getText().toString() );
                    total.setText(String.valueOf(roundtot));
                    roba = spinner.getSelectedItem().toString();
                    boja = spinnerzaboju.getSelectedItem().toString();

                    spremanje();
                }

                if (total != null && brplo != null) {
                    kubikaza.setText(String.valueOf(""));
                    promjer.setText(String.valueOf(""));
                    duzina.setText(String.valueOf(""));


                    duzina.requestFocus();
                    brpl++;

                    brplo = String.valueOf(brpl);
                    brojplocice.setText(brplo);

                }


            }
        });

    }



    private void spremanje() {
        if (!promjer.getText().toString().isEmpty() && !duzina.getText().toString().isEmpty()){
            a = Double.parseDouble(duzina.getText().toString());
            b = Double.parseDouble(promjer.getText().toString());
            b = b/2;
            res = b*b*pi*a;
            res = res/1000000;
            roundres = Math.round(res*100.0)/100.0;
            kubikaza.setText(String.valueOf(roundres));
        }

        String desc = opis;
        /*String datum = String.valueOf(date);*/
        String brojplocice = String.valueOf(brpl);
        String duzinaa = String.valueOf(a);
        double promje = b * 2;
        String promjerr = String.valueOf(promje);
        String kubikaza = String.valueOf(roundres);
        String total = String.valueOf(tot);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        TrupciKlasa trupciKlasa = new TrupciKlasa();
        try {
            trupciKlasa.setOpis(desc);
            if(uts != null){
                trupciKlasa.setDatum(uts.getDatum());
                trupciKlasa.setOpis(uts.getOpis());
            }else {
                SimpleDateFormat sdfWithTime = new SimpleDateFormat("dd.MM.yyyy. HH:mm");
                trupciKlasa.setDatum(sdfWithTime.parse(date.getText().toString()));
            }
            trupciKlasa.setBrojPlocice(brojplocice);
            trupciKlasa.setDuzina(Float.parseFloat(duzinaa));
            trupciKlasa.setPromjer(Float.parseFloat(promjerr));
            trupciKlasa.setKubikaza(Float.parseFloat(kubikaza));
            trupciKlasa.setTotal(Float.parseFloat(total));
            trupciKlasa.setRoba(roba);
            trupciKlasa.setBoja(boja);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(uts != null){
            trupciKlasa.setID(uts.getID());
            daoSession.getTrupciKlasaDao().update(trupciKlasa);

            //if (update > 0) {
                Toast.makeText(this, "Unos uspješan updatan!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Upisi.this, Pregled.class);
                intent.putExtra("id", String.valueOf(trupciKlasa.getID()));

                startActivity(intent);
            //} else {
               // Toast.makeText(this, "Unos NIJE uspješan!", Toast.LENGTH_SHORT).show();
            //}
        }else {
            long unos = daoSession.getTrupciKlasaDao().insert(trupciKlasa);

            if (unos > 0) {
                Toast.makeText(this, "Unos uspješan!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Unos NIJE uspješan!", Toast.LENGTH_SHORT).show();
            }
        }

        /*Boolean rezultat = myDb.insertData(desc, date.getText().toString(), brojplocice, duzina, promjer, kubikaza, total, roba, boja);
        if (rezultat == true) {
            Toast.makeText(this, "Unos uspješan!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Unos NIJE uspješan!", Toast.LENGTH_SHORT).show();
        }*/


    }




}