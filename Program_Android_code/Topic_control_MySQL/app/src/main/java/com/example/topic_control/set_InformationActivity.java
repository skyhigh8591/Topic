package com.example.topic_control;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

public class set_InformationActivity extends AppCompatActivity {

    private EditText editTextRFID, editTextName, editTextSpecification, editTextNumber, editTextField, editTextRemarks;
    private ListView listInputText;
    private ArrayList<String> items;
    private ArrayAdapter adapter;
    private Context context;
    private Button buttonSetClean, buttonSetSave;
    private String RFID, name, specification, num, field, remarks;
    private TextView textViewInput;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set__information);
        setTitle("set_Information");

        context = this;
        editTextRFID = (EditText) findViewById(R.id.editText_set_RFID);
        editTextName = (EditText) findViewById(R.id.editText_set_name);
        editTextSpecification = (EditText) findViewById(R.id.editText_set_specification);
        editTextNumber = (EditText) findViewById(R.id.editText_set_number);
        editTextField = (EditText) findViewById(R.id.editText_set_field);
        editTextRemarks = (EditText) findViewById(R.id.editText_set_remarks);
        textViewInput = (TextView) findViewById(R.id.textView_set_comment);


        buttonSetClean = (Button) findViewById(R.id.button_set_end);
        buttonSetClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextName.setText("");
                editTextSpecification.setText("");
                editTextNumber.setText("");
                editTextField.setText("");
                editTextRemarks.setText("");
                textViewInput.setText("");
            }
        });

        buttonSetSave = (Button) findViewById(R.id.button_set_save);


        buttonSetSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("上傳");
                builder.setIcon(android.R.drawable.ic_input_add);

                builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RFID = editTextRFID.getText().toString();
                        name = editTextName.getText().toString();
                        specification = editTextSpecification.getText().toString();
                        num = editTextNumber.getText().toString();
                        field = editTextField.getText().toString();
                        remarks = editTextRemarks.getText().toString();


                        Map<String, String> data = new HashMap<>();
                        data.put("name", name);
                        data.put("specification", specification);
                        data.put("number", num);
                        data.put("field", field);
                        data.put("remarks", remarks);

                        SetSQLData myGet = new SetSQLData();
                        myGet.execute();


                        finish();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                builder.create().show();
            }
        });


        editTextRemarks.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!editTextRemarks.getText().toString().equals("")) {
                    textViewInput.append((editTextRemarks.getText().toString()) + "\n");
                    editTextRemarks.setText("");
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "返回選擇頁面");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST:
                new AlertDialog.Builder(context)
                        .setTitle("離開此頁面")
                        .setMessage("你確定要離開？")
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class SetSQLData extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String data = null;
            myURL = new StringBuilder();
            myURL.append(webAddress);
            myURL.append(newDataURL);
            String param = RFID_e + RFID + "&" + name_e + name + "&" + specification_e + specification + "&" + num_e + num + "&" + field_e + field + "&" + remarks_e + remarks;
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
                        jsonData.append("RFID = " + rfid + ",");

                        String nameValue = jsonObj.getString("name");
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

                        jsonData.append("-----------------------------\n");

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
