package com.example.topic_control;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class get_change_Activity extends AppCompatActivity {

    private Context context;
    private String RFID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_change_);

        context = this;
        Intent intent = getIntent();
        RFID = intent.getStringExtra("RFID");
        Log.d("main","RFID =" + RFID);
    }
}
