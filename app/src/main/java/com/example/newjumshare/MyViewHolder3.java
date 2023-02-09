package com.example.newjumshare;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder3 extends RecyclerView.ViewHolder {

    TextView texts1, texts2, texts3, texts4, texts5, texts6, texts7,texts8,text9;
    View v;


    public MyViewHolder3(@NonNull View itemView) {
        super(itemView);

        texts1 = itemView.findViewById(R.id.text1);
        texts2 = itemView.findViewById(R.id.drivernamer);
        texts3 = itemView.findViewById(R.id.cartype);
        texts4 = itemView.findViewById(R.id.cartcolor);
        texts5 = itemView.findViewById(R.id.carplate);
        texts6 = itemView.findViewById(R.id.pickuplocation);
        texts7 = itemView.findViewById(R.id.destination);
        //texts8 = itemView.findViewById(R.id.Phone);
        //text9 = itemView.findViewById(R.id.availability);
        texts8 = itemView.findViewById(R.id.yes);
        v = itemView;
    }
}
