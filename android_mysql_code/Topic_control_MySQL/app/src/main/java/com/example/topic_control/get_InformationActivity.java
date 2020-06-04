package com.example.topic_control;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;
import java.util.Map;

public class get_InformationActivity extends AppCompatActivity {

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
    //---------------------------
    private String rfid;
    private String nameValue;
    private String specificationValue;
    private String numValue;
    private String fieldValue;
    private String remarksValue;
    private String timeValue;
    //---------------------------
    private Button buttonGet;
    private TextView editText_get_ALL;

    private String get_RFID, get_name, get_ALL; //取得的輸入
    private int CheckFlag; //判斷是否重複
    private TextView textViewTest;
    private Context context;
    private ListView listViewGetStock;
    private ArrayList<Map<String, String>> arraylist;
    private SimpleAdapter adapter;
    private HashMap<String, String> mapData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get__information);
        setTitle("get_Information");

        context = this;

        GlobalVariable G = new GlobalVariable();
        webAddress = G.getWeb();

        editText_get_ALL = (TextView) findViewById(R.id.editText_get_ALL);

        textViewTest = (TextView) findViewById(R.id.textView_test);

        listViewGetStock = (ListView)findViewById(R.id.listView_get_stock);
        arraylist = new ArrayList<Map<String, String>>();
        adapter = new SimpleAdapter(context, arraylist, R.layout.get_item_layout, new String[]{"RFID", "name", "specification", "num"},
                                    new int[]{R.id.textView_get_RFID, R.id.textView_get_name, R.id.textView_get_specification, R.id.textView_get_item_number} );

        listViewGetStock.setAdapter(adapter);

        buttonGet = (Button) findViewById(R.id.button_get_Inquire);
        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                get_ALL = editText_get_ALL.getText().toString();

                if(editText_get_ALL.getText().toString().matches("")){

                    get_ALL = "";
                }

                Log.d("main","get_RFID =" + get_RFID);
                Log.d("main","get_name =" + get_name);

                arraylist.clear();
                adapter.notifyDataSetChanged();

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

                    JSONArray jsonArray = new JSONArray(s); //JSONArray ->try catch
                    StringBuffer jsonData = new StringBuffer();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        CheckFlag =0;
                        JSONObject jsonObj = jsonArray.getJSONObject(i);


                        rfid = jsonObj.getString("RFID");
                        nameValue = jsonObj.getString("name");
                        specificationValue = jsonObj.getString("specification");
                        numValue = jsonObj.getString("num");
                        fieldValue = jsonObj.getString("field");
                        remarksValue = jsonObj.getString("remarks");

                        int getAll_Flag1 = rfid.compareTo(get_ALL);
                        int getAll_Flag2 = nameValue.compareTo(get_ALL);
                        int getAll_Flag3 = specificationValue.compareTo(get_ALL);
                        int getAll_Flag4 = numValue.compareTo(get_ALL);
                        int getAll_Flag5 = fieldValue.compareTo(get_ALL);
                        int getAll_Flag6 = remarksValue.compareTo(get_ALL);

                        if(getAll_Flag1 ==0)  CheckFlag =1;
                        if(getAll_Flag2 ==0)  CheckFlag =1;
                        if(getAll_Flag3 ==0)  CheckFlag =1;
                        if(getAll_Flag4 ==0)  CheckFlag =1;
                        if(getAll_Flag5 ==0)  CheckFlag =1;
                        if(getAll_Flag6 ==0)  CheckFlag =1;

                        Log.d("main","CheckFlag:" +CheckFlag);
                        Log.d("main","all:" + get_ALL.isEmpty());

                        if(get_ALL.isEmpty()) {
                            mapData = new HashMap<String, String>();
                            mapData.put("RFID", rfid);
                            mapData.put("name", nameValue);
                            mapData.put("specification", specificationValue);
                            mapData.put("num", numValue);

                            arraylist.add(mapData);
                        } else {
                            if(CheckFlag == 1){
                                mapData = new HashMap<String, String>();
                                mapData.put("RFID", rfid);
                                mapData.put("name", nameValue);
                                mapData.put("specification", specificationValue);
                                mapData.put("num", numValue);

                                arraylist.add(mapData);
                                Log.d("main",rfid + "成功加入:" + mapData);

                                Log.d("main","成功加入:" + rfid);

                                Log.d("main","arraylist :" + arraylist);
                            }

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }

}
