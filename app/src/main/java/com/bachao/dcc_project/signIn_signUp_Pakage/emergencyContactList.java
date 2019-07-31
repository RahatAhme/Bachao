package com.bachao.dcc_project.signIn_signUp_Pakage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.bachao.dcc_project.R;
import com.bachao.dcc_project.model.dataModel;

public class emergencyContactList extends AppCompatActivity {

    TextView  ph , phtwo ;

    DatabaseReference mdatabse;
    FirebaseAuth mauth ;
    String uid ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact_list);
        ph = findViewById(R.id.ph1) ;
        phtwo = findViewById(R.id.ph2) ;
        getSupportActionBar().hide();



    }

    public  void loadDataFromFirebase(){
        mauth = FirebaseAuth.getInstance();
        uid = mauth.getUid();
        mdatabse = FirebaseDatabase.getInstance().getReference("users").child(uid);
        mdatabse.keepSynced(true);
        mdatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataModel post = dataSnapshot.getValue(dataModel.class);

             ph .setText(post.getEmNum());
             phtwo.setText(post.getEmnumTwo());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }



    @Override
    protected void onStart() {
        super.onStart();
        loadDataFromFirebase();
    }
}
