package com.example.topic_control;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class get_InformationActivity extends AppCompatActivity {

    //SQL參數--------------------
    private String webAddress = "http://192.168.58.116:8080/topic_code/test/";
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
    //---------------------------
    private Button buttonGet;
    private TextView editTextGet_RFID, editTextGet_name;

    private String get_RFID, get_name; //取得的輸入
    private int CheckFlag; //判斷是否重複
    private TextView textViewTest;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get__information);
        setTitle("get_Information");

        context = this;

        editTextGet_RFID = (TextView) findViewById(R.id.editText_get_RDIF );
        editTextGet_name = (TextView) findViewById(R.id.editText_get_name);

        textViewTest = (TextView) findViewById(R.id.textView_test);

        buttonGet = (Button) findViewById(R.id.button_get_Inquire);
        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_RFID = editTextGet_RFID.getText().toString();
                get_name = editTextGet_name.getText().toString();

                if(editTextGet_RFID.getText().toString().matches("")){

                    get_RFID ="fdjgodfngpojepfw4634832";
                }
                if( editTextGet_name.getText().toString().matches("")){
                    get_name = "kgpo3490543jotighd8f9g0j";
                }

                Log.d("main","get_RFID =" + get_RFID);
                Log.d("main","get_name =" + get_name);

                SetSQLData myGet2 = new SetSQLData();
                myGet2.execute();



            }
        });


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.get_menu, menu);
        return true;
    }

    private class SetSQLData extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String data = null;
            myURL = new StringBuilder();
            myURL.append(webAddress);

            myURL.append(getDataURL);

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


                try {

                    CheckFlag =0;

                    JSONArray jsonArray = new JSONArray(s); //JSONArray ->try catch
                    StringBuffer jsonData = new StringBuffer();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObj = jsonArray.getJSONObject(i);


                        String rfid = jsonObj.getString("RFID");
                        String nameValue = jsonObj.getString("name");


                        int rfid_Flag = rfid.compareTo(get_RFID);
                        int name_Flag = nameValue.compareTo(get_name);

                        ArrayList<String> arrayList = new ArrayList<>();
                        arrayList.add(jsonObj.getString("RFID"));
                        arrayList.add(jsonObj.getString("name"));
                        arrayList.add(jsonObj.getString("specification"));
                        arrayList.add(jsonObj.getString("num"));
                        arrayList.add(jsonObj.getString("field"));
                        arrayList.add(jsonObj.getString("remarks"));

                        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
                        for(int i2=0; i2< arrayList.size(); i2++){

                        }


                        if(rfid_Flag == 0){
                            CheckFlag =1;

                            rfid = jsonObj.getString("RFID");
                            jsonData.append("RFID = " + rfid + ",");

                            nameValue = jsonObj.getString("name");
                            jsonData.append("name = " + nameValue + ",");

                            String specificationValue = jsonObj.getString("specification");
                            jsonData.append("specification= " + specificationValue + ",");

                            String numValue = jsonObj.getString("num");
                            jsonData.append("num= " + numValue + ",");

                            String fieldValue = jsonObj.getString("field");
                            jsonData.append("field= " + fieldValue + ",");

                            String remarksValue = jsonObj.getString("remarks");
                            jsonData.append("remarks= " + remarksValue + ",");

                            String time = jsonObj.getString("datetime");
                            jsonData.append("created time = " + time + "\n");
                        }

                        if(name_Flag == 0){
                            CheckFlag =1;

                            rfid = jsonObj.getString("RFID");
                            jsonData.append("RFID = " + rfid + ",");

                            nameValue = jsonObj.getString("name");
                            jsonData.append("name = " + nameValue + ",");

                            String specificationValue = jsonObj.getString("specification");
                            jsonData.append("specification= " + specificationValue + ",");

                            String numValue = jsonObj.getString("num");
                            jsonData.append("num= " + numValue + ",");

                            String fieldValue = jsonObj.getString("field");
                            jsonData.append("field= " + fieldValue + ",");

                            String remarksValue = jsonObj.getString("remarks");
                            jsonData.append("remarks= " + remarksValue + ",");

                            String time = jsonObj.getString("datetime");
                            jsonData.append("created time = " + time + "\n");
                        }


                    }

                    if(CheckFlag == 1){
                        textViewTest.setText(jsonData);
                    }else {
                        textViewTest.setText("沒有搜尋到!!");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

        }
    }


}
