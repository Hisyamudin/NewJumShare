package com.example.newjumshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class studentEvent2 extends AppCompatActivity {
    TextView test1;
    String lectevent;
    DatabaseReference userRef, DataRef, test, carRef, ref;
    RecyclerView recyclerView, recyclerView2;
    FirebaseRecyclerOptions<studentdriverclass> options;
    FirebaseRecyclerAdapter<studentdriverclass, MyViewHolder> adapter;

    Button add;
    int testr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_event2);

        add = findViewById(R.id.button_add_event);
        //lectevent = getIntent().getStringExtra("keys");
        test1 = findViewById(R.id.test);
        lectevent = getIntent().getStringExtra("keys");
        testr = 0;
        carRef = FirebaseDatabase.getInstance().getReference().child("user_car");

        ref = FirebaseDatabase.getInstance().getReference().child("lectevent");

        recyclerView = findViewById(R.id.recyclerView1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(linearLayoutManager.HORIZONTAL);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        //DataRef = FirebaseDatabase.getInstance().getReference().child("lectevent");
        DataRef = FirebaseDatabase.getInstance().getReference("drivers_data");
        test = FirebaseDatabase.getInstance().getReference("drivers_data");

        test1.setText(lectevent);

        LoadData();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(studentEvent2.this, addstudentdriver.class);
                intent.putExtra("keys", lectevent);
                startActivity(intent);

            }
        });

    }

    private void LoadData() {
        options = new FirebaseRecyclerOptions.Builder<studentdriverclass>().setQuery(DataRef.orderByChild("event_name").equalTo(lectevent), studentdriverclass.class).build();
        adapter = new FirebaseRecyclerAdapter<studentdriverclass, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull studentdriverclass model) {
                holder.texts1.setText(model.getEvent_name());
                holder.texts2.setText(model.getDate_data());
                /*holder.texts3.setText(model.getEnd_date());
                holder.texts4.setText(model.getStart_time());
                holder.texts5.setText(model.getEnd_time());
                holder.texts6.setText(model.getEvent_location());
                holder.texts7.setText(model.getEvent_description());
                holder.texts8.setText(model.getPhone());*/
                //holder.text9.setText(model.getCount());
                holder.text9.setText(String.valueOf(model.getCount()));
                holder.text10.setText(String.valueOf(model.getDestination()));
                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(studentEvent2.this, booked_driver.class);
                        intent.putExtra("driverKey", getRef(position).getKey());
                        intent.putExtra("carKey", model.getCar_id());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view2, parent, false);

                return new MyViewHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}