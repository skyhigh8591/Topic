package com.example.topic_control;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Map;

public class get_change_Activity extends AppCompatActivity {

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

    private String RFID, name, specification, num, field, remarks;
    //---------------------------
    private String rfid;
    private String nameValue;
    private String specificationValue;
    private String numValue;
    private String fieldValue;
    private String remarksValue;
    private String timeValue;
    //---------------------------
    private EditText editTextGetChangeRFID, editTextGetChangeName, editTextGetChangeSpecification, editTextGetChangeNumber, editTextGetChangeField, editTextGetChangeRemarks;
    private TextView textViewGetChangeComment;
    private Button buttonGetChangeEnd, buttonGetChangeSave, buttonGetChangeRemarksClear;
    private String ExtraRFID,ExtranameValue, ExtraspecificationValue, ExtrafieldValue, ExtranumValue, ExtraremarksValue;
    private String saveRemarks="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_change_);
        setTitle("GetChange");

        GlobalVariable G = new GlobalVariable();
        webAddress = G.getWeb();

        context = this;

        editTextGetChangeRFID = (EditText) findViewById(R.id.editText_get_change_RFID);
        editTextGetChangeName = (EditText) findViewById(R.id.editText_get_change_name);
        editTextGetChangeSpecification = (EditText) findViewById(R.id.editText_get_change_specification);
        editTextGetChangeNumber = (EditText) findViewById(R.id.editText_get_change_number);
        editTextGetChangeField = (EditText) findViewById(R.id.editText_get_change_field);
        editTextGetChangeRemarks = (EditText) findViewById(R.id.editText_get_change_remarks);

        textViewGetChangeComment = (TextView) findViewById(R.id.textView_get_change_comment);

        intent = getIntent();
        ExtraRFID = intent.getStringExtra("RFID");
        ExtranameValue = intent.getStringExtra("name");
        ExtraspecificationValue = intent.getStringExtra("specification");
        ExtrafieldValue = intent.getStringExtra("field");
        ExtranumValue = intent.getStringExtra("num");
        ExtraremarksValue = intent.getStringExtra("remarks");
        String remarksPut = ExtraremarksValue.replace("_","\n");

        editTextGetChangeRFID.setText(ExtraRFID);
        editTextGetChangeName.setText(ExtranameValue);
        editTextGetChangeSpecification.setText(ExtraspecificationValue);
        editTextGetChangeNumber.setText(ExtranumValue);
        editTextGetChangeField.setText(ExtrafieldValue);
        textViewGetChangeComment.setText(remarksPut);

        Log.d("main", "RFID = " + ExtraRFID);



        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        buttonGetChangeEnd = (Button) findViewById(R.id.button_get_change_end);
        buttonGetChangeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("取消修改資料")
                        .setMessage("是否要取消")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

            }
        });

        editTextGetChangeRemarks.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!editTextGetChangeRemarks.getText().toString().equals("")) {
                    textViewGetChangeComment.append((editTextGetChangeRemarks.getText().toString()) + "\n");
                    saveRemarks = saveRemarks + editTextGetChangeRemarks.getText().toString() +"_";
                    editTextGetChangeRemarks.setText("");
                }
                return false;
            }
        });

        buttonGetChangeSave = (Button) findViewById(R.id.button_get_change_save);
        buttonGetChangeSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("修改");
                builder.setMessage("確認修改資料");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RFID = editTextGetChangeRFID.getText().toString();
                        name = editTextGetChangeName.getText().toString();
                        specification = editTextGetChangeSpecification.getText().toString();
                        num = editTextGetChangeNumber.getText().toString();
                        field = editTextGetChangeField.getText().toString();
                        //remarks = textViewGetChangeComment.getText().toString();
                        remarks = saveRemarks;
                        Log.d("main","remaks = " + remarks);
                        int Flag = RFID.compareTo(ExtraRFID);
                        Log.d("main","Flag = " + Flag);
                        Log.d("main","RFID = " + ExtraRFID);
                        if(Flag != 0){
                           AlertDialog.Builder dialog_RFID = new AlertDialog.Builder(context);
                           dialog_RFID.setTitle("錯誤");
                           dialog_RFID.setMessage("RFID錯誤");
                           dialog_RFID.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   editTextGetChangeRFID.setText(ExtraRFID);
                                   dialog.dismiss();
                               }
                           });
                           dialog_RFID.create().show();
                        } else {
                            SetSQLData myChange = new SetSQLData();
                            myChange.execute();

                            //finish();
                            Intent intent = new Intent(context,get_out_Activity.class);
                            intent.putExtra("RFID",RFID);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();

            }

        });

        buttonGetChangeRemarksClear = (Button) findViewById(R.id.button_get_change_remarks_clear);
        buttonGetChangeRemarksClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewGetChangeComment.setText("");
            }
        });


    }

    private class SetSQLData extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String data = null;
            myURL = new StringBuilder();
            myURL.append(webAddress);
            myURL.append(update_allURL);
            String param = RFID_e + "\"" + RFID + "\"" + "&" + name_e + "\"" + name + "\"" + "&" + specification_e +  "\"" + specification +  "\"" + "&" + num_e +  "\"" + num +  "\"" + "&" + field_e +  "\"" + field +  "\"" + "&" + remarks_e +  "\"" + remarks + "\"";
            myURL.append(param);

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

            try {
                JSONArray jsonArray = new JSONArray(s); //JSONArray ->try catch
                StringBuffer jsonData = new StringBuffer();

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObj = jsonArray.getJSONObject(i);

                    String rfid = jsonObj.getString("RFID");

                    String nameValue = jsonObj.getString("name");

                    String specificationValue = jsonObj.getString("specification");

                    String numValue = jsonObj.getString("num");

                    String fieldValue = jsonObj.getString("field");

                    String remarksValue = jsonObj.getString("remarks");

                    String time = jsonObj.getString("datetime");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
