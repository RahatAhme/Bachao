package com.bachao.dcc_project.signIn_signUp_Pakage;

import android.app.Activity;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.bachao.dcc_project.R;

import java.util.Locale;

public class Registration_Page extends AppCompatActivity {

    Button nextBtn ;
    EditText  mailIn , PassIn ;
    String mail , pass ;
    ProgressBar pbar ;
    FirebaseAuth mauth  ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_registration__page);
        mauth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();

        mailIn = findViewById(R.id.mailINput);
        PassIn = findViewById(R.id.passwordINPUT);
        nextBtn = findViewById(R.id.NextBtn);
        pbar = findViewById(R.id.progressBarRegiterPage);
        pbar.setVisibility(View.INVISIBLE);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mail = mailIn.getText().toString();
                pass  = PassIn.getText().toString();


                if (!TextUtils.isEmpty(mail)|| !TextUtils.isEmpty(pass)){

                    pbar.setVisibility(View.VISIBLE);
                    SignUpUserInGOOGLE(mail  , pass);

                }
                else {

                    Toast.makeText(getApplicationContext() , "Enter Value Properly" , Toast.LENGTH_SHORT)
                            .show();
                    pbar.setVisibility(View.INVISIBLE);

                }



            }
        });



    }

    private void SignUpUserInGOOGLE(String mail, String pass) {


        mauth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            pbar.setVisibility(View.INVISIBLE);
                            Intent oi = new Intent(getApplicationContext(), AccountSetupPage.class);
                            startActivity(oi);
                            finish();


                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(Registration_Page.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            pbar.setVisibility(View.INVISIBLE);
                        }
                    }
                });


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
    public void loadLocale() {

        SharedPreferences preferences = getSharedPreferences("settigs" , Activity.MODE_PRIVATE);

        String lang = preferences.getString("MY_LANG", "");
        setLocale(lang);

    }
}
