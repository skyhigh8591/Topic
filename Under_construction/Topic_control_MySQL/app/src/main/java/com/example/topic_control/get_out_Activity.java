package com.example.topic_control;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLData;


public class get_out_Activity extends AppCompatActivity {

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

    //---------------------------
    private String rfid;
    private String nameValue;
    private String specificationValue;
    private String numValue;
    private String fieldValue;
    private String remarksValue;
    private String timeValue;
    //---------------------------
    private String ExtraRFID, ExtranameValue, ExtraspecificationValue, ExtrafieldValue, ExtranumValue, ExtraremarksValue, Extratime;
    private Button buttonGetOutNo, buttonGetOutYes;
    private EditText editTextGetOutNumber;
    private String getOutName, getOutSpecification, getOutNumber, getOutField, getOutRemarks;
    private int editText_number_check, getOutNumber_check;
    private String message;
    private int getOutFlag;
    private int getOutValue;
    private int chooseFlag;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_out_);

        setTitle("GetOut");

        chooseFlag = 1;

        GlobalVariable G = new GlobalVariable();
        webAddress = G.getWeb();

        context = this;
        textViewGetOutComment = (TextView) findViewById(R.id.textView_get_out_comment);

        intent = getIntent();
        ExtraRFID = intent.getStringExtra("RFID");

        final SetSQLData myGet = new SetSQLData();
        myGet.execute();

        editTextGetOutNumber = (EditText) findViewById(R.id.editText_get_out_number);

        buttonGetOutNo = (Button) findViewById(R.id.button_get_out_no);
        buttonGetOutYes = (Button) findViewById(R.id.button_get_out_yes);

        buttonGetOutNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("返回清單")
                        .setMessage("你確定要返回？")
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
            }
        });

        buttonGetOutYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextGetOutNumber.length() == 0) {
                    new AlertDialog.Builder(context)
                            .setTitle("ERRO")
                            .setMessage("領出數量不得空白")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                } else {
                    String test2 = editTextGetOutNumber.getText().toString();
                    editText_number_check = Integer.parseInt(test2);
                    Log.d("main", "editText_number_check = " + editText_number_check);
                    if (getOutNumber_check >= editText_number_check) {

                        ///////////////////////////////////////////////////////////////////////

                        new AlertDialog.Builder(context)
                                .setTitle("ERRO")
                                .setMessage("確定是否出庫")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        chooseFlag= 2;
                                        getOutFlag = 1;
                                        getOutValue = getOutNumber_check - editText_number_check;
                                        Log.d("main", "getOutValue = " + getOutValue);

                                        SetSQLData myOut = new SetSQLData();
                                        myOut.execute();
                                        getOutFlag = 0;

                                        intent = new Intent(context,get_InformationActivity.class);
                                        startActivity(intent);

                                    }
                                })
                                .show();


                        //////////////////////////////////////////////////////////////////////


                    } else {
                        new AlertDialog.Builder(context)
                                .setTitle("ERRO")
                                .setMessage("數量不足")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                    }
                }

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.get_out_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.get_out_change:
                message = "是否要修改資料";
                showDialog_2();

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog_2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("選擇動作");
        builder.setMessage(message);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intent = new Intent(context, get_change_Activity.class);

                intent.putExtra("RFID", ExtraRFID);
                intent.putExtra("name", ExtranameValue);
                intent.putExtra("specification", ExtraspecificationValue);
                intent.putExtra("field", ExtrafieldValue);
                intent.putExtra("num", ExtranumValue);
                intent.putExtra("remarks", ExtraremarksValue);

                startActivity(intent);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

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
