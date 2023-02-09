package com.example.newjumshare;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class chooselocationActivityHolder extends RecyclerView.ViewHolder {
    TextView name, location;
    public chooselocationActivityHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.passengername);
        location = itemView.findViewById(R.id.location);

    }
}
