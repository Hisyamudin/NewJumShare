package com.example.newjumshare;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class rideListActivityHolder extends RecyclerView.ViewHolder{

    TextView text1,text2,text3,text4,text5;
    ImageView profpic,reppic,messpic;

    public rideListActivityHolder(@NonNull View itemView) {
        super(itemView);

        text1=itemView.findViewById(R.id.driver_namez);
        text2=itemView.findViewById(R.id.genderz);
        text3=itemView.findViewById(R.id.occupationz);
        text4=itemView.findViewById(R.id.phonez);
        text5=itemView.findViewById(R.id.pickup_location);
        profpic=itemView.findViewById(R.id.profilePic);
        reppic=itemView.findViewById(R.id.message);
        messpic=itemView.findViewById(R.id.report);
    }
}
