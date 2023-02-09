package com.example.newjumshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class booked_driver extends AppCompatActivity {

    TextView driver_name, driver_gender, event1, driver_car, driver_platenumber, driver_phone, car_capacity, time, max, date;
    ImageView profilepic;
    Button confirm_button;
    DatabaseReference ref, carRef;
    AlertDialog.Builder builder;
    String driver_uid, driver_event, driverkey_id, car_id, carkeys, dates;
    LocalDate datenows;
    DateTimeFormatter formatters;
    LocalDate ridenow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_profile_picture);

        driver_name = findViewById(R.id.drivername);
        driver_gender = findViewById(R.id.drivergender);
        max = findViewById(R.id.maxcapacity);
        event1 = findViewById(R.id.event);
        driver_car = findViewById(R.id.drivercar);
        date = findViewById(R.id.date);
        driver_platenumber = findViewById(R.id.driverplatenumber);
        driver_phone = findViewById(R.id.driverphone);
        car_capacity = findViewById(R.id.carcapacity);
        time = findViewById(R.id.time);
        profilepic = findViewById(R.id.profile_image);
        confirm_button = findViewById(R.id.button_book);
        ref = FirebaseDatabase.getInstance().getReference().child("drivers_data");
        builder = new AlertDialog.Builder(this);
        String driverKey = getIntent().getStringExtra("driverKey");
        carkeys = getIntent().getStringExtra("carKey");
        carRef = FirebaseDatabase.getInstance().getReference().child("user_car");
        datenows = LocalDate.now();

        ref.child(driverKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String names = (String) snapshot.child("fullNames").getValue();
                String genders = (String) snapshot.child("gender").getValue();
                String events = (String) snapshot.child("event_name").getValue();
                //String car = (String) snapshot.child("car").getValue();
                String plate = (String) snapshot.child("plate_number").getValue();
                String phone = (String) snapshot.child("phoneNum").getValue();
                String times = (String) snapshot.child("time").getValue();
                String pic = (String) snapshot.child("pic").getValue();
                String uids = (String) snapshot.child("id").getValue();
                driverkey_id = (String) snapshot.child("driver_id").getValue();
                dates = (String) snapshot.child("date_data").getValue();
                car_id = (String) snapshot.child("car_id").getValue();
                String counts = String.valueOf(snapshot.child("count").getValue());

                car_capacity.setText(counts);
                driver_name.setText(names);
                driver_gender.setText(genders);
                event1.setText(events);
                //driver_car.setText(car);
                driver_platenumber.setText(plate);
                driver_phone.setText(phone);
                time.setText(times);
                driver_uid = uids;
                driver_event = events;
                date.setText(dates);
                Picasso.get().load(pic).into(profilepic);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        carRef.child(carkeys).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //for (DataSnapshot snapshots : snapshot.getChildren()) {
                    String capacity = (String) snapshot.child("num_passenger").getValue();
                    String car = (String) snapshot.child("cartype").getValue();
                    driver_car.setText(car);
                    max.setText(capacity);
                    //holder.textView4.setText(capacity);
                    //}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                ridenow = LocalDate.parse(dates, formatters);
                if (datenows.isAfter(ridenow)) {
                    Toast.makeText(booked_driver.this, "The carpooling date is overdue. You can not book the driver.", Toast.LENGTH_SHORT).show();
                } else {
                    builder.setTitle("Confirm!")
                            .setMessage("Do you want book this driver?")
                            .setCancelable(true)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(booked_driver.this, bookedDriverData.class);
                                    intent.putExtra("driverKey", driver_uid);
                                    intent.putExtra("lectevent", driver_event);
                                    intent.putExtra("driveridkey", driverkey_id);
                                    intent.putExtra("carid", car_id);
                                    startActivity(intent);
                                    //finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(booked_driver.this, View_Activity.class);
        //intent.putExtra("EVENTNAME", eventsname);
        startActivity(intent);
    }
}