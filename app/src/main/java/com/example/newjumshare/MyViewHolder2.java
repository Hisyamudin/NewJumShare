package com.example.newjumshare;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.rxjava3.annotations.NonNull;

public class MyViewHolder2 extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView textView1,textView2,textView3,textView4,getTextView5;
    View v;

    public MyViewHolder2(@NonNull View itemView){
        super(itemView);
        imageView = itemView.findViewById(R.id.profilePic);
        textView1 = itemView.findViewById(R.id.driver_name);
        textView2 = itemView.findViewById(R.id.driver_gender);
        textView3 = itemView.findViewById(R.id.car_capacity);
        textView4 = itemView.findViewById(R.id.total_capacity);
        getTextView5 = itemView.findViewById(R.id.rating);
        v=itemView;

    }

}

