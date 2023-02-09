package com.example.newjumshare;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class showDriverListHolder extends RecyclerView.ViewHolder {

    TextView eventName, dates, times, locations, numPassenger,today;
    View v;

    public showDriverListHolder(@NonNull View itemView) {
        super(itemView);
        eventName = itemView.findViewById(R.id.event_name);
        dates = itemView.findViewById(R.id.date);
        times = itemView.findViewById(R.id.time);
        locations = itemView.findViewById(R.id.location);
        numPassenger = itemView.findViewById(R.id.car_capacity);
        today =  itemView.findViewById(R.id.today);
        v=itemView;

    }
}
