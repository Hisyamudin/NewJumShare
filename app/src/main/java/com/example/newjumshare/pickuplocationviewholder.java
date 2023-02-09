package com.example.newjumshare;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class pickuplocationviewholder extends RecyclerView.ViewHolder{
    TextView texts1;
    ImageView image1;
View v;
    public pickuplocationviewholder(@NonNull View itemView) {
        super(itemView);
        texts1 = itemView.findViewById(R.id.locationname);
        image1 = itemView.findViewById(R.id.profilePic1);
      v= itemView;
    }
}
