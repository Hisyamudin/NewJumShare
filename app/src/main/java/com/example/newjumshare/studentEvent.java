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

public class studentEvent extends AppCompatActivity {

    TextView test1;
    String lectevent;
    DatabaseReference userRef, DataRef, test, carRef,ref;
    RecyclerView recyclerView, recyclerView2;
    FirebaseRecyclerOptions<event_driver> options;
    FirebaseRecyclerAdapter<event_driver, MyViewHolder2> adapter;
    Button add;
    int testr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_event);

        add = findViewById(R.id.button_add_event);
        lectevent = getIntent().getStringExtra("keys");
        test1 = findViewById(R.id.test);
        lectevent = getIntent().getStringExtra("keys");
        testr =0;
        carRef = FirebaseDatabase.getInstance().getReference().child("user_car");;
        ref = FirebaseDatabase.getInstance().getReference().child("lectevent");

        recyclerView = findViewById(R.id.recyclerView1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(linearLayoutManager.HORIZONTAL);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        //DataRef = FirebaseDatabase.getInstance().getReference().child("lectevent");
        DataRef = FirebaseDatabase.getInstance().getReference("lectevent");
        test = FirebaseDatabase.getInstance().getReference("drivers_data");

        test1.setText(lectevent);

        LoadData();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(studentEvent.this, addEvent.class);
                intent.putExtra("EVENTNAME", lectevent);
                startActivity(intent);

            }
        });

    }

    private void LoadData() {
        options = new FirebaseRecyclerOptions.Builder<event_driver>().setQuery(test.orderByChild("type").equalTo(lectevent), event_driver.class).build();
        adapter = new FirebaseRecyclerAdapter<event_driver, MyViewHolder2>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder2 holder, int position, @NonNull event_driver model) {
                //if (model.getEvent_name().equals(eventsname)) {
                holder.textView1.setText(model.getFullNames());
                holder.textView2.setText(model.getGender());
                holder.textView3.setText(String.valueOf(model.getCount()));

                carRef.child(model.getCar_id()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String capacity = snapshot.child("num_passenger").getValue().toString();

                            holder.textView4.setText(capacity);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //}
                Picasso.get().load(model.getPic()).into(holder.imageView);
                testr= adapter.getItemCount();
                //holder.textView3.setText(testr);
                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(studentEvent.this,studentFreeDriver.class);
                        intent.putExtra("driverKey",getRef(position).getKey());
                        intent.putExtra("carKey",model.getCar_id());
                        startActivity(intent);
                    }
                });
                ref.child(lectevent).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            ref.child(lectevent).child("count").setValue(testr);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_single_view, parent, false);
                return new MyViewHolder2(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}