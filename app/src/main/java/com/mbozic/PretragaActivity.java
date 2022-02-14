package com.mbozic;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.text.TextWatcher;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class PretragaActivity extends AppCompatActivity {
    private EditText editText;
    private Button btnSave;
    String opis;
    String date;
    DataBaseHelper myDb;
    private TextView textView;
    DaoSession daoSession;

    String brpl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pretraga_activity);
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        daoSession = DaoMaster.newDevSession(this, Constants.DATBASE_ORM);

        editText = (EditText) findViewById(R.id.edit_pretraga);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //String s = editText.getText().toString();
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brpl = editText.getText().toString();
                prikazpretrage();

            }
        });

        baza();




    }

    private void prikazpretrage() {

        final ListView listView = (ListView) findViewById(R.id.idListView);
        ArrayList<String> theList = new ArrayList<>();

        long zbrojPodataka = daoSession.getTrupciKlasaDao().queryBuilder().where(TrupciKlasaDao.Properties.BrojPlocice.like("%" + brpl + "%")).count();
        List <TrupciKlasa> dohvatiTrupcePoBrPl = daoSession.getTrupciKlasaDao().queryBuilder().where(TrupciKlasaDao.Properties.BrojPlocice.like(brpl)).list();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");


        if(zbrojPodataka > 0){
            for(TrupciKlasa tk : dohvatiTrupcePoBrPl){
                theList.add(tk.getID()+"\nBR.PLOCICE:"+tk.getBrojPlocice()+"\nOPIS: "+tk.getOpis() + "\nDATUM: "+sdf.format(tk.getDatum()) +"\nKUBIKAZA: "+tk.getKubikaza()+" \n");
            }

            final ArrayAdapter listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,theList);
            listView.setAdapter(listAdapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String UserInfo = listView.getItemAtPosition(position).toString();
                    String userId = UserInfo.substring(0, UserInfo.indexOf("\n"));
                    //Toast.makeText(PretragaActivity.this, "Kliknut je id:"+userId, Toast.LENGTH_LONG).show();

                    //String UserInfo = listView.getItemAtPosition(position).toString();

                    //String userId = UserInfo.substring(0, UserInfo.indexOf("\n"));
                    Intent intent = new Intent(view.getContext(), Pregled.class);
                    intent.putExtra("id", userId);
                    startActivity(intent);

                }
            });
        }else {
            Toast.makeText(this, "Nema podataka u bazi. Unesite ispravan broj pločice!",Toast.LENGTH_LONG).show();
        }








    }

    private void baza() {
        myDb = new DataBaseHelper(this);

    }

}
