package com.example.topic_control;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    private String getDataURL2 = "GetData2.php";
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
    private Button buttonGet ;
    private TextView editText_get_ALL;

    private String get_RFID, get_name, get_ALL; //取得的輸入
    private int CheckFlag; //判斷是否重複
    private TextView textViewTest;
    private Context context;
    private ListView listViewGetStock;
    private ArrayList<Map<String, String>> arraylist;
    private SimpleAdapter adapter;
    private HashMap<String, String> mapData;
    private Intent intent;
    private String getRfidSend;
    private String activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get__information);
        setTitle("get_Information");

        context = this;

        GlobalVariable G = new GlobalVariable();
        webAddress = G.getWeb();


        editText_get_ALL = (TextView) findViewById(R.id.editText_get_ALL);
        editText_get_ALL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                doSQL();
            }
        });

        listViewGetStock = (ListView)findViewById(R.id.listView_get_stock);
        arraylist = new ArrayList<Map<String, String>>();
        adapter = new SimpleAdapter(context, arraylist, R.layout.get_item_layout, new String[]{"RFID", "name", "specification", "num"},
                                    new int[]{R.id.textView_get_RFID, R.id.textView_get_name, R.id.textView_get_specification, R.id.textView_get_item_number} );

        listViewGetStock.setAdapter(adapter);

        buttonGet = (Button) findViewById(R.id.button_get_Inquire);
        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_get_ALL.setText("");
            }
        });

        doSQL();//初始化


        listViewGetStock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,String> item = (Map<String, String>) parent.getItemAtPosition(position);
                getRfidSend = item.get("RFID");
                Log.d("main","getRfidSend =" +getRfidSend);
                intent = new Intent(context,get_out_Activity.class);
                intent.putExtra("RFID",getRfidSend);
                startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.get_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){



            case R.id.get_newdata:
                AlertDialog.Builder dialog2 = new AlertDialog.Builder(context);
                dialog2.setTitle("選擇動作");
                dialog2.setMessage("是否要新增資料？");
                dialog2.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent = new Intent(context, Bt_App_Main_Activity.class);
                        activity = "set";
                        intent.putExtra("activity", activity);
                        startActivity(intent);
                    }
                });
                dialog2.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog2.show();

                break;

            case R.id.get_break:

                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("離開此頁面");
                dialog.setMessage("你確定要離開？");
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                    }
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                break;

        }

        return super.onOptionsItemSelected(item);

    }

    private void doSQL(){

        get_ALL = editText_get_ALL.getText().toString();

        if(editText_get_ALL.getText().toString().matches("")){

            get_ALL = "";
        }

        arraylist.clear();
        //adapter.notifyDataSetChanged();


        SetSQLData myGet2 = new SetSQLData();
        myGet2.execute();
    }

    private class SetSQLData extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String data = null;
            myURL = new StringBuilder();
            myURL.append(webAddress);

            myURL.append(getDataURL2);
            myURL.append("?search="+get_ALL);

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

                        mapData = new HashMap<String, String>();
                        mapData.put("RFID", rfid);
                        mapData.put("name", nameValue);
                        mapData.put("specification", specificationValue);
                        mapData.put("num", numValue);

                        arraylist.add(mapData);

                    }
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }

}
