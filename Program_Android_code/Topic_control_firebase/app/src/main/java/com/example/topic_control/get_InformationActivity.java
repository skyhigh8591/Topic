package com.example.topic_control;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class get_InformationActivity extends AppCompatActivity {

    private EditText editTextGetRDIF,editTextGetName;
    private ListView listViewGetStock;
    private ArrayList<Map<String, String>> datalist;
    private Context context;
    private SimpleAdapter adapter;
    private DatabaseReference myFireBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get__information);
        setTitle("get_Information");

        context = this;

        editTextGetRDIF=(EditText)findViewById(R.id.editText_get_RDIF);
        editTextGetName=(EditText)findViewById(R.id.editText_get_name);

        listViewGetStock=(ListView)findViewById(R.id.listView_get_stock);
        datalist = new ArrayList<Map<String,String>>();
        datalist.clear();

        adapter= new SimpleAdapter(context,
                datalist,
                R.layout.get_item_layout,
                new String[]{"RFID","name","specification"},
                new int[] {R.id.textView_get_RFID,R.id.textView_get_name,R.id.textView_get_specification});

        listViewGetStock.setAdapter(adapter);

        myFireBase = FirebaseDatabase.getInstance().getReference("Topic");

        myFireBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                datalist.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Log.d("main","ds =  " + ds);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.get_menu, menu);
        return true;
    }







}
