package com.example.topic_control;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class ReturnActivity extends AppCompatActivity {

    private static final String TAG = "RFID";
    private Context context;
    private String btData;
    private BluetoothAdapter btAapter;
    private BTChatService myChatService;
    private String btMacAddress;
    private BluetoothDevice remoteDevice;
    private Button buttonLink;
    private TextView textViewBT;
    private EditText editTestData;
    private Intent intent;
    private String getBtRFID;
    private String getSetActivity;
    private Class<set_InformationActivity> s1;
    private String getOutCheckRFID;
    private String getOutChangeNumber;
    private DatabaseReference myFireBase;
    private int n;
    private int setnumber;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);

        setTitle("RFID Control");

        context = this;
        Intent intent = getIntent();
        btData = intent.getStringExtra("btdata");
        getSetActivity = intent.getStringExtra("BTactivity");
        getOutCheckRFID = intent.getStringExtra("getOutCheckRFID");
        getOutChangeNumber = intent.getStringExtra("getOutChangeNumber");

        textViewBT = (TextView) findViewById(R.id.textView_RFIDBT);
        textViewBT.setText(btData);

        editTestData = (EditText) findViewById(R.id.editText_return_RFID);


        buttonLink = (Button) findViewById(R.id.button_RFIDLink);

        btAapter = BluetoothAdapter.getDefaultAdapter();
        myChatService = new BTChatService(context, myHandler);

        if (btData != null) {
            Toast.makeText(context, "連線藍芽中", Toast.LENGTH_SHORT).show();
            btMacAddress = btData.substring(btData.length() - 17);
            Log.d(TAG, "btMacAddress = " + btMacAddress);
            remoteDevice = btAapter.getRemoteDevice(btMacAddress);
            myChatService.connect(remoteDevice);
        }


        buttonLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "再次連線藍芽", Toast.LENGTH_SHORT).show();
                if (btMacAddress != null) {
                    Log.d(TAG, "btMacAddress = " + btMacAddress);
                    remoteDevice = btAapter.getRemoteDevice(btMacAddress);
                    myChatService.connect(remoteDevice);

                } else {
                    Toast.makeText(context, "沒有此藍芽", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private final Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_READ:
                    byte[] data = (byte[]) msg.obj;
                    String dataString = new String(data, 0, msg.arg1);
                    editTestData.setText(dataString);
                    getBtRFID = editTestData.getText().toString();
                    getBtRFID = getBtRFID.replace(" ", "");
                    if (getBtRFID.length() > 0) {
                        Log.d("main", "getSetActivity =  " + getSetActivity);
                        if (getSetActivity.equals("set")) {
                            myFireBase = FirebaseDatabase.getInstance().getReference("Topic/RFID/");
                            myFireBase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        setnumber = setnumber + 1;
                                    }
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        String EP = ds.getKey().toString();
                                        EP = EP.replace("_", ".");
                                        Log.d(TAG, "ds.getKey = " + EP);
                                        Log.d(TAG, " setnumber " + setnumber);
                                        n = n + 1;
                                        if (getBtRFID.equals(EP)) {
                                            n = n - 1;
                                            Log.d(TAG, "mainnumber =  " + n);
                                            new AlertDialog.Builder(context)
                                                    .setTitle("RFID已存在")
                                                    .setPositiveButton("重試", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            editTestData.setText("");

                                                        }
                                                    })
                                                    .setNegativeButton("取消領出", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            intent = new Intent(context, MainActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    })
                                                    .show();
                                        } else if (n == setnumber) {
                                            intent = new Intent(context, set_InformationActivity.class);
                                            intent.putExtra("RFID", editTestData.getText().toString());
                                            startActivity(intent);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        if (getSetActivity.equals("get")) {
                            if (getBtRFID.equals(getOutCheckRFID)) {
                                Log.d(TAG, "getSetActivity = " + getSetActivity);
                                Log.d(TAG, "getOutCheckRFID = " + getOutCheckRFID);
                                Log.d(TAG, "getOutChangeNumber = " + getOutChangeNumber);
                                getOutCheckRFID = getOutCheckRFID.replace(".", "_");
                                myFireBase = FirebaseDatabase.getInstance().getReference("Topic/RFID/" + getOutCheckRFID);
                                Map<String, Object> changeData = new HashMap<>();
                                changeData.put("number", getOutChangeNumber);
                                myFireBase.updateChildren(changeData);
                                intent = new Intent(context, MainActivity.class);
                                startActivity(intent);
                            } else {
                                new AlertDialog.Builder(context)
                                        .setTitle("ERRO")
                                        .setMessage("RFID不正確")
                                        .setPositiveButton("重試", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                editTestData.setText("");

                                            }
                                        })
                                        .setNegativeButton("取消領出", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                intent = new Intent(context, MainActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .show();
                            }
                        }
                    }
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    String btName = msg.getData().getString(Constants.DEVICE_NAME);
                    Toast.makeText(context, " 已連線 " + btName, Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_TOAST:
                    String errMsg = msg.getData().getString(Constants.TOAST);
                    Log.d(TAG, "errMsg = " + errMsg);
                    Toast.makeText(context, "設備連接丟失", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };

    private void sendCMD(String message) {
        int btState = myChatService.getState();
        if (btState == BTChatService.STATE_CONNECTED) {
            if (message.length() > 0) {
                byte[] data = message.getBytes();
                myChatService.BTWrite(data);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myChatService != null) {
            myChatService.stop();
            myChatService = null;
        }
    }
}