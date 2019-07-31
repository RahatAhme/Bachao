package com.bachao.dcc_project;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.codemybrainsout.ratingdialog.RatingDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.bachao.dcc_project.complainBox.abooutUs;
import com.bachao.dcc_project.complainBox.complainBox;
import com.bachao.dcc_project.model.dataModel;
import com.bachao.dcc_project.signIn_signUp_Pakage.Login_Page;
import com.bachao.dcc_project.signIn_signUp_Pakage.emergencyContactList;

import java.util.Locale;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    FirebaseUser muser;
    DatabaseReference mdatabse;
    FirebaseAuth mauth;
    String uid;
    TextView namee;
    LinearLayout map, emer, complain, about;
    private static final int PERMISSION_SEND_SMS = 123;


    String name, emNum, emNUM2, adress, nid;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    public String Latitude, Longitude, msgBody;

    public static Boolean isActive = false;

    private Button btStartService;
    private TextView tvText;

    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main);

        requestSmsPermission();

        mauth = FirebaseAuth.getInstance();


        uid = mauth.getUid();
        muser = mauth.getCurrentUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        namee = findViewById(R.id.nameInHOme);
        map = findViewById(R.id.mapBtn);
        emer = findViewById(R.id.emerBtn);
        complain = findViewById(R.id.complainId);
        about = findViewById(R.id.aboutId);
        setSupportActionBar(toolbar);


        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent y = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(y);

            }
        });
        emer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent y = new Intent(MainActivity.this, emergencyContactList.class);
                startActivity(y);


            }
        });
        complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent y = new Intent(MainActivity.this, complainBox.class);
                startActivity(y);


            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // sending sms to the friends in need from the firebaseDatabase

                loadDataFromFirebase();  // loading data From Firebase
                // calling sms api for sending msg
                GPSTracker gpsTracker = new GPSTracker(getApplicationContext());

                String latitude = String.valueOf(gpsTracker.getLatitude());
                String longitude = String.valueOf(gpsTracker.getLongitude());

                //  Toast.makeText(this, "Help mode activated "+ emNum +" "+ emNUM2, Toast.LENGTH_SHORT).show();

                SmsManager smsManager = SmsManager.getDefault();


                //   String test = "http://maps.google.com/maps?saddr=" +latitude+","+longitude;
                String test = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;


                //String test = "https://www.google.com/maps/search/?api=1&query="+latitude+","+longitude;

                msgBody = "I am in Danger. Help me !! My Current Location is \n " + test;


                if (!emNum.contains("null")) {

                    smsManager.sendTextMessage(emNum, null, msgBody, null, null);
                }
                if (!emNUM2.contains("null")) {

                    smsManager.sendTextMessage(emNUM2, null, msgBody, null, null);
                }

                Toast.makeText(getApplicationContext(), "Asking For Help !!!", Toast.LENGTH_SHORT)
                        .show();


            }
        });


        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE}, 1);

        }


        btStartService = (Button) findViewById(R.id.btStartService);
        tvText = (TextView) findViewById(R.id.tvText);
        enableAutoStart();


        if (checkServiceRunning()) {
            btStartService.setText(getString(R.string.stop_service));
            tvText.setVisibility(View.VISIBLE);
        }


        btStartService.setOnClickListener(v -> {
            if (btStartService.getText().toString().equalsIgnoreCase(getString(R.string.start_service))) {

                loadDataFromFirebase();
                Intent i = new Intent(MainActivity.this, MyService.class);

                i.putExtra("num1", emNum);
                i.putExtra("num2", emNUM2);
                startService(i);
                btStartService.setText(getString(R.string.stop_service));
                tvText.setVisibility(View.VISIBLE);
                tvText.setText(mauth.getUid());
            } else {
                stopService(new Intent(MainActivity.this, MyService.class));
                btStartService.setText(getString(R.string.start_service));
                tvText.setVisibility(View.GONE);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        displaySelectedScreen(R.id.nav_inst);


/*
        //wi;; delete
        SQLiteDatabase db = openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT * FROM details", null);

        while (c.moveToFirst())
        {
            Log.d("Number", c.getString(1));
        }*/


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_power:
                android.support.v7.app.ActionBar bar = getSupportActionBar();
                //item.setTitle("changed");

                Log.d("Title:", item.getTitle().toString());

                if (item.getTitle().toString().contains("START")) {
                    Log.d("Title2:", "If er vitore");

                    //  SQLiteDatabase db2 = this.openOrCreateDatabase("NumberDB", MODE_PRIVATE, null);
                    loadDataFromFirebase();
                    Toast.makeText(getApplicationContext(), "Listening For The Danger ", Toast.LENGTH_SHORT)
                            .show();

                    //  Log.d("Number is:", Register.getNumber(db2));

                    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#78C257")));

                    Intent o = new Intent(new Intent(MainActivity.this, MyService.class));
                    o.putExtra("num1", emNum);
                    o.putExtra("num2", emNUM2);
                    startService(o);

                    item.setTitle("STOP SERVICE");
                    loadDataFromFirebase();

                    return true;
                } else {
                    Toast.makeText(getApplicationContext(), "Listening Stopped ", Toast.LENGTH_SHORT)
                            .show();
                    //  recreate();
                    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E81123")));

                    Intent o = new Intent(new Intent(MainActivity.this, MyService.class));
                    o.putExtra("num1", emNum);
                    o.putExtra("num2", emNUM2);
                    startService(o);
                    item.setTitle("START SERVICE");
                    return true;
                }

/*
                    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#439EB8")));
                    stopService(new Intent(MainActivity.this, MyService.class));
                    return true;
*/
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean checkServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                    Integer.MAX_VALUE)) {
                if (getString(R.string.my_service_name).equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void enableAutoStart() {

        for (Intent intent : Constants.AUTO_START_INTENTS) {

            if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                new MaterialDialog.Builder(this).title(R.string.enable_autostart)
                        .content(R.string.ask_permission)
                        .theme(Theme.LIGHT)
                        .positiveText(getString(R.string.allow))
                        .onPositive((dialog, which) -> {
                            try {
                                for (Intent intent1 : Constants.AUTO_START_INTENTS)
                                    if (getPackageManager().resolveActivity(intent1, PackageManager.MATCH_DEFAULT_ONLY)
                                            != null) {
                                        startActivity(intent1);
                                        break;
                                    }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        })
                        .show();
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finishAffinity();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.newbutton, menu);

        // Locate MenuItem with ShareActionProvider
        // MenuItem item = menu.findItem(R.id.menu_share);

        // Fetch and store ShareActionProvider
        //mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        //setIntent("Testing Share Feature");
        // Return true to display menu

        return true;
    }

    // Call to update the share intent
    private void setIntent(String s) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_TEXT, s);
        mShareActionProvider.setShareIntent(intent);

    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
*/
   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());

        // Handle navigation view item clicks here.
        /*int id = item.getItemId();
        if (id == R.id.nav_camera) {

            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } *//*else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);*/
        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_inst: {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.popup_layout, null);
                AlertDialog alertDialog = new AlertDialog.Builder(
                        MainActivity.this).create();

                // Setting Dialog Title
                alertDialog.setTitle("Instructions");
                // Setting Dialog Message
                alertDialog.setView(alertLayout);

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.instruct_icon);


                // Showing Alert Message
                //   alertDialog.show();
            }
            break;

            case R.id.profile:
                Intent i = new Intent(MainActivity.this, profile.class);
                startActivity(i);
                break;
            case R.id.nav_verify:
                fragment = new Verify();
                break;
            case R.id.about_us:
                Intent ti = new Intent(MainActivity.this, abooutUs.class);
                startActivity(ti);
                break;
            case R.id.ComplainBOx:
                Intent yyt = new Intent(MainActivity.this, complainBox.class);
                startActivity(yyt);
                break;
            case R.id.nav_nearby:
                Intent yy = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(yy);
                break;
            case R.id.log_out_btn:

                mauth.signOut();
                Intent y = new Intent(MainActivity.this, Login_Page.class);
                startActivity(y);
                finish();
                break;


            case R.id.nav_rate:
                final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                        .threshold(3)
                        .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
                            @Override
                            public void onFormSubmitted(String feedback) {

                            }
                        }).build();

                ratingDialog.show();
                break;
            case R.id.nav_safety:
//                Intent intent = new Intent(MainActivity.this, Safety.class);
//                startActivity(intent);
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

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

    public void loadDataFromFirebase() {

        mdatabse = FirebaseDatabase.getInstance().getReference("users").child(uid);
        mdatabse.keepSynced(true);
        mdatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataModel post = dataSnapshot.getValue(dataModel.class);

                emNum = post.getEmNum();
                emNUM2 = post.getEmnumTwo();
                namee.setText(post.getName());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //  Toast.makeText(this, name +" :" + emNum + "  " + emNUM2, Toast.LENGTH_SHORT).show();

    }

    public String getemNumber() {

        return emNum;

    }

    public String getemNumber2() {

        return emNUM2;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "You Have The Permission", Toast.LENGTH_SHORT)
                        .show();
            }


        }


    }

    private void requestSmsPermission() {

        String permission = Manifest.permission.READ_SMS;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 1);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


        loadDataFromFirebase();


    }

}
