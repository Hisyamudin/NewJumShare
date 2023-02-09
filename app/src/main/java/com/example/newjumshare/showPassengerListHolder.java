package com.example.newjumshare;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class showPassengerListHolder extends RecyclerView.ViewHolder {

    ImageView image1,image2,image3;
    TextView text1,text2,text3,text4,text5,text6,today;
    View v;


    public showPassengerListHolder(@NonNull View itemView){
        super (itemView);
        image1= itemView.findViewById(R.id.profilePic);
        text1= itemView.findViewById(R.id.event_name);
        text2= itemView.findViewById(R.id.driver_name);
        text3= itemView.findViewById(R.id.date);
        text4= itemView.findViewById(R.id.time);
        text5= itemView.findViewById(R.id.location);
        text6= itemView.findViewById(R.id.car_capacity);
        //image2= itemView.findViewById(R.id.message);
       //image3= itemView.findViewById(R.id.report);
        today =  itemView.findViewById(R.id.today);

        v=itemView;

    }
}
