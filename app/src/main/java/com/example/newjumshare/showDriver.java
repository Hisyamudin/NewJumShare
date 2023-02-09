package com.example.newjumshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class showDriver extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseRecyclerOptions<showDriverListClass> options;
    FirebaseRecyclerAdapter<showDriverListClass, showDriverListHolder> adapter;
    DatabaseReference DataRef, ref, test;
    String start_datez, locationz, currentUid, driverid, eventname;
    Query refe, reference;
    Button btndriver, btnpassenger;
    LocalDate datenows;
    DateTimeFormatter formatters;
    LocalDate ridenow;
    //ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_driver);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        DataRef = FirebaseDatabase.getInstance().getReference("drivers_data");
        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        test = FirebaseDatabase.getInstance().getReference("lectevent");
        eventname = "";
        btndriver  = findViewById(R.id.btn_driver);
        btnpassenger = findViewById(R.id.btn_passenger);
        datenows = LocalDate.now();
        reference = DataRef.orderByChild("id").equalTo(currentUid);

        btnpassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(showDriver.this, showPassenger.class));
            }
        });

        btndriver.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(showDriver.this, "You already view your driver list", Toast.LENGTH_SHORT).show();



            }
        });

        LoadData();

    }

    private void LoadData() {
        options = new FirebaseRecyclerOptions.Builder<showDriverListClass>().setQuery(DataRef.orderByChild("id").equalTo(currentUid), showDriverListClass.class).build();
        adapter = new FirebaseRecyclerAdapter<showDriverListClass, showDriverListHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull showDriverListHolder holder, int position, @NonNull showDriverListClass model) {
                holder.eventName.setText(model.getEvent_name());
                holder.times.setText(model.getTime());
                String eventId = model.getEvent_name();
                String dates = model.getDate_data();
                formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                ridenow = LocalDate.parse(dates, formatters);
                if (datenows.isEqual(ridenow)){
                    holder.today.setText("Today is the ride");
                }else{
                    holder.today.setText("Today is the not ride");
                }
                test.orderByChild("Event_name").equalTo(eventId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshots : snapshot.getChildren()) {
                                //start_datez = (String) snapshots.child("Start_date").getValue();
                                locationz = (String) snapshots.child("Event_location").getValue();

                                //holder.dates.setText(start_datez);
                                holder.locations.setText(locationz);
                            }
                        } else {
                            //Toast.makeText(showDriver.this, "error.getMessage()", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //.dates.setText(start_datez);
                holder.dates.setText(model.getDate_data());
                holder.locations.setText(locationz);
                holder.numPassenger.setText(String.valueOf(model.getCount()));
                //Toast.makeText(showDriver.this, model.getBooked_data_key() +"", Toast.LENGTH_SHORT).show();
                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(showDriver.this, rideListActivity.class);
                        intent.putExtra("drivers", getRef(position).getKey());
                        intent.putExtra("driverkeyid",model.getDriver_id());
                        intent.putExtra("eventid",model.getEvent_name());
                        intent.putExtra("passid",model.getId());
                        intent.putExtra("keydriver",model.getDriver_id());
                        intent.putExtra("carkey",model.getCar_id());
                        intent.putExtra("bookeddatakey",model.getBooked_data_key());

                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public showDriverListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_my_ride_driver, parent, false);
                return new showDriverListHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(showDriver.this, HomeScreen.class);
        startActivity(intent);

    }

    /*ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                start_datez = snapshot.child("Start_date").getValue().toString();
                locationz = snapshot.child("Event_location").getValue().toString();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

            Toast.makeText(showDriver.this, error.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };*/

    /*ValueEventListener valuesEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                driverid = snapshot.child("id").getValue().toString();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

            Toast.makeText(showDriver.this, error.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };*/

}
