package com.bachao.dcc_project.signIn_signUp_Pakage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.bachao.dcc_project.MainActivity;
import com.bachao.dcc_project.R;

import java.util.Locale;

public class Login_Page extends AppCompatActivity {

    Button signINBtn, signUPBtn;
    EditText emailIn, PassIn;
    String pass, email;
    private FirebaseAuth mAuth;
    FirebaseUser muser;
    ProgressBar pbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.login_page);

        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        muser = mAuth.getCurrentUser();

        signINBtn = findViewById(R.id.buttonSubmit);
        signUPBtn = findViewById(R.id.buttonRegPage);
        emailIn = findViewById(R.id.emmailInput);
        PassIn = findViewById(R.id.passwordInput);
        pbar = findViewById(R.id.progressBarLoginPage);

        pbar.setVisibility(View.INVISIBLE);
        //

        signINBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Toast.makeText(getApplicationContext() , "toast" , Toast.LENGTH_SHORT)
                // .show();


                pbar.setVisibility(View.VISIBLE);
                email = emailIn.getText().toString();
                pass = PassIn.getText().toString();

                if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(pass)) {
                    startLogIN(email, pass);
                } else {
                    pbar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Login_Page.this, "Enter proper data", Toast.LENGTH_SHORT).show();
                }


            }
        });


        signUPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Registration_Page.class);
                startActivity(i);
                finish();
            }
        });


    }


    private void startLogIN(String email, String pass) {


        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            pbar.setVisibility(View.INVISIBLE);
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            pbar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Error : " + task.getException(), Toast.LENGTH_SHORT)
                                    .show();

                        }
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

    @Override
    protected void onStart() {
        super.onStart();
        if (muser != null) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();

        }

    }

}
