package com.example.newjumshare;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

public class requestListHolder extends RecyclerView.ViewHolder {

    ImageView profile_pic;
    TextView text1,text2,text3,text4,text5;
    View v;

    public requestListHolder(@NonNull View itemView){
        super(itemView);
        profile_pic=itemView.findViewById(R.id.profilePic);
        text1=itemView.findViewById(R.id.passenger_name);
        text2=itemView.findViewById(R.id.event_name);
        text3=itemView.findViewById(R.id.date);
        text4=itemView.findViewById(R.id.location);
        text5 =itemView.findViewById(R.id.gender);
        v=itemView;
    }
}
