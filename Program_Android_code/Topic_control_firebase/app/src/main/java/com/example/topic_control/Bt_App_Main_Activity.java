package com.example.topic_control;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Bt_App_Main_Activity extends AppCompatActivity {

    private static final int BT_REQUEST_CODE = 100;
    private Context context;
    private ListView listView;
    private BluetoothAdapter btAdapter;
    private Intent intent;
    private Set<BluetoothDevice> btDeviceList;
    private List<String> btNameList;
    private ArrayAdapter<String> adapter;
    private String itemData;
    private final int RFIDControl=4;
    private static int mode;
    private String getSetActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt__app__main_);

        context = this;
        setTitle("BT APP");
        mode = RFIDControl;


        Intent intentActivity = getIntent();
        getSetActivity = intentActivity.getStringExtra("activity");
        Log.d("main", "BTgetSetActivity =  " + getSetActivity);

        listView = (ListView)findViewById(R.id.listView_bt_app_RFID);
        btAdapter = BluetoothAdapter.getDefaultAdapter();

        if(btAdapter==null){
            Toast.makeText(context,"No BT hardware",Toast.LENGTH_SHORT).show();
            finish();
        }else if(!btAdapter.isEnabled()){
            intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent,BT_REQUEST_CODE);
        }


    }

    protected void onResume() {
        super.onResume();
        btDeviceList = btAdapter.getBondedDevices();
        btNameList = new ArrayList<String>();
        if(btDeviceList.size()>0){
            for(BluetoothDevice device : btDeviceList){
                String message = device.getName() + "\n" + device.getAddress();
                btNameList.add(message);
            }
            adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, btNameList);
            listView.setAdapter(adapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d("main", "position =  " + position);

                itemData = parent.getItemAtPosition(position).toString();

                Log.d("main", "itemData =  " + itemData);

                intent = new Intent(context, ReturnActivity.class);
                intent.putExtra("btdata", itemData);
                intent.putExtra("BTactivity",getSetActivity);
                startActivity(intent);


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode , int resultCode , @Nullable Intent data) {
        super.onActivityResult(requestCode , resultCode , data);

        if(requestCode == BT_REQUEST_CODE){
            if(requestCode == RESULT_CANCELED){
                Toast.makeText(context,"User refuse to enable BT.",Toast.LENGTH_SHORT).show();
                finish();
            }else if(requestCode == RESULT_OK ){
                Toast.makeText(context,"BT is enabled.",Toast.LENGTH_SHORT).show();
            }
        }

    }
}