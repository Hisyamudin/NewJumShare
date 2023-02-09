package com.example.newjumshare;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView texts1, texts2, texts3, texts4, texts5, texts6, texts7,texts8,text9,text10,text11;
    View v;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        texts1 = itemView.findViewById(R.id.text1);
        texts2 = itemView.findViewById(R.id.textDate);
        texts3 = itemView.findViewById(R.id.textEndDate);
        texts4 = itemView.findViewById(R.id.startTimes);
        texts5 = itemView.findViewById(R.id.endTimes);
        texts6 = itemView.findViewById(R.id.eventLocation);
        texts7 = itemView.findViewById(R.id.desc);
        texts8 = itemView.findViewById(R.id.Phone);
        text9 = itemView.findViewById(R.id.availability);
        text10 = itemView.findViewById(R.id.destinations);
        text11 = itemView.findViewById(R.id.endDate);
        v = itemView;
    }
}
