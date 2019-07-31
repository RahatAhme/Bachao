package com.bachao.dcc_project;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.bachao.dcc_project.model.dataModel;
import com.bachao.dcc_project.splashScreen.SplashScreen;

import java.util.Locale;

public class profile extends AppCompatActivity {


    EditText name , adress ,  emrgencyph , emergencyph2  , ownphn ;
    String  NAME , nid  ,ADRESS , EMERGENCYPH ,EMERGENCYPH2 , OWNPHN ;
    EditText NID ;
Button languagebtn ;

    FirebaseAuth mauth ;
    FirebaseUser muser ;
    DatabaseReference mdatabse;
Button updateBtn ;

    FirebaseAuth getMauth ;
    String uids ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_profile);

        mauth = FirebaseAuth.getInstance();
        uids = mauth.getUid();
        mdatabse = FirebaseDatabase.getInstance().getReference("users").child(uids);

        name = findViewById(R.id.nameInProfile);
        emrgencyph = findViewById(R.id.emergencyNumber1INprofile);
        emergencyph2 = findViewById(R.id.emergencyNumber2INPROFILE);
        ownphn = findViewById(R.id.numberInprofile);
        adress = findViewById(R.id.adressInPRofile);
        updateBtn = findViewById(R.id.updateBTN);
        NID = findViewById(R.id.nidNoPRofile);
        languagebtn = findViewById(R.id.laguagePrestBtn);


        languagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] listitems = {"English", "Bangla"};

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(profile.this);
                mBuilder.setTitle("Choose Language...") ;
                mBuilder.setSingleChoiceItems(listitems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which==0 )
                        {
                            setLocale("en");
                            recreate();


                            Intent i  = new Intent(getApplicationContext() , SplashScreen.class);
                            startActivity(i);

                        }
                        else {

                            setLocale("bn") ;
                            Intent i  = new Intent(getApplicationContext() , SplashScreen.class);
                            startActivity(i);

                        }
                        dialog.dismiss();
                      //  recreate();
                    }
                });


                AlertDialog mDialogue = mBuilder.create();
                mDialogue.show();



            }
        });


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                NAME = name.getText().toString() ;
                EMERGENCYPH = emrgencyph.getText().toString() ;
                EMERGENCYPH2 = emergencyph2.getText().toString() ;
                ADRESS = adress.getText().toString() ;
                OWNPHN = ownphn.getText().toString();
                nid = NID.getText().toString() ;
                uploadDataRoFirebase();
            }
        });


loadDataFromFirebase();

    }

    private void setLocale(String s) {



        Locale locale = new Locale(s);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale ;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        //save Data to shared Pref
        SharedPreferences.Editor editor= getSharedPreferences("settigs" , MODE_PRIVATE).edit();
        editor.putString("MY_LANG", s);
        editor.apply();




    }
    // load languagfe saved in prefences

    public void loadLocale()
    {

                SharedPreferences preferences = getSharedPreferences("settigs" , Activity.MODE_PRIVATE);

                String lang = preferences.getString("MY_LANG", "");
                setLocale(lang);

    }

    public  void loadDataFromFirebase(){

        mdatabse = FirebaseDatabase.getInstance().getReference("users").child(uids);
        mdatabse.keepSynced(true);
        mdatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataModel post = dataSnapshot.getValue(dataModel.class);

                name.setText(post.getName());
                ownphn.setText(post.getOwnNumber());
                adress.setText(post.getAdress());
                emrgencyph.setText(post.getEmNum());
                emergencyph2.setText(post.getEmnumTwo());
                NID.setText(post.getNidNo());



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    public void uploadDataRoFirebase(){

        dataModel  model = new dataModel( NAME ,uids , OWNPHN  , EMERGENCYPH , EMERGENCYPH2  , ADRESS , nid);

        mdatabse.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

              //  pbar.setVisibility(View.INVISIBLE);

                Intent i = new Intent(getApplicationContext() , MainActivity.class);
                startActivity(i);
                finish();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

              //  pbar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Error : "+ e.getMessage() , Toast.LENGTH_SHORT)
                        .show();

            }
        });


    }
}
