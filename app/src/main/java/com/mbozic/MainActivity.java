package com.mbozic;

//dialog
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
//imei u dialogu
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
//
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.MenuItem;
import android.content.Intent;
import android.database.Cursor;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//card view

//menu u bottomu.
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.greenrobot.greendao.query.WhereCondition;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    DataBaseHelper myDb;
    private static final String TAG = "MainActivity";
    TextView izbaze;
    /*Button otprema;*/

    //DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        //daoSession = DaoMaster.newDevSession(this, Constants.DATBASE_ORM);
        baza();
        menudole();
        prikaz();



        plus();
        isStoragePermissionGranted();

    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("main","Permission is granted");
                return true;
            } else {

                Log.v("main","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("main","Permission is granted");
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        prikaz();
    }



    private void baza() {
        myDb = new DataBaseHelper(this);

    }
    private void plus() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), UpisActivity.class));
            }
        });
    }

    private void menudole() {
        final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.menudole);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.edit:
                        startActivity(new Intent(MainActivity.this, PretragaActivity.class));
                        break;
                    case R.id.export:

                        break;
                    case R.id.info:
                        adminlogin();
                        break;
                }
                return true;
            }
        });
    }
    private void adminlogin(){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(false);
        builder.setTitle("PRIJAVA ADMINISTRATORA");
        builder.setMessage("Unesite lozinku: ");
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
        builder.setPositiveButton("Prijava", new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialog, int which) {
            String pass = input.getText().toString();
            if (pass == null || pass.equals("")) {
                Toast.makeText(MainActivity.this, "Unesite lozinku!", Toast.LENGTH_SHORT).show();
            } else {

                if (pass.equals("mbozic")) {
                    Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Unesite ispravnu lozinku!", Toast.LENGTH_SHORT).show();
                }

            }
        }
        }
        );
        builder.create().show();
    }
    private void prikaz() {
        final DaoSession daoSession;
        daoSession = DaoMaster.newDevSession(this, Constants.DATBASE_ORM);

        final SwipeMenuListView listView = (SwipeMenuListView) findViewById(R.id.idListView);
        final ArrayList<String> theList = new ArrayList<>();
        final ArrayList<String> theListCopyData = new ArrayList<>();

        long zbrojPodataka = daoSession.getTrupciKlasaDao().count();
        List<TrupciKlasa> dohvatiTrupce = daoSession.getTrupciKlasaDao().queryBuilder().where(new WhereCondition.StringCondition("1 GROUP BY Opis")).orderDesc(TrupciKlasaDao.Properties.ID).build().list();

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

        if(zbrojPodataka > 0){
            listView.setVisibility(View.VISIBLE);

            List<TrupciKlasa> dohvatiSvePodatke = daoSession.getTrupciKlasaDao().loadAll();

            HashMap<String, Float> mapa = new HashMap<>();

            for(TrupciKlasa tk : dohvatiSvePodatke){

                if(mapa.containsKey(tk.getOpis())){
                    Float podataka = mapa.get(tk.getOpis());
                    podataka += tk.getKubikaza();
                    mapa.put(tk.getOpis(), podataka);
                }else {
                    mapa.put(tk.getOpis(), tk.getKubikaza());
                }
            }

            theList.clear();
            theListCopyData.clear();

            for(TrupciKlasa tk : dohvatiTrupce){
              theList.add("\nOPIS: "+tk.getOpis() + "\nDATUM: "+sdf.format(tk.getDatum()) +" u "+ sdf2.format(tk.getDatum())+"\nTOTAL: "+Math.round(mapa.get(tk.getOpis())*100.0)/100.0+" \n");
              theListCopyData.add(tk.getID()+"\nOPIS: "+tk.getOpis() + "\nDATUM: "+sdf.format(tk.getDatum()) +" u "+ sdf2.format(tk.getDatum())+"\nTOTAL: "+Math.round(mapa.get(tk.getOpis())*100.0)/100.0+" \n");
            }

            final ArrayAdapter listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,theList);
            listView.setAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();


            SwipeMenuCreator creator = new SwipeMenuCreator() {

                @Override
                public void create(SwipeMenu menu) {

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
                            Log.d(TAG, "onMenuItemClick: clicked item" + index+ " "+position);
                            String podatakIzListe = theList.get(position);
                            Log.d("TAG", podatakIzListe);
                            String[] splitPodatakLista = podatakIzListe.split("\n");
                            String dohvatiOpis = splitPodatakLista[1].substring(6, splitPodatakLista[1].length());

                            List<TrupciKlasa> dohvatiSveTrupePoOpisu = daoSession
                                    .getTrupciKlasaDao()
                                    .queryBuilder()
                                    .where(TrupciKlasaDao.Properties.Opis.eq(dohvatiOpis))
                                    .list();

                            for(TrupciKlasa tk : dohvatiSveTrupePoOpisu){
                                daoSession.getTrupciKlasaDao().delete(tk);
                            }
                            theList.remove(position);
                            listAdapter.notifyDataSetChanged();


                            System.out.println("");
                            break;
                    }
                    // false : close the menu; true : not close the menu
                    return false;
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //String UserInfo = listView.getItemAtPosition(position).toString();
                    String UserInfo = theListCopyData.get(position);


                    String userId = UserInfo.substring(0, UserInfo.indexOf("\n"));
                    Intent intent = new Intent(view.getContext(), Pregled.class);
                    intent.putExtra("id", userId);
                    startActivity(intent);

                }
            });
        }else {
            listView.setVisibility(View.GONE);
            Toast.makeText(this, "Nema podataka u bazi. Napravite novi unos!",Toast.LENGTH_LONG).show();
        }

        }

    }




