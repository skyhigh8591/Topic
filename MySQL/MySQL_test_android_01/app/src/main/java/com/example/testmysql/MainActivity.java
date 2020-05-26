package com.example.testmysql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    private TextView textViewShow;
    private Button buttonShow, buttonNew,buttonCheck, buttonUpdate;
    String result;
    private EditText EditTextUsername, EditTextNum;
    private String webAddress = "http://192.168.1.110:8080/test/";
    private String getDataURL = "GetData2.php";
    private String username = "username=";
    private String num = "num=";
    private String newDataURL = "newData.php?";
    private String updateURL = "updateData.php?";
    private String userNameData, numData;
    private StringBuilder myURL;
    private int actionFlag;
    private final int newDataFlag = 1;
    private final int showAllDataFlag = 2;
    private final int checkFlag = 3;
    private final int updateFlag = 4;
    private boolean turn;
    private Context context;
    private int CheckFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        actionFlag = newDataFlag;
        turn = true;

        textViewShow = (TextView)findViewById (R.id.textView_show);
        buttonShow = (Button) findViewById(R.id.button_show);
        buttonNew = (Button) findViewById(R.id.button_new);
        buttonCheck = (Button) findViewById(R.id.button_check);
        buttonUpdate = (Button) findViewById(R.id.button_update);

        EditTextUsername = (EditText) findViewById(R.id.editText_username);
        EditTextNum = (EditText) findViewById(R.id.editText_num);

        buttonShow.setOnClickListener(new MyButton());
        buttonNew.setOnClickListener(new MyButton());
        buttonCheck.setOnClickListener(new MyButton());
        buttonUpdate.setOnClickListener(new MyButton());

    }

    private class MyButton implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            switch (v.getId()){

                case R.id.button_show:
                    if(turn == true){
                        turn = false;
                        actionFlag = showAllDataFlag;

                        SetSQLData myGet1 = new SetSQLData();
                        myGet1.execute();

                    } else {
                        turn = true;
                        textViewShow.setText("");
                    }

                    break;

                case R.id.button_new:

                    userNameData = EditTextUsername.getText().toString().trim();
                    Log.d("main","name=" + userNameData);
                    numData = EditTextNum.getText().toString().trim();
                    actionFlag = newDataFlag;
                    SetSQLData myGet = new SetSQLData();
                    myGet.execute();

                    break;

                case R.id.button_check:

                    userNameData = EditTextUsername.getText().toString();
                    actionFlag = checkFlag;

                    SetSQLData myGet2 = new SetSQLData();
                    myGet2.execute();


                    break;

                case R.id.button_update:

                    userNameData = EditTextUsername.getText().toString();
                    numData = EditTextNum.getText().toString();
                    actionFlag = updateFlag;

                    SetSQLData myGet3 = new SetSQLData();
                    myGet3.execute();


                    break;

            }

        }
    }



    private class SetSQLData extends AsyncTask <Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            String data = null;
            myURL = new StringBuilder();
            myURL.append(webAddress);

            switch (actionFlag){

                case showAllDataFlag:
                    myURL.append(getDataURL);

                    break;

                case newDataFlag:
                    myURL.append(newDataURL);
                    String param = username + userNameData  + "&" + num + numData;
                    myURL.append(param);
                    Log.d("main","myURL=" + myURL);
                    break;

                case checkFlag:

                    myURL.append(getDataURL);

                    break;

                case updateFlag:

                    myURL.append(updateURL);
                    String param2 = username + userNameData  + "&" + num + numData;
                    myURL.append(param2);
                    Log.d("main","myURL=" + myURL);
                    break;

            }

            try {
                URL url = new URL(myURL.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                int code = conn.getResponseCode();

                if(code == HttpURLConnection.HTTP_OK){
                    InputStream inputStream = conn.getInputStream();
                    InputStreamReader reader = new InputStreamReader(inputStream);


                    char[] buffer = new char[4096];
                    int number = reader.read(buffer);
                    data = String.valueOf(buffer);
                    inputStream.close();
                }

            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }


            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (actionFlag == showAllDataFlag) {

                try {
                    JSONArray jsonArray = new JSONArray(s); //JSONArray ->try catch
                    StringBuffer jsonData = new StringBuffer();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObj = jsonArray.getJSONObject(i);

                        String id = jsonObj.getString("ID");
                        jsonData.append("ID = " + id + ",");

                        String userValue = jsonObj.getString("username");
                        jsonData.append("username = " + userValue + ",");

                        String numValue = jsonObj.getString("num");
                        jsonData.append("num= " + numValue + ",");

                        String time = jsonObj.getString("datetime");
                        jsonData.append("created time = " + time + "\n");

                        jsonData.append("-----------------------------\n");

                    }

                    textViewShow.setText(jsonData.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }else if(actionFlag == checkFlag){

                try {

                    CheckFlag =0;

                    JSONArray jsonArray = new JSONArray(s); //JSONArray ->try catch
                    StringBuffer jsonData = new StringBuffer();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObj = jsonArray.getJSONObject(i);


                        String userValue = jsonObj.getString("username");
                     //   jsonData.append(i +" = " + userValue + ",\n");

                        textViewShow.append("userValue = " + userValue+ "   ");

                        int Flag= userValue.compareTo(userNameData);
                        if(Flag == 0){

                            CheckFlag =1;
                        }

                    }

                    if(CheckFlag == 1){
                        textViewShow.setText("重複!!");
                    }else {
                        textViewShow.setText("沒有重複!!");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                textViewShow.setText(s);
            }
        }
    }


}
