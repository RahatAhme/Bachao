package com.bachao.dcc_project.complainBox;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.bachao.dcc_project.GPSTracker;
import com.bachao.dcc_project.R;
import com.bachao.dcc_project.model.modelForComplain;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class complainBox extends AppCompatActivity {

    EditText complaiunINput ;
    Button postBtn ;


DatabaseReference mRef  ;
FirebaseAuth mauth ;
String uid  , DATE;
ImageButton imageButton ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_box);
        getSupportActionBar().hide();

        mauth = FirebaseAuth.getInstance();
        uid = mauth.getUid() ;

        mRef = FirebaseDatabase.getInstance().getReference("complainList").child(uid);


        complaiunINput = findViewById(R.id.editText);
        postBtn = findViewById(R.id.postBtn);
        imageButton = findViewById(R.id.histotryBtn);


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext() , complainHistory.class);
                startActivity(i);

            }
        });


        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String complian  = complaiunINput.getText().toString();
                double lonG, lat ;


                GPSTracker gpsTracker = new GPSTracker(getApplicationContext());

                lat = gpsTracker.getLatitude();
                lonG= gpsTracker.getLongitude();

                if (!TextUtils.isEmpty(complian)){

                    uploadTheDataToFirebae(complian , lat , lonG );

                }





            }
        });
    }

    private void uploadTheDataToFirebae(String complian, double lat, double lonG) {
        String delegate = "hh:mm aaa";
        String  Time = String.valueOf(DateFormat.format(delegate, Calendar.getInstance().getTime()));


        DATE = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        DATE = DATE + " "+Time;


        String postID ;
        postID  =mRef.push().getKey() ;

        modelForComplain modelForComplain  = new modelForComplain(DATE ,postID, complian , lat+"", lonG + "");




        mRef.child(postID).setValue(modelForComplain).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext() , "Complain Uploaded To The Servers " , Toast.LENGTH_LONG).show();
                complaiunINput.setText("");

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getApplicationContext() , "Error : "+e.getMessage() , Toast.LENGTH_SHORT).show();

                    }
                }) ;



    }
}
