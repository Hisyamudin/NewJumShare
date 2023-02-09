package com.example.newjumshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class passengerRideListActivity extends AppCompatActivity {

    TextView eventName, event_date, event_time, event_location, driver_name, driver_brand, driver_plate, driver_phone, pickupdate, pickuptime;
    ImageView driver_pic, passenger_pic, report, message;
    DatabaseReference eventrefs, driverrefs, carsref, passengerdata, passref, locationref, driver_ref, driverdelref, userref2;
    String bookedkey, driv, driverkeyid, eventid, passids, carkey, driverkeyids, pic, name, pics;
    RecyclerView recyclerView;
    Button btn_cancel;
    FirebaseRecyclerOptions<bookedDriversClas> options;
    FirebaseRecyclerAdapter<bookedDriversClas, passengerRideListActivityHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_ride_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager((getApplicationContext())));
        recyclerView.setHasFixedSize(true);
        eventName = findViewById(R.id.text1);
        event_date = findViewById(R.id.textDate);
        event_time = findViewById(R.id.texttime);
        event_location = findViewById(R.id.textlocation);
        driver_name = findViewById(R.id.driver_namez);
        driver_brand = findViewById(R.id.brand);
        driver_plate = findViewById(R.id.plate);
        driver_phone = findViewById(R.id.phonez);
        report = findViewById(R.id.report);
        message = findViewById(R.id.message);
        driver_pic = findViewById(R.id.profilePic);
        //event_date = findViewById(R.id.textDate);
        driverdelref = FirebaseDatabase.getInstance().getReference().child("booked_drivers");
        eventrefs = FirebaseDatabase.getInstance().getReference().child("lectevent");
        driverrefs = FirebaseDatabase.getInstance().getReference("users");
        carsref = FirebaseDatabase.getInstance().getReference().child("user_car");
        passengerdata = FirebaseDatabase.getInstance().getReference("booked_drivers");
        locationref = FirebaseDatabase.getInstance().getReference("booked_drivers");
        passref = FirebaseDatabase.getInstance().getReference().child("users");
        //test.orderByChild("event_name").equalTo(lectevent)
        bookedkey = getIntent().getStringExtra("drivers");
        driverkeyid = getIntent().getStringExtra("driverkeyid");
        driverkeyids = getIntent().getStringExtra("driverkeyids");//this
        pics = getIntent().getStringExtra("driverpic");
        eventid = getIntent().getStringExtra("eventid");
        carkey = getIntent().getStringExtra("carkey");
        passids = getIntent().getStringExtra("passid");
        driver_ref = FirebaseDatabase.getInstance().getReference().child("drivers_data");
        pickupdate = findViewById(R.id.pickupdate);
        pickuptime = findViewById(R.id.pickuptime);
        btn_cancel = findViewById(R.id.cancel_button);
        androidx.appcompat.app.AlertDialog.Builder builder;
        builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        userref2 = FirebaseDatabase.getInstance().getReference("users");


        LoadData();

        carsref.child(carkey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String brand = (String) snapshot.child("cartype").getValue();
                    String plate = (String) snapshot.child("carplate").getValue();

                    driver_brand.setText(brand);
                    driver_plate.setText(plate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        driver_ref.child(driverkeyids).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        String datepick = (String) snapshot.child("date_data").getValue();
                        String timepick = (String) snapshot.child("time").getValue();
                        pickupdate.setText(datepick);
                        pickuptime.setText(timepick);
                    }
                } else {
                    //Toast.makeText(passengerRideListActivity.this, "error.getMessage()", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        driverrefs.orderByChild("uid").equalTo(driverkeyid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        name = (String) snapshots.child("name").getValue();
                        String fon = (String) snapshots.child("phoneNo").getValue();
                        //pics =(String)snapshot.child("pic").getValue();
                        driv = (String) snapshots.child("uid").getValue();
                        //
                        driver_name.setText(name);
                        driver_phone.setText(fon);
                        Picasso.get().load(pics).into(driver_pic);
                    }
                } else {
                    //Toast.makeText(passengerRideListActivity.this, "error.getMessage()", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(passengerRideListActivity.this, Chat.class);
                intent.putExtra("userID", driverkeyid);
                intent.putExtra("name", name);
                intent.putExtra("imageuri", pics);
                //intent.putExtra("userID", model.getPassenger_id());

                startActivity(intent);

            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(passengerRideListActivity.this, reportUserActivity.class);
                intent.putExtra("userID", driv);

                startActivity(intent);
            }
        });
        eventrefs.child(eventid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String eventname = (String) snapshot.child("Event_name").getValue();
                    String datez = (String) snapshot.child("Start_date").getValue();
                    String timezz = (String) snapshot.child("Start_time").getValue();
                    String Location = (String) snapshot.child("Event_location").getValue();

                    eventName.setText(eventname);
                    event_date.setText(datez);
                    event_time.setText(timezz);
                    event_location.setText(Location);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("Cancelling to join the carpooling session.")
                        .setMessage("Are you sure you don not want to join the ride?")
                        .setCancelable(true)
                        .setPositiveButton("Cancel the Ride", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                Query bookeddata2 = FirebaseDatabase.getInstance().getReference("booked_drivers").orderByChild("key_driver").equalTo(driverkeyids);
                                bookeddata2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                            String passengerid = childSnapshot.child("passenger_id").getValue(String.class);
                                            if (passengerid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                                childSnapshot.getRef().removeValue();

                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        //Log.w("Data removed", "failure", databaseError.toException());
                                    }


                            });
                            Intent intent = new Intent(passengerRideListActivity.this, HomeScreen.class);
                            userref2.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("drivernum").setValue(ServerValue.increment(-1));
                            startActivity(intent);
                        }
            })
                    .

            setNegativeButton("Don't Cancel.",new DialogInterface.OnClickListener() {
                @Override
                public void onClick (DialogInterface dialogInterface,int i){

                }
            }).

            show();
        }
    });

}

    private void LoadData() {
        options = new FirebaseRecyclerOptions.Builder<bookedDriversClas>().setQuery(passengerdata.orderByChild("key_driver").equalTo(driverkeyids), bookedDriversClas.class).build();
        adapter = new FirebaseRecyclerAdapter<bookedDriversClas, passengerRideListActivityHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull passengerRideListActivityHolder holder, int position, @NonNull bookedDriversClas model) {
                holder.text1.setText(model.getName());
                passref.child(model.getPassenger_id()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String gendz = (String) snapshot.child("gender").getValue();
                            String occ = (String) snapshot.child("occupation").getValue();
                            String phonz = (String) snapshot.child("phoneNo").getValue();
                            pic = (String) snapshot.child("pic").getValue();
                            holder.text2.setText(gendz);
                            holder.text3.setText(occ);
                            holder.text4.setText(phonz);
                            Picasso.get().load(pic).into(holder.profpic);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                holder.messpic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (model.getPassenger_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            Toast.makeText(passengerRideListActivity.this, "You can not report yourself", Toast.LENGTH_SHORT).show();

                        } else {
                            Intent intent = new Intent(passengerRideListActivity.this, reportUserActivity.class);
                            intent.putExtra("userID", model.getPassenger_id());

                            startActivity(intent);
                        }
                    }
                });

                locationref.orderByChild("passenger_id").equalTo(model.getPassenger_id()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshots : snapshot.getChildren()) {
                                String loc = (String) snapshots.child("driver_event").getValue();

                                holder.text5.setText(loc);
                            }
                        } else {
                            //Toast.makeText(passengerRideListActivity.this, "error.getMessage()", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                holder.reppic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //pic = model.getPassenger_id();
                        if (model.getPassenger_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            Toast.makeText(passengerRideListActivity.this, "You can not chat yourself", Toast.LENGTH_SHORT).show();

                        } else {
                            Intent intent = new Intent(passengerRideListActivity.this, Chat.class);
                            intent.putExtra("userID", model.getPassenger_id());
                            intent.putExtra("name", model.getName());
                            intent.putExtra("imageuri", pic);
                            intent.putExtra("lectevent", getRef(position).getKey());
                            //intent.putExtra("userID", model.getPassenger_id());

                            startActivity(intent);
                        }
                    }
                });
                //holder.text2.setText(model.getName());
                //holder.text3.setText(model.getName());
                //holder.text4.setText(model.getName());
                //holder.text5.setText(model.getName());
                //Picasso.get().load(model.getImageUrl()).into(holder.profpic);
            }

            @NonNull
            @Override
            public passengerRideListActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_passengerridelist, parent, false);
                return new passengerRideListActivityHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}