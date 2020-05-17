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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class set_InformationActivity extends AppCompatActivity {

    private EditText editTextRFID, editTextName, editTextSpecification, editTextNumber, editTextField, editTextRemarks;
    private ListView listInputText;
    private ArrayList<String> items;
    private ArrayAdapter adapter;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set__information);
        setTitle("set_Information");

        context = this;
        editTextRFID = (EditText) findViewById(R.id.editText_RFID);
        editTextName = (EditText) findViewById(R.id.editText_name);
        editTextSpecification = (EditText) findViewById(R.id.editText_specification);
        editTextNumber = (EditText) findViewById(R.id.editText_number);
        editTextField = (EditText) findViewById(R.id.editText_field);
        editTextRemarks = (EditText) findViewById(R.id.editText_remarks);



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
