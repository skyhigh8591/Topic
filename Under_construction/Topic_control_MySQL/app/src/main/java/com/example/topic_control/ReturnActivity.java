package com.example.topic_control;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class ReturnActivity extends AppCompatActivity {

    //SQL參數--------------------
    private String webAddress;
    private String getDataURL = "GetData.php";
    private String newDataURL = "newData.php?";
    private String update_numURL = "updateData_num.php?";
    private String update_allURL = "updateData_all.php?";
    private StringBuilder myURL;

    private String RFID_e = "RFID=";
    private String name_e = "name=";
    private String specification_e = "specification=";
    private String num_e = "num=";
    private String field_e = "field=";
    private String remarks_e = "remarks=";
    private Context context;
    private TextView textViewGetOutComment;
    private Intent intent;

    private int chooseFlag;
    private String ExtraRFID, ExtranameValue, ExtraspecificationValue, ExtrafieldValue, ExtranumValue, ExtraremarksValue, Extratime;
    private int getOutFlag;
    private int getOutValue;
    private int editText_number_check, getOutNumber_check;
    //-----------------------------------------------------

    private static final String TAG = "RFID";
    private String btData;
    private BluetoothAdapter btAapter;
    private BTChatService myChatService;
    private String btMacAddress;
    private BluetoothDevice remoteDevice;
    private Button buttonLink;
    private TextView textViewBT;
    private EditText editTestData;
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
                        if (getSetActivity.equals("set")) {


                        }
                        if (getSetActivity.equals("get")) {
                            if (getBtRFID.equals(getOutCheckRFID)) {

                                chooseFlag= 2;
                                getOutFlag = 1;
                                getOutValue = getOutNumber_check - editText_number_check;
                                Log.d("main", "getOutValue = " + getOutValue);

                                SetSQLData myOut = new SetSQLData();
                                myOut.execute();
                                getOutFlag = 0;

                                intent = new Intent(context,get_InformationActivity.class);
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

    private class SetSQLData extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String data = null;
            myURL = new StringBuilder();
            myURL.append(webAddress);

            switch(chooseFlag){
                case 1:

                    myURL.append(getDataURL);
                    Log.d("main", "myURL=" + myURL );

                    break;

                case 2:
                    myURL.append(update_numURL);
                    String parm = RFID_e + "\""+ ExtraRFID + "\"" + "&" + num_e + getOutValue;
                    myURL.append(parm);

                    break;

            }
            Log.d("main", "myURL=" + myURL);

            try {
                URL url = new URL(myURL.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                int code = conn.getResponseCode();

                if (code == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = conn.getInputStream();
                    InputStreamReader reader = new InputStreamReader(inputStream);


                    char[] buffer = new char[4096];
                    int number = reader.read(buffer);
                    data = String.valueOf(buffer);
                    inputStream.close();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (getOutFlag == 1) {

                try {
                    JSONArray jsonArray1 = new JSONArray(s); //JSONArray ->try catch
                    StringBuffer jsonData = new StringBuffer();



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {

                try {
                    JSONArray jsonArray = new JSONArray(s); //JSONArray ->try catch
                    StringBuffer jsonData = new StringBuffer();


                    for (int i = 0; i < jsonArray.length(); i++) {

                        int Flag = 1;
                        jsonData.setLength(0);

                        JSONObject jsonObj = jsonArray.getJSONObject(i);

                        String rfid = jsonObj.getString("RFID");
                        Flag = ExtraRFID.compareTo(rfid);

                        if (Flag == 0) {
                            jsonData.append("RFID : " + rfid + "\n");
                            ExtraRFID = rfid;

                            String nameValue = jsonObj.getString("name");
                            jsonData.append("產品名稱 : " + nameValue + "\n");
                            ExtranameValue = nameValue;

                            String specificationValue = jsonObj.getString("specification");
                            jsonData.append("產品規格 : " + specificationValue + "\n");
                            ExtraspecificationValue = specificationValue;

                            String numValue = jsonObj.getString("num");
                            jsonData.append("產品數量 : " + numValue + "\n");
                            getOutNumber_check = Integer.parseInt(numValue);
                            Log.d("main", "getOutNumber_check = " + getOutNumber_check);
                            ExtranumValue = numValue;

                            String fieldValue = jsonObj.getString("field");
                            jsonData.append("儲存欄位 : " + fieldValue + "\n");
                            ExtrafieldValue = fieldValue;

                            String remarksValue = jsonObj.getString("remarks");
                            String remarksPut = remarksValue.replace("_","\n");
                            jsonData.append("備註事項 : " + remarksPut + "\n");
                            ExtraremarksValue = remarksValue;

                            String time = jsonObj.getString("datetime");
                            //jsonData.append("創建時間 : " + time + "\n");
                            Extratime = time;

                        }

                        textViewGetOutComment.append(jsonData);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}