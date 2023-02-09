package com.example.newjumshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class showPassenger extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btndriver, btnpassenger;
    FirebaseRecyclerOptions<showPassengerClas> options;
    FirebaseRecyclerAdapter<showPassengerClas, showPassengerListHolder> adapter;
    DatabaseReference DataRef, test, ref, tests, refdriv;
    String currentUid, pic, eventz, datez, timez, locationz;
    int num_pass;
    LocalDate datenows;
    DateTimeFormatter formatters;
    LocalDate ridenow;
    TextView capacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_passenger);

        recyclerView = findViewById(R.id.recyclerView);
        btndriver = findViewById(R.id.btn_driver);
        btnpassenger = findViewById(R.id.btn_passenger);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        DataRef = FirebaseDatabase.getInstance().getReference().child("booked_drivers");
        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        test = FirebaseDatabase.getInstance().getReference("users");
        //tests=FirebaseDatabase.getInstance().getReference("users");
        ref = FirebaseDatabase.getInstance().getReference("lectevent");
        refdriv = FirebaseDatabase.getInstance().getReference("drivers_data");
        num_pass = 0;
        //reference = DataRef.orderByChild("id").equalTo(currentUid);
        datenows = LocalDate.now();

        btnpassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(showPassenger.this, "You already view your passenger list", Toast.LENGTH_SHORT).show();
            }
        });

        btndriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(showPassenger.this, showDriver.class));

            }
        });

        LoadData();

    }

    private void LoadData() {

        options = new FirebaseRecyclerOptions.Builder<showPassengerClas>().setQuery(DataRef.orderByChild("passenger_id").equalTo(currentUid), showPassengerClas.class).build();
        adapter = new FirebaseRecyclerAdapter<showPassengerClas, showPassengerListHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull showPassengerListHolder holder, int position, @NonNull showPassengerClas model) {
                // holder.text1.setText(model.getName());
                //holder.text2.setText(model.getName());
                //holder.text3.setText(model.getName());
                //holder.text4.setText(model.getName());
                //holder.text5.setText(model.getDriver_event());
                //holder.text6.setText(model.getName());
                num_pass = adapter.getItemCount();

                String dates = model.getDriverdate();
                formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                ridenow = LocalDate.parse(dates, formatters);
                if (datenows.isEqual(ridenow)){
                    holder.today.setText("Today");
                }else{
                    holder.today.setText("Not Today");
                }
                refdriv.orderByChild("driver_id").equalTo(model.getKey_driver()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshots : snapshot.getChildren()) {
                                timez = (String) snapshots.child("time").getValue();
                                holder.text4.setText(timez);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                test.orderByChild("uid").equalTo(model.getDriver_id()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshots : snapshot.getChildren()) {
                                pic = (String) snapshots.child("pic").getValue();
                                String name = (String) snapshots.child("name").getValue();
                                holder.text2.setText(name);
                                Picasso.get().load(pic).into(holder.image1);

                                //holder.dates.setText(start_datez);
                                //holder.locations.setText(locationz);
                            }
                        } else {
                            //Toast.makeText(showPassenger.this, "error.getMessage()", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                /*test.orderByChild("uid").equalTo(model.getDriver_id()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshots : snapshot.getChildren()) {
                                String  name  =     (String) snapshots.child("name").getValue();
                                holder.text2.setText(name);

                                //holder.dates.setText(start_datez);
                                //holder.locations.setText(locationz);
                            }
                        } else {
                            Toast.makeText(showPassenger.this, "error.getMessage()", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });*/

                ref.orderByChild("Event_name").equalTo(model.getEvent_id()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshots : snapshot.getChildren()) {
                                //pic = (String) snapshots.child("pic").getValue();
                                eventz = (String) snapshots.child("Event_name").getValue();
                                datez = (String) snapshots.child("Start_date").getValue();
                                //timez = (String) snapshots.child("Start_time").getValue();

                                locationz = (String) snapshots.child("Event_location").getValue();
                                holder.text1.setText(eventz);


                                holder.text5.setText(locationz);
                            }
                        } else {
                            //Toast.makeText(showPassenger.this, "error.getMessage()", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Picasso.get().load(pic).into(holder.image1);
                holder.text1.setText(eventz);
                holder.text4.setText(timez);
                holder.text5.setText(locationz);
                holder.text3.setText(model.getDriverdate());
                /*holder.image3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(showPassenger.this, reportUserActivity.class);
                        intent.putExtra("drivers", getRef(position).getKey());
                        intent.putExtra("driverkeyid", model.getDriver_id());
                        intent.putExtra("eventid", model.getEvent_id());
                        intent.putExtra("passid", model.getId());
                        intent.putExtra("carkey", model.getCar_id());
                        intent.putExtra("driverkeyids", model.getKey_driver());

                        startActivity(intent);
                    }
                });*/
                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(showPassenger.this, passengerRideListActivity.class);
                        intent.putExtra("drivers", getRef(position).getKey());
                        intent.putExtra("driverkeyid", model.getDriver_id());
                        intent.putExtra("eventid", model.getEvent_id());
                        intent.putExtra("passid", model.getId());
                        intent.putExtra("carkey", model.getCar_id());
                        intent.putExtra("driverkeyids", model.getKey_driver());
                        intent.putExtra("driverpic",pic);


                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public showPassengerListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_my_ride_passenger, parent, false);
                return new showPassengerListHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}