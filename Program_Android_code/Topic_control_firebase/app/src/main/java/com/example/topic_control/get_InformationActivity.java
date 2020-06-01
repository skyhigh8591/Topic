package com.example.topic_control;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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
import java.util.List;
import java.util.Map;

public class get_InformationActivity extends AppCompatActivity {

    private EditText editTextGetRDIF, editTextGetName;
    private ListView listViewGetStock;
    private ArrayList<Map<String, String>> datalist;
    private Context context;
    private SimpleAdapter adapter;
    private DatabaseReference myFireBase;
    private HashMap<String, String> mapData;
    private Runnable eChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get__information);
        setTitle("get_Information");

        context = this;

        editTextGetRDIF = (EditText) findViewById(R.id.editText_get_RDIF);
        editTextGetName = (EditText) findViewById(R.id.editText_get_name);

        editTextGetRDIF.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String name = s.toString();
                changeClient(name);

            }

            private void changeClient(String name) {


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        listViewGetStock = (ListView) findViewById(R.id.listView_get_stock);
        datalist = new ArrayList<Map<String, String>>();
        datalist.clear();

        adapter = new SimpleAdapter(context,
                datalist,
                R.layout.get_item_layout,
                new String[]{"RFID", "name", "specification", "number"},
                new int[]{R.id.textView_get_RFID, R.id.textView_get_name, R.id.textView_get_specification, R.id.textView_get_item_number});

        listViewGetStock.setAdapter(adapter);
        myFireBase = FirebaseDatabase.getInstance().getReference("Topic/RFID");
        myFireBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                datalist.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d("main", "ds =  " + ds);
                    mapData = new HashMap<String, String>();
                    String rfidData = (String) ds.getKey();
                    String rfidGet = rfidData.replace("_", ".");
                    Log.d("main", "RFIDData =  " + rfidData);
                    if (rfidData == null)
                        mapData.put("RFID", "NO RFID");
                    else
                        mapData.put("RFID", rfidGet);

                    String nameData = (String) ds.child("name").getValue();
                    Log.d("main", "name =  " + nameData);
                    if (nameData == null)
                        mapData.put("name", "no name");
                    else
                        mapData.put("name", nameData);

                    String specificationData = (String) ds.child("specification").getValue();
                    Log.d("main", "specification =  " + nameData);
                    if (specificationData == null)
                        mapData.put("specification", "no specification");
                    else
                        mapData.put("specification", specificationData);

                    String numberData = (String) ds.child("number").getValue();
                    Log.d("main", "number =  " + numberData);
                    if (numberData == null)
                        mapData.put("number", "no number");
                    else
                        mapData.put("number", numberData);
                    datalist.add(mapData);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.get_menu, menu);
        return true;
    }


    private class ShopClient {
    }

    private class DataSupport {
    }
}
