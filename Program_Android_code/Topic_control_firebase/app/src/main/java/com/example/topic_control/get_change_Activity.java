package com.example.topic_control;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class get_change_Activity extends AppCompatActivity {

    private Context context;
    private String RFID;
    private EditText editTextGetChangeRFID,editTextGetChangeName,editTextGetChangeSpecification
            ,editTextGetChangeNumber,editTextGetChangeField;
    private DatabaseReference myFireBase;
    private String getChangeName,getChangeSpecification,getChangeNumber,getChangeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_change_);

        context = this;
        Intent intent = getIntent();
        RFID = intent.getStringExtra("RFID");
        Log.d("main","RFID =" + RFID);


        editTextGetChangeRFID = (EditText)findViewById(R.id.editText_get_change_RFID);
        editTextGetChangeName = (EditText)findViewById(R.id.editText_get_change_name);
        editTextGetChangeSpecification=(EditText)findViewById(R.id.editText_get_change_specification);
        editTextGetChangeNumber = (EditText)findViewById(R.id.editText_get_change_number);
        editTextGetChangeField = (EditText)findViewById(R.id.editText_get_change_field);

        myFireBase = FirebaseDatabase.getInstance().getReference("Topic/RFID/"+RFID);
        myFireBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getChangeName = (String) dataSnapshot.child("name").getValue();
                editTextGetChangeName.setText(getChangeName);
                getChangeSpecification = (String) dataSnapshot.child("specification").getValue();
                editTextGetChangeSpecification.setText(getChangeSpecification);
                getChangeNumber = (String) dataSnapshot.child("number").getValue();
                editTextGetChangeNumber.setText(getChangeNumber);
                getChangeField= (String) dataSnapshot.child("field").getValue();
                editTextGetChangeField.setText(getChangeField);

            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        String test1 = RFID.replace("_", ".");
        editTextGetChangeRFID.setText(test1);


    }
}
