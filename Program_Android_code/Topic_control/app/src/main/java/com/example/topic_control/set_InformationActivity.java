package com.example.topic_control;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
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
    private Button buttonSetClean,buttonSetSave;
    private DatabaseReference myFireBase;
    private String RFID,name,specification,number,field,remarks;


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

        myFireBase = FirebaseDatabase.getInstance().getReference("Topic");

        buttonSetClean=(Button)findViewById(R.id.button_set_clean);
        buttonSetClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextName.setText("");
                editTextSpecification.setText("");
                editTextNumber.setText("");
                editTextField.setText("");
                editTextRemarks.setText("");
            }
        });

        buttonSetSave=(Button)findViewById(R.id.button_set_save);


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
                        number = editTextSpecification.getText().toString();
                        field = editTextSpecification.getText().toString();


                        Map<String, String> data = new HashMap<>();
                        data.put("name",name);
                        data.put("specification",specification);
                        data.put("number",number);
                        data.put("field",field);




                        myFireBase.child("RFID").child(RFID).setValue(data);
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


        listInputText = (ListView) findViewById(R.id.listInputText);
        items = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        listInputText.setAdapter(adapter);

        listInputText.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id) {
                final int pos = position;
                new AlertDialog.Builder(context)
                        .setTitle("刪除備註")
                        .setMessage("你確定要刪除？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                items.remove(pos);
                                listInputText.setAdapter(adapter);
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

                return false;
            }

        });

        editTextRemarks.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!editTextRemarks.getText().toString().equals("")) {
                    items.add(editTextRemarks.getText().toString());
                    listInputText.setAdapter(adapter);
                    editTextRemarks.setText("");
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, Menu.FIRST , Menu.NONE, "返回選擇頁面");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST :
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


}
