package com.bachao.dcc_project.splashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.bachao.dcc_project.signIn_signUp_Pakage.Login_Page;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Intent i = new Intent(SplashScreen.this , Login_Page.class);
                    startActivity(i);
                    finish();
                } catch (InterruptedException e) {
                    Toast.makeText(SplashScreen.this, "WelCome To Bachao ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        thread.start();



        /*
        getSupportActionBar().hide();
        EasySplashScreen config = new EasySplashScreen(SplashScreen.this)

                .withFullScreen()
                .withTargetActivity(Home_Activity.class)
                .withSplashTimeOut(2000)
                .withFooterText("\nBuild By MetaCoders")

                .withFullScreen()
                .withBackgroundResource(R.drawable.splash_background);


       // config.getHeaderTextView().setTextColor(android.graphics.Color.WHITE);
        config.getFooterTextView().setTextColor(android.graphics.Color.WHITE);
       // config.getAfterLogoTextView().setTextColor(android.graphics.Color.WHITE);
       // config.getBeforeLogoTextView().setTextColor(android.graphics.Color.WHITE);
        View view = config.create();


        setContentView(view);

*/

}}
