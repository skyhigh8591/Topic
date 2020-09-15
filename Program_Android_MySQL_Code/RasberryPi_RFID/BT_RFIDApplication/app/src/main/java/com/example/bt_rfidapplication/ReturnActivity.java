package com.example.bt_rfidapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


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



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);

        setTitle("RFID Control");

        context = this;
        Intent intent = getIntent();
        btData = intent.getStringExtra("btdata");

        textViewBT = (TextView) findViewById(R.id.textView_RFIDBT);
        textViewBT.setText(btData);

        editTestData=(EditText)findViewById(R.id.editText_RFID);


        buttonLink = (Button)findViewById(R.id.button_RFIDLink);

        btAapter = BluetoothAdapter.getDefaultAdapter();
        myChatService = new BTChatService(context, myHandler);

        if (btData != null){
            Toast.makeText(context,"Linking with BT.",Toast.LENGTH_SHORT).show();
            btMacAddress = btData.substring(btData.length()-17);
            Log.d(TAG, "btMacAddress = " + btMacAddress);
            remoteDevice = btAapter.getRemoteDevice(btMacAddress);
            myChatService.connect(remoteDevice);
        }


        buttonLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Link with BT again", Toast.LENGTH_SHORT).show();
                if (btMacAddress != null){
                    Log.d(TAG, "btMacAddress = " + btMacAddress);
                    remoteDevice = btAapter.getRemoteDevice(btMacAddress);
                    myChatService.connect(remoteDevice);

                } else {
                    Toast.makeText(context,"no MAC address", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private final Handler myHandler = new Handler(){

        @Override
        public void handleMessage(Message msg){

            switch (msg.what){
                case Constants.MESSAGE_READ:
                    byte[] data = (byte[]) msg.obj;
                    String dataString = new String(data, 0, msg.arg1);
                    editTestData.append(dataString);
                    break;

                case Constants.MESSAGE_DEVICE_NAME:
                    String btName = msg.getData().getString(Constants.DEVICE_NAME);
                    Toast.makeText(context,"Link with " + btName, Toast.LENGTH_SHORT).show();
                    break;

                case Constants.MESSAGE_TOAST:
                    String errMsg = msg.getData().getString(Constants.TOAST);
                    Toast.makeText(context, errMsg, Toast.LENGTH_SHORT).show();
                    break;

            }
        }

    };

    private void sendCMD(String message){
        int btState = myChatService.getState();

        if (btState == BTChatService.STATE_CONNECTED){
            if (message.length() >0){
                byte[] data = message.getBytes();
                myChatService.BTWrite(data);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myChatService != null){
            myChatService.stop();
            myChatService=null;
        }
    }
}