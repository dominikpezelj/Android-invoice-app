package com.mbozic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.text.Editable;
import android.view.View;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class UpisActivity extends AppCompatActivity {
    private EditText editText;
    private Button btnSave;
    String opis;
    String date;

    DaoSession daoSession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upis_activity);
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        daoSession = DaoMaster.newDevSession(this, Constants.DATBASE_ORM);

        final EditText Datum;
        Datum = (EditText) findViewById(R.id.datum);
        final Calendar cal = Calendar.getInstance();
        final int dd;
        final int mm;
        final int yy;
        final int hh;
        final int min;

        dd = cal.get(Calendar.DAY_OF_MONTH);
        mm = cal.get(Calendar.MONTH);
        yy = cal.get(Calendar.YEAR);
        hh = cal.get(Calendar.HOUR_OF_DAY);
        min = cal.get(Calendar.MINUTE);
        Datum.setText(new StringBuilder()
                .append(dd).append("").append(".").append(mm + 1).append(".")
                .append(yy).append(".").append(" u ").append(hh).append(":").append(min));

        editText = (EditText) findViewById(R.id.edit_title);

        btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                date = new StringBuilder()
                        .append(dd).append("").append(".").append(mm + 1).append(".")
                        .append(yy).append(".").append(" ").append(hh).append(":").append(min).toString();

                opis = editText.getText().toString();
                if (opis.isEmpty()) {
                    Toast.makeText(v.getContext(), "Unesite opis!", Toast.LENGTH_SHORT).show();
                }
                else{
                Intent intent = new Intent(v.getContext(), Upisi.class);
                intent.putExtra("Opis", opis);
                intent.putExtra("Datum", date);
                startActivity(intent);
                }
                /*startActivity(new Intent(v.getContext(), Upisi.class));*/ //STARI NACIN START ACTIVITY-A
            }
        });




    }




}
