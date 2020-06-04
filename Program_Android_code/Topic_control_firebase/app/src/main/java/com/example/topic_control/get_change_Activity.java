package com.example.topic_control;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class get_change_Activity extends AppCompatActivity {

    private Context context;
    private String RFID;
    private EditText editTextGetChangeRFID, editTextGetChangeName, editTextGetChangeSpecification, editTextGetChangeNumber, editTextGetChangeField;
    private DatabaseReference myFireBase;
    private String getChangeName, getChangeSpecification, getChangeNumber, getChangeField, getChangeComment;
    private TextView textViewGetChangeComment;
    private Button buttonRemarksClear;
    private Button buttonGetChangeEnd;
    private EditText editTextGetChangeRemarks;
    private Button buttonGetChangeSave;
    private String specification, name, number, field, remarks;
    private String s1,s2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_change_);

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        context = this;
        Intent intent = getIntent();
        RFID = intent.getStringExtra("RFID");
        Log.d("main", "RFID =" + RFID);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        editTextGetChangeRFID = (EditText) findViewById(R.id.editText_get_change_RFID);
        editTextGetChangeName = (EditText) findViewById(R.id.editText_get_change_name);
        editTextGetChangeSpecification = (EditText) findViewById(R.id.editText_get_change_specification);
        editTextGetChangeNumber = (EditText) findViewById(R.id.editText_get_change_number);
        editTextGetChangeField = (EditText) findViewById(R.id.editText_get_change_field);
        textViewGetChangeComment = (TextView) findViewById(R.id.textView_get_change_comment);

        myFireBase = FirebaseDatabase.getInstance().getReference("Topic/RFID/" + RFID);
        myFireBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getChangeName = (String) dataSnapshot.child("name").getValue();
                editTextGetChangeName.setText(getChangeName);
                getChangeSpecification = (String) dataSnapshot.child("specification").getValue();
                editTextGetChangeSpecification.setText(getChangeSpecification);
                getChangeNumber = (String) dataSnapshot.child("number").getValue();
                editTextGetChangeNumber.setText(getChangeNumber);
                getChangeField = (String) dataSnapshot.child("field").getValue();
                editTextGetChangeField.setText(getChangeField);
                getChangeComment = (String) dataSnapshot.child("remarks").getValue();
                textViewGetChangeComment.setText(getChangeComment);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        String test1 = RFID.replace("_", ".");
        editTextGetChangeRFID.setText(test1);
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        buttonRemarksClear = (Button) findViewById(R.id.button_remarks_clear);
        buttonRemarksClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("清除備註")
                        .setMessage("是否要清除")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                textViewGetChangeComment.setText("");
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
        //////////////////////////////////////////////////////////////////////////////////////////////////////////

        editTextGetChangeRemarks = (EditText) findViewById(R.id.editText_get_change_remarks);

        editTextGetChangeRemarks.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!editTextGetChangeRemarks.getText().toString().equals("")) {
                    textViewGetChangeComment.append((editTextGetChangeRemarks.getText().toString()) + "\n");
                    editTextGetChangeRemarks.setText("");
                }
                return false;
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        buttonGetChangeSave = (Button) findViewById(R.id.button_get_change_save);
        buttonGetChangeSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("修改");


                builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        RFID = RFID.replace("_", ".");
                        Log.d("main", "RFID =" + RFID);
                        Log.d("main", "editTextGetChangeRFID =" + editTextGetChangeRFID.getText().toString());
                         s1 = RFID.toString();
                         s2 = editTextGetChangeRFID.getText().toString();

                        if (s1.equals(s2)) {

                            String test2 = RFID.replace(".", "_");
                            editTextGetChangeRFID.setText(test2);
                            RFID = editTextGetChangeRFID.getText().toString();
                            name = editTextGetChangeName.getText().toString();
                            specification = editTextGetChangeSpecification.getText().toString();
                            number = editTextGetChangeNumber.getText().toString();
                            field = editTextGetChangeField.getText().toString();
                            remarks = textViewGetChangeComment.getText().toString();


                            Map<String, String> data = new HashMap<>();

                            Log.d("main", "RFID =  " + RFID);
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

                            myFireBase.setValue(data);
                            finish();
                            dialog.dismiss();
                        }else{
                            new AlertDialog.Builder(context)
                                    .setTitle("RFID 不相同")
                                    .setPositiveButton("修正", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            editTextGetChangeRFID.setText(s1);
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


    }
}
