package com.example.topic_control;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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


public class get_out_Activity extends AppCompatActivity {

    //SQL參數--------------------
    private String webAddress;
    private String getDataURL = "GetData.php";
    private String newDataURL = "newData.php?";
    private String updateURL = "updateData.php?";
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
    private String ExtraRFID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_out_);

        setTitle("GetOut");

        GlobalVariable G = new GlobalVariable();
        webAddress = G.getWeb();

        context = this;
        textViewGetOutComment = (TextView)findViewById(R.id.textView_get_out_comment);

        intent = getIntent();
        ExtraRFID = intent.getStringExtra("RFID");

        SetSQLData myGet = new SetSQLData();
        myGet.execute();


    }

    private class SetSQLData extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String data = null;
            myURL = new StringBuilder();
            myURL.append(webAddress);
            myURL.append(getDataURL);

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

                    int Flag =1;
                    jsonData.setLength(0);

                    JSONObject jsonObj = jsonArray.getJSONObject(i);

                    String rfid = jsonObj.getString("RFID");
                    Flag = ExtraRFID.compareTo(rfid);

                    if(Flag == 0){
                        jsonData.append("RFID = " + rfid + "\n");

                        String nameValue = jsonObj.getString("name");
                        jsonData.append("name = " + nameValue + "\n" );

                        String specificationValue = jsonObj.getString("specification");
                        jsonData.append("specification= " + specificationValue + "\n");

                        String numValue = jsonObj.getString("num");
                        jsonData.append("num= " + numValue + "\n");

                        String fieldValue = jsonObj.getString("field");
                        jsonData.append("field= " + fieldValue + "\n");

                        String remarksValue = jsonObj.getString("remarks");
                        jsonData.append("remarks= " + remarksValue + "\n");

                        String time = jsonObj.getString("datetime");
                        jsonData.append("created time = " + time + "\n");

                        jsonData.append("-----------------------------\n");
                    }

                    textViewGetOutComment.append(jsonData);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
