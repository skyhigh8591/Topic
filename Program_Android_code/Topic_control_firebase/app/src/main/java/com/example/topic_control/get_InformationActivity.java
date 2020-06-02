package com.example.topic_control;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    private Handler myhandler;
    private Button buttonGetInquire;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get__information);
        setTitle("get_Information");

        context = this;

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        editTextGetRDIF = (EditText) findViewById(R.id.editText_get_RDIF);
//        editTextGetName = (EditText) findViewById(R.id.editText_get_name);

        editTextGetRDIF.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                get_InformationActivity.this.adapter.getFilter().filter(arg0);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });


        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        buttonGetInquire = (Button) findViewById(R.id.button_get_Inquire);
        buttonGetInquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextGetRDIF.setText("");
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

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.get_new:
                message = "你是否選擇出庫";

                break;
            case R.id.get_break:
                new AlertDialog.Builder(context)
                        .setTitle("離開此頁面")
                        .setMessage("你確定要離開？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
