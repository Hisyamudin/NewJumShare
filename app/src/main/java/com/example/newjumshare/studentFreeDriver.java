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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class studentFreeDriver extends AppCompatActivity {
    TextView driver_name, driver_gender, locations, driver_car, driver_platenumber, driver_phone, car_capacity, time, max;
    ImageView profilepic;
    Button confirm_button;
    DatabaseReference ref, carRef;
    AlertDialog.Builder builder;
    String driver_uid, driver_event, driverkey_id, car_id,carkeys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_free_driver);

        driver_name = findViewById(R.id.drivername);
        driver_gender = findViewById(R.id.drivergender);
        max = findViewById(R.id.maxcapacity);
        locations = findViewById(R.id.location);
        driver_car = findViewById(R.id.drivercar);
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



        ref.child(driverKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String names = snapshot.child("fullNames").getValue().toString();
                String genders = snapshot.child("gender").getValue().toString();
                //String events = snapshot.child("event_name").getValue().toString();
                //String car = (String) snapshot.child("car").getValue();
                String plate = snapshot.child("plate_number").getValue().toString();
                String location = snapshot.child("location").getValue().toString();
                String phone = snapshot.child("phoneNum").getValue().toString();
                String times = snapshot.child("time").getValue().toString();
                String pic = (String) snapshot.child("pic").getValue();
                String uids = snapshot.child("id").getValue().toString();
                driverkey_id = snapshot.child("driver_id").getValue().toString();
                car_id = snapshot.child("car_id").getValue().toString();
                String counts = snapshot.child("count").getValue().toString();

                car_capacity.setText(counts);
                driver_name.setText(names);
                driver_gender.setText(genders);
                //event1.setText(events);
                locations.setText(location);
                //driver_car.setText(car);
                driver_platenumber.setText(plate);
                driver_phone.setText(phone);
                time.setText(times);
                driver_uid = uids;
                //driver_event = events;
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
                builder.setTitle("Confirm!")
                        .setMessage("Do you want book this driver?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(studentFreeDriver.this, bookedDriverData.class);
                                intent.putExtra("driverKey", driver_uid);
                                //intent.putExtra("lectevent", driver_event);
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
        });

    }
}