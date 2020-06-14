package com.example.topic_control;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.GLES10;
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
    private DatabaseReference myFireBase;
    private String RFID, name, specification, number, field, remarks;
    private TextView textViewInput;
    private Button buttonSetChangeRemarksClear;
    private Intent intent;
    private String getBtRFID;
    private Intent intentMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set__information);
        setTitle("SetInformation");

        Intent intent = getIntent();
        getBtRFID = intent.getStringExtra("RFID");

        context = this;
        editTextRFID = (EditText) findViewById(R.id.editText_set_RFID);
        editTextName = (EditText) findViewById(R.id.editText_set_name);
        editTextSpecification = (EditText) findViewById(R.id.editText_set_specification);
        editTextNumber = (EditText) findViewById(R.id.editText_set_number);
        editTextField = (EditText) findViewById(R.id.editText_set_field);
        editTextRemarks = (EditText) findViewById(R.id.editText_set_remarks);
        textViewInput = (TextView) findViewById(R.id.textView_set_comment);
        editTextRFID.setText(getBtRFID);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        buttonSetClean = (Button) findViewById(R.id.button_set_end);
        buttonSetClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("清除全部資料")
                        .setMessage("是否要清除")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editTextName.setText("");
                                editTextSpecification.setText("");
                                editTextNumber.setText("");
                                editTextField.setText("");
                                editTextRemarks.setText("");
                                textViewInput.setText("");
                                editTextRFID.setText(getBtRFID);
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

        buttonSetChangeRemarksClear=(Button)findViewById(R.id.button_set_change_remarks_clear);
        buttonSetChangeRemarksClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("清除備註")
                        .setMessage("是否要清除")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                textViewInput.setText("");
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

        myFireBase = FirebaseDatabase.getInstance().getReference("Topic");
        buttonSetSave = (Button) findViewById(R.id.button_set_save);
        buttonSetSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("上傳");
                builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        RFID = editTextRFID.getText().toString();

                        Log.d("main", "getBtRFID =" + getBtRFID);
                        Log.d("main", "RFID =" + RFID);
                        if(getBtRFID.equals(RFID)) {
                            name = editTextName.getText().toString();
                            specification = editTextSpecification.getText().toString();
                            number = editTextNumber.getText().toString();
                            field = editTextField.getText().toString();
                            remarks = textViewInput.getText().toString();

                            Map<String, String> data = new HashMap<>();
                           String rfidPut = RFID.replace(".", "_");

                            Log.d("main", "rfidPut =  " + rfidPut);
                            Log.d("main", "name =  " + name);
                            Log.d("main", "specification =  " + specification);
                            Log.d("main", "number =  " + number);
                            Log.d("main", "field =  " + field);
                            Log.d("main", "remarks =  " + remarks);

                            data.put("name", name);
                            data.put("specification", specification);
                            data.put("number", number);
                            data.put("field", field);
                            data.put("remarks", remarks);

                            myFireBase.child("RFID").child(rfidPut).setValue(data);
                            intentMain = new Intent(context, MainActivity.class);
                            startActivity(intentMain);
                            dialog.dismiss();
                        }else {
                            new AlertDialog.Builder(context)
                                    .setTitle("RFID 不相同")
                                    .setPositiveButton("修正", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            editTextRFID.setText(getBtRFID);
                                        }
                                    })
                                    .show();
                        }
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
        menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "返回主選單");
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
                                intent = new Intent(context, MainActivity.class);
                                startActivity(intent);
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


}
