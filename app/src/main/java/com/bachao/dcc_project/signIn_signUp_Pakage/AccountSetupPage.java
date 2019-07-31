package com.bachao.dcc_project.signIn_signUp_Pakage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.bachao.dcc_project.MainActivity;
import com.bachao.dcc_project.R;
import com.bachao.dcc_project.model.dataModel;

import java.util.Locale;

public class AccountSetupPage extends AppCompatActivity {


    String uids, names, emNums, emNum2s, adresss, nids, ownNumbers;
    EditText NAME, emNUM, ENUM2, adressInput, PH, NID;
    DatabaseReference mref;
    FirebaseAuth matuh;
    Button submitBtn;

    ProgressBar pbar;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_account_setup_page);
        getSupportActionBar().hide();
        matuh = FirebaseAuth.getInstance();
        uids = matuh.getUid();

        mref = FirebaseDatabase.getInstance().getReference("users").child(uids);

        NAME = findViewById(R.id.nameInput);
        emNUM = findViewById(R.id.emergencyNumber1);
        ENUM2 = findViewById(R.id.emergencyNumber2);
        PH = findViewById(R.id.phoneIN);
        adressInput = findViewById(R.id.adressIN);
        pbar = findViewById(R.id.progressBarAcSetupPage);
        pbar.setVisibility(View.INVISIBLE);
        NID = findViewById(R.id.nidNo);

        submitBtn = findViewById(R.id.createBtn);

        preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = preferences.edit();


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbar.setVisibility(View.VISIBLE);
                names = NAME.getText().toString();
                emNums = emNUM.getText().toString();
                emNum2s = ENUM2.getText().toString();
                adresss = adressInput.getText().toString();
                ownNumbers = PH.getText().toString();
                nids = NID.getText().toString();

                if (!TextUtils.isEmpty(names) || !TextUtils.isEmpty(emNum2s) || !TextUtils.isEmpty(emNums) || !TextUtils.isEmpty(adresss) || !TextUtils.isEmpty(ownNumbers)) {
                    editor.putString("nid", nids);
                    editor.apply();

                    UploadDataToFireBase();

                } else {
                    pbar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Enter Value Properly !!  ", Toast.LENGTH_SHORT)
                            .show();

                }

            }
        });

    }


    public void UploadDataToFireBase() {

        dataModel model = new dataModel(names, uids, ownNumbers, emNums, emNum2s, adresss, nids);

        mref.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                pbar.setVisibility(View.INVISIBLE);

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pbar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT)
                        .show();

            }
        });


    }

    private void setLocale(String s) {

        Locale locale = new Locale(s);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        //save Data to shared Pref
        SharedPreferences.Editor editor = getSharedPreferences("settigs", MODE_PRIVATE).edit();
        editor.putString("MY_LANG", s);
        editor.apply();

    }
    // load languagfe saved in prefences

    public void loadLocale() {

        SharedPreferences preferences = getSharedPreferences("settigs", Activity.MODE_PRIVATE);

        String lang = preferences.getString("MY_LANG", "");
        setLocale(lang);

    }


}
