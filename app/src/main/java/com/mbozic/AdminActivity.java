package com.mbozic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class AdminActivity extends AppCompatActivity {
    private EditText editText;
    private Button btnDeleteAll;
    String opis;
    String date;

    private TextView textView;
    DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        daoSession = DaoMaster.newDevSession(this, Constants.DATBASE_ORM);



        btnDeleteAll = (Button) findViewById(R.id.btn_save);
        btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            brisanjebaze();
            }
        });




    }
    private void brisanjebaze(){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(false);
        builder.setTitle("BRISANJE SVIH PODATAKA IZ BAZE");
        builder.setMessage("Jeste li sigurni da želite izbrisati sve podatke iz baze?");

        builder.setPositiveButton("DA!", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        daoSession.getTrupciKlasaDao().deleteAll();
                        Toast.makeText(AdminActivity.this, "Baza podataka je izbrisana.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        builder.setNegativeButton("NE!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }









    }



