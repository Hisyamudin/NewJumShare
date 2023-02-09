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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class rideListActivity extends AppCompatActivity {

    TextView eventName, event_date, event_time, event_location, driver_name, driver_brand, driver_plate, driver_phone, pickupdate, pickuptime;
    ImageView driver_pic, passenger_pic, report, message;
    DatabaseReference eventrefs, driverrefs, carsref, passengerdata, passref, locationref, refdriv, ongoingref, requestref, refdriv2, refdriver3, userref2;
    ;
    String bookedkey, driverkeyid, eventid, passids, carkey, brand, plate, name, fon, bookeddatakeys, datepick;
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<bookedDriversClas> options;
    FirebaseRecyclerAdapter<bookedDriversClas, rideListActivityHolder> adapter;
    Button btn_start, btn_cancel;
    String pic, Location, keydriver;
    int num_pass;
    DateTimeFormatter formatters;
    LocalDate ridenow, datenows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_list);

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
        //report = findViewById(R.id.report);
        //message = findViewById(R.id.message);
        driver_pic = findViewById(R.id.profilePic);
        //event_date = findViewById(R.id.textDate);
        eventrefs = FirebaseDatabase.getInstance().getReference().child("lectevent");
        driverrefs = FirebaseDatabase.getInstance().getReference().child("users");
        carsref = FirebaseDatabase.getInstance().getReference().child("user_car");
        passengerdata = FirebaseDatabase.getInstance().getReference("booked_drivers");
        locationref = FirebaseDatabase.getInstance().getReference("booked_drivers");
        passref = FirebaseDatabase.getInstance().getReference().child("users");
        //test.orderByChild("event_name").equalTo(lectevent)
        bookedkey = getIntent().getStringExtra("drivers");
        bookeddatakeys = getIntent().getStringExtra("bookeddatakey");
        driverkeyid = getIntent().getStringExtra("driverkeyid");
        eventid = getIntent().getStringExtra("eventid");
        carkey = getIntent().getStringExtra("carkey");
        passids = getIntent().getStringExtra("passid");
        btn_start = findViewById(R.id.start_button);
        refdriv = FirebaseDatabase.getInstance().getReference().child("drivers_data");
        refdriv2 = FirebaseDatabase.getInstance().getReference("drivers_data");
        ongoingref = FirebaseDatabase.getInstance().getReference("booked_drivers");
        requestref = FirebaseDatabase.getInstance().getReference("ongoing_data");
        refdriver3 = FirebaseDatabase.getInstance().getReference().child("drivers_data");
        pickupdate = findViewById(R.id.pickupdate);
        pickuptime = findViewById(R.id.pickuptime);
        formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        datenows = LocalDate.now();
        keydriver = getIntent().getStringExtra("keydriver");
        btn_cancel = findViewById(R.id.cancel_button);
        //ridenow = LocalDate.parse(ridedate, formatters);
        androidx.appcompat.app.AlertDialog.Builder builder;
        builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        userref2 = FirebaseDatabase.getInstance().getReference("users");

        LoadData();


        carsref.child(carkey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    brand = (String) snapshot.child("cartype").getValue();
                    plate = (String) snapshot.child("carplate").getValue();

                    driver_brand.setText(brand);
                    driver_plate.setText(plate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        driverrefs.child(passids).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    name = (String) snapshot.child("name").getValue();
                    fon = (String) snapshot.child("phoneNo").getValue();
                    driver_name.setText(name);
                    driver_phone.setText(fon);
                    String pic = (String) snapshot.child("pic").getValue();
                    Picasso.get().load(pic).into(driver_pic);

//                    String datepick = (String) snapshot.child("driverdate").getValue();
//                    String timepick = (String) snapshot.child("pickuptime").getValue();
//                    pickupdate.setText(datepick);
//                    pickuptime.setText(timepick);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refdriver3.child(driverkeyid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    datepick = (String) snapshot.child("date_data").getValue();
                    String timepick = (String) snapshot.child("time").getValue();
                    pickupdate.setText(datepick);
                    pickuptime.setText(timepick);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        eventrefs.child(eventid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String eventname = (String) snapshot.child("Event_name").getValue();
                    String datez = (String) snapshot.child("Start_date").getValue();
                    String timezz = (String) snapshot.child("Start_time").getValue();
                    Location = (String) snapshot.child("Event_location").getValue();

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

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ridenow = LocalDate.parse(datepick, formatters);
                if (ridenow.isAfter(datenows) || ridenow.isBefore(datenows)) {
                    Toast.makeText(rideListActivity.this, "You can not start the ride yet. Please wait until the date of the pickup date.", Toast.LENGTH_SHORT).show();
                } else {

                    ongoingref.orderByChild("driver_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot snapshots : snapshot.getChildren()) {
                                    //UserHelperClass event = snapshots.getValue(UserHelperClass.class);
                                    String key = FirebaseDatabase.getInstance().getReference("ongoing_data").push().getKey().toString();
                                    bookedDriversClas bookedDriversClas = snapshots.getValue(bookedDriversClas.class);
                                    // String car_id,int count_) {
                                    String driverids = bookedDriversClas.getDriver_id();
                                    String ids = bookedDriversClas.getId();
                                    String driverevents = bookedDriversClas.getDriver_event(); //nama temppat
                                    String names = bookedDriversClas.getName();
                                    String passengerids = bookedDriversClas.getPassenger_id();
                                    String eventids = bookedDriversClas.getEvent_id();
                                    String Phone = bookedDriversClas.getPhone();
                                    String keydrivers = bookedDriversClas.getKey_driver();
                                    String carids = bookedDriversClas.getCar_id();
                                    int counts = num_pass;
                                    String driverkey = bookedDriversClas.getBooked_data_key();

                                    //ongoingclass bookeddriver = new ongoingclass(driverids, ids, driverevents, names, passengerids, Phone, eventids, keydrivers, carids, counts, key, driverkey);

                                    if (num_pass == 6) {
                                        //requestref.child(key).setValue(bookeddriver);
                                        //Toast.makeText(rideListActivity.this, "Successfully sent the information!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(rideListActivity.this, ChooseLocation.class);
                                        intent.putExtra("ID", keydrivers);
                                        intent.putExtra("driverkeyid", driverkeyid);
                                        intent.putExtra("ids", ids);
                                        intent.putExtra("num_pass", num_pass);
                                        intent.putExtra("driverskey", driverkey);
                                        intent.putExtra("Location", Location);
                                        //intent.putExtra("userID", model.getPassenger_id());
                                        startActivity(intent);
                                    } else if (num_pass == 5) {
                                        //requestref.child(key).setValue(bookeddriver);
                                        //Toast.makeText(rideListActivity.this, "Successfully sent the information!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(rideListActivity.this, chooselocation5.class);
                                        intent.putExtra("ID", keydrivers);
                                        intent.putExtra("driverkeyid", driverkeyid);
                                        intent.putExtra("ids", ids);
                                        intent.putExtra("num_pass", num_pass);
                                        intent.putExtra("Location", Location);

                                        intent.putExtra("driverskey", driverkey);
                                        //intent.putExtra("userID", model.getPassenger_id());

                                        startActivity(intent);
                                    } else if (num_pass == 4) {
//requestref.child(key).setValue(bookeddriver);
                                        //Toast.makeText(rideListActivity.this, "Successfully sent the information!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(rideListActivity.this, chooseLocation4.class);
                                        intent.putExtra("ID", keydrivers);
                                        intent.putExtra("driverkeyid", driverkeyid);
                                        intent.putExtra("ids", ids);
                                        intent.putExtra("num_pass", num_pass);
                                        intent.putExtra("Location", Location);
                                        intent.putExtra("driverskey", driverkey);
                                        //intent.putExtra("userID", model.getPassenger_id());

                                        startActivity(intent);
                                    } else if (num_pass == 3) {
//requestref.child(key).setValue(bookeddriver);
                                        //Toast.makeText(rideListActivity.this, "Successfully sent the information!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(rideListActivity.this, ChooseLocation3.class);
                                        intent.putExtra("ID", keydrivers);
                                        intent.putExtra("driverkeyid", driverkeyid);
                                        intent.putExtra("ids", ids);
                                        intent.putExtra("num_pass", num_pass);
                                        intent.putExtra("Location", Location);
                                        intent.putExtra("driverskey", driverkey);
                                        //intent.putExtra("userID", model.getPassenger_id());

                                        startActivity(intent);
                                    } else if (num_pass == 2) {
//requestref.child(key).setValue(bookeddriver);
                                        //Toast.makeText(rideListActivity.this, "Successfully sent the information!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(rideListActivity.this, chooseLocation2.class);
                                        intent.putExtra("ID", keydrivers);
                                        intent.putExtra("driverkeyid", driverkeyid);
                                        intent.putExtra("ids", ids);
                                        intent.putExtra("num_pass", num_pass);
                                        intent.putExtra("Location", Location);
                                        intent.putExtra("driverskey", driverkey);
                                        //intent.putExtra("userID", model.getPassenger_id());

                                        startActivity(intent);
                                    } else if (num_pass == 1) {
                                        //requestref.child(key).setValue(bookeddriver);
                                        //Toast.makeText(rideListActivity.this, "Successfully sent the information!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(rideListActivity.this, chooseLocation1.class);
                                        intent.putExtra("ID", keydrivers);
                                        intent.putExtra("driverkeyid", driverkeyid);
                                        intent.putExtra("ids", ids);
                                        intent.putExtra("num_pass", num_pass);
                                        intent.putExtra("Location", Location);
                                        intent.putExtra("driverskey", driverkey);
                                        //intent.putExtra("userID", model.getPassenger_id());

                                        startActivity(intent);
                                    } else if (num_pass == 0) {
                                        //requestref.child(key).setValue(bookeddriver);
                                        Toast.makeText(rideListActivity.this, "You can not start the journey without a passenger.", Toast.LENGTH_SHORT).show();

                                    }

                                }


                            } else {
                                //Toast.makeText(rideListActivity.this, "error.getMessage()", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("Cancelling the carpooling session.")
                        .setMessage("Are you sure to cancel your ride?")
                        .setCancelable(true)
                        .setPositiveButton("Cancel the Ride", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Query driverdata = FirebaseDatabase.getInstance().getReference("drivers_data").orderByChild("driver_id").equalTo(keydriver);
                                driverdata.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            child.getRef().removeValue();
                                            userref2.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("drivernum").setValue(ServerValue.increment(-1));

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });

                                Query bookeddata2 = FirebaseDatabase.getInstance().getReference("booked_drivers").orderByChild("key_driver").equalTo(keydriver);
                                bookeddata2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            child.getRef().removeValue();
                                            String passengerid = child.child("passenger_id").getValue(String.class);
                                            userref2.child(passengerid).child("passengernum").setValue(ServerValue.increment(-1));


                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // Handle the error
                                    }
                                });
                                Intent intent = new Intent(rideListActivity.this, HomeScreen.class);

                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Don't Cancel.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
            }
        });
    }

    private void LoadData() {
        options = new FirebaseRecyclerOptions.Builder<bookedDriversClas>().setQuery(passengerdata.orderByChild("key_driver").equalTo(driverkeyid), bookedDriversClas.class).build();
        adapter = new FirebaseRecyclerAdapter<bookedDriversClas, rideListActivityHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull rideListActivityHolder holder, int position, @NonNull bookedDriversClas model) {
                holder.text1.setText(model.getName());
                num_pass = adapter.getItemCount();

                if (num_pass == 0) {
                    num_pass = 0;
                }

                refdriv2.child(model.getKey_driver()).child("count").setValue(num_pass);

                /*refdriv.child(model.getKey_driver()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            refdriv.child(model.getKey_driver()).child("count").setValue(num_pass);
                            //Toast.makeText(rideListActivity.this,num_pass, Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });*/

                holder.messpic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (model.getPassenger_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            Toast.makeText(rideListActivity.this, "You can not chat yourself", Toast.LENGTH_SHORT).show();

                        } else {
                            Intent intent = new Intent(rideListActivity.this, reportUserActivity.class);
                            intent.putExtra("userID", model.getPassenger_id());

                            startActivity(intent);
                        }
                    }
                });
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

                locationref.orderByChild("passenger_id").equalTo(model.getPassenger_id()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshots : snapshot.getChildren()) {
                                String loc = (String) snapshots.child("driver_event").getValue();
                                holder.text5.setText(loc);
                            }
                        } else {
                            //Toast.makeText(rideListActivity.this, "error.getMessage()", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(rideListActivity.this, "You can not chat yourself", Toast.LENGTH_SHORT).show();

                        } else {
                            Intent intent = new Intent(rideListActivity.this, Chat.class);
                            intent.putExtra("userID", model.getPassenger_id());
                            intent.putExtra("name", model.getName());
                            intent.putExtra("imageuri", pic);
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
            public rideListActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_driverride, parent, false);
                return new rideListActivityHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}