package com.bachao.dcc_project.complainBox;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.bachao.dcc_project.R;
import com.bachao.dcc_project.model.modelForComplain;

public class complainHistory extends AppCompatActivity {


    LinearLayoutManager mlayoutManager ;
    GridLayoutManager mLayoutManager;
    private SearchView.OnQueryTextListener queryTextListener;
    RecyclerView mRecyclerView ;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef , mref ;
    FirebaseAuth mauth ;
    String uid ;
ProgressBar mbar ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_history);

        mauth = FirebaseAuth.getInstance() ;
        uid = mauth.getUid() ;


        mRecyclerView = findViewById(R.id.recyclerview)       ;
        mRecyclerView.setHasFixedSize(true);
        mlayoutManager  = new LinearLayoutManager(getApplicationContext()) ;
        mbar = findViewById(R.id.progressbar_Histroy);


        mRecyclerView.setLayoutManager(mlayoutManager);
        mRef = FirebaseDatabase.getInstance().getReference("complainList").child(uid);


        LoadDataFromFirbase();  // loading data from firebase

    }

    private void LoadDataFromFirbase() {

        FirebaseRecyclerAdapter<modelForComplain, viewholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<modelForComplain, viewholder>(
                        modelForComplain.class,
                        R.layout.rowforcomplainhistory,
                        viewholder.class,
                        mRef
                ) {


                    @Override
                    protected void populateViewHolder(viewholder viewHolder, modelForComplain model, int position) {


                        viewHolder.setDetailsForCoupons(getApplicationContext(), model.getDate(), model.getComplain(), model.getPostId());
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                    mbar.setVisibility(View.GONE);

                            }
                        }, 3000);



                    }

                    @Override
                    public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
                        viewholder viewHolder = super.onCreateViewHolder(parent, viewType);


                        return viewHolder;

                    }
                };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
