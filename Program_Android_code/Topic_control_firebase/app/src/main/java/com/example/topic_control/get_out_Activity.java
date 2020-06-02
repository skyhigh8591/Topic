package com.example.topic_control;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class get_out_Activity extends AppCompatActivity {


    private DatabaseReference myFireBase;
    private HashMap<String, String> mapData;
    private TextView textViewGetOutComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_out_);

        textViewGetOutComment = (TextView)findViewById(R.id.textView_get_out_comment);

        Intent intent = getIntent();
        final String RFID = intent.getStringExtra("RFID");
        textViewGetOutComment.append("RFID = "+RFID+"\n");

        Log.d("main","RFID =" + RFID);
        final String rfidGet = RFID.replace(".", "_");

        myFireBase = FirebaseDatabase.getInstance().getReference("Topic/RFID/"+rfidGet);
        myFireBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d("main", "ds =  " + ds);
                    String RFID = rfidGet.replace("_", ".");

                    textViewGetOutComment.append(ds.getKey()+ "=" +ds.getValue()+"\n");


//
//
//                    String nameData = (String) ds.child("name").getValue();
//                    Log.d("main", "name =  " + nameData);
//                    if (nameData == null)
//                        mapData.put("name", "no name");
//                    else
//                        mapData.put("name", nameData);
//
//                    String specificationData = (String) ds.child("specification").getValue();
//                    Log.d("main", "specification =  " + nameData);
//                    if (specificationData == null)
//                        mapData.put("specification", "no specification");
//                    else
//                        mapData.put("specification", specificationData);
//
//                    String numberData = (String) ds.child("number").getValue();
//                    Log.d("main", "number =  " + numberData);
//                    if (numberData == null)
//                        mapData.put("number", "no number");
//                    else
//                        mapData.put("number", numberData);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}
