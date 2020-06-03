package com.example.topic_control;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class get_out_Activity extends AppCompatActivity {


    private DatabaseReference myFireBase;
    private HashMap<String, String> mapData;
    private TextView textViewGetOutComment;
    private int getOutNumber_check;
    private String message;
    private Intent intent;
    private Context context;
    private Button buttonGetOutNo;
    private Button buttonGetOutYes;
    private EditText editTextGetOutNumber;
    private int editText_number_check;
    private String RFID,rfidGet;
    private String getOutName,getOutSpecification,getOutNumber,getOutField,getOutRemarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_out_);

        context = this;
        textViewGetOutComment = (TextView)findViewById(R.id.textView_get_out_comment);


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Intent intent = getIntent();
          RFID = intent.getStringExtra("RFID");
        textViewGetOutComment.append("RFID = "+RFID+"\n");

        Log.d("main","RFID =" + RFID);
        rfidGet = RFID.replace(".", "_");

        myFireBase = FirebaseDatabase.getInstance().getReference("Topic/RFID/"+rfidGet);
        myFireBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getOutName = (String) dataSnapshot.child("name").getValue();
                textViewGetOutComment.append("產品名稱= "+ getOutName + "\n");
                getOutSpecification = (String) dataSnapshot.child("specification").getValue();
                textViewGetOutComment.append("產品規格= "+getOutSpecification+ "\n");
                getOutNumber = (String) dataSnapshot.child("number").getValue();
                textViewGetOutComment.append("產品數量= "+ getOutNumber + "\n");
                getOutField= (String) dataSnapshot.child("field").getValue();
                textViewGetOutComment.append("儲存欄位= "+getOutField + "\n");
                getOutRemarks= (String) dataSnapshot.child("remarks").getValue();
                textViewGetOutComment.append("備註事項:\n"+getOutRemarks+ "\n");
                String test1 = dataSnapshot.child("number").getValue().toString();
                getOutNumber_check=Integer.parseInt(test1);
                Log.d("main","getOutNumber_check =" + getOutNumber_check);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        buttonGetOutNo = (Button)findViewById(R.id.button_get_out_no);

        buttonGetOutNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("返回清單")
                        .setMessage("你確定要返回？")
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
            }
        });
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        editTextGetOutNumber = (EditText)findViewById(R.id.editText_get_out_number);



        buttonGetOutYes = (Button)findViewById(R.id.button_get_out_yes);

        buttonGetOutYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextGetOutNumber.length()==0) {
                    new AlertDialog.Builder(context)
                            .setTitle("ERRO")
                            .setMessage("領出數量不得空白")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }else{
                    String test2 = editTextGetOutNumber.getText().toString();
                    editText_number_check = Integer.parseInt(test2);
                    if(getOutNumber_check >= editText_number_check){
                        ///////////////////////////////////////////////////////////////////////

                        new AlertDialog.Builder(context)
                                .setTitle("ERRO")
                                .setMessage("RFID功能")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();


                        //////////////////////////////////////////////////////////////////////


                    }else {
                        new AlertDialog.Builder(context)
                                .setTitle("ERRO")
                                .setMessage("數量不足")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                    }
                }

            }
        });

    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.get_out_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.get_out_change:
                message = "是否要修改資料";
                showDialog_2();

                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showDialog_2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("選擇動作");
        builder.setMessage(message);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intent = new Intent(context,get_change_Activity.class);
                intent.putExtra("RFID",rfidGet);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
