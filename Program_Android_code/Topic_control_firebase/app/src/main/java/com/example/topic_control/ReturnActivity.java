package com.example.topic_control;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
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

import androidx.appcompat.app.AppCompatActivity;


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

        textViewBT = (TextView) findViewById(R.id.textView_RFIDBT);
        textViewBT.setText(btData);

        editTestData=(EditText)findViewById(R.id.editText_return_RFID);


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
                    editTestData.setText(dataString);
                    getBtRFID = editTestData.getText().toString();
                    getBtRFID = getBtRFID.replace(" ","");
                    if(getBtRFID.length()>0){
                        Log.d("main", "getSetActivity =  " + getSetActivity);
                        if(getSetActivity.equals("set")) {
                            s1 = set_InformationActivity.class;
                            intent = new Intent(context, s1);
                            intent.putExtra("RFID", editTestData.getText().toString());
                            startActivity(intent);
                            break;
                        }
                        if(getSetActivity.equals("get")) {
                            intent = new Intent(context, get_out_Activity.class);
                            intent.putExtra("RFID", editTestData.getText().toString());
                            startActivity(intent);
                            break;
                        }
                    }

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