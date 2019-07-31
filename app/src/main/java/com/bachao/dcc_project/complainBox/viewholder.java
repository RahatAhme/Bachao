package com.bachao.dcc_project.complainBox;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bachao.dcc_project.R;

public class viewholder extends RecyclerView.ViewHolder {

    View mview ;


    public viewholder(View itemView) {
        super(itemView);

        mview = itemView ;
    }

    public void setDetailsForCoupons(Context ctx,  String date , String compain , String postid  ){
        //Views
        TextView dateview = mview.findViewById(R.id.date);
        TextView complainview = mview.findViewById(R.id.complain);




        //  mResturantNameTv.setText(resName);
        dateview.setText("Date :"+date);
        complainview.setText(compain);





    }

}
