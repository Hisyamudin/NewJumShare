package com.example.newjumshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class chooseLocation1 extends AppCompatActivity {

    String[] location = {"Fakulti Kejuruteraan FKJ", "Fakulti Komputeran dan Informatik (FKI)", "UMS ODEC beach", "Kolej TUN MUSTAPHA UMS, Kota Kinabalu, Sabah", "Bustop Kolej Kediaman Tun Fuad (CD)", "Kolej Kediaman E (Kampung E) Universiti Malaysia Sabah", "Padang Kawad UMS", "Perpustakaan Universiti Malaysia Sabah", "1 Borneo Mall", "Dewan Kuliah Pusat (DKP Baru) - UMS", "DKP Lama / Dewan Kuliah Pusat I", "Fakulti Psikologi dan Pendidikan", "Fakulti Kemanusian, Seni dan Warisan (FKSW)", "Fakulti Sains Dan Sumber Alam", "Faculty of Business, Economics and Accountancy", "Fakulti Perubatan dan Sains Kesihatan (FPSK)", "Fakulti Sains Makanan Dan Pemakanan", "Anjung Siswa, UMS", "Pusat Rawatan Warga UMS", "Kompleks Sukan UMS", "Pusat Penataran Ilmu dan Bahasa", "Masjid Universiti Malaysia Sabah", "Bahagian Keselamatan Universiti Malaysia Sabah"};
    String keydrivers, driverkeyid, ids, driverkey, item1, Location;
    AutoCompleteTextView location1;
    ArrayAdapter<String> adapterItems;
    RecyclerView recyclerview;
    FirebaseRecyclerOptions<bookedDriversClas> options;
    FirebaseRecyclerAdapter<bookedDriversClas, chooselocationActivityHolder> adapter;
    DatabaseReference ongoingref, ridestartref, driverref;
    int num_pass;
    Button btn_start;
    private FusedLocationProviderClient mLocationClient;
    private android.location.Location locations;
    ArrayList<String> dataArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location1);
        dataArray = new ArrayList<>();
        keydrivers = getIntent().getStringExtra("ID");
        driverkeyid = getIntent().getStringExtra("driverkeyid");
        ids = getIntent().getStringExtra("ids");
        driverref = FirebaseDatabase.getInstance().getReference("booked_drivers");
        Location = getIntent().getStringExtra("Location");
        driverkey = getIntent().getStringExtra("driverskey");
        ongoingref = FirebaseDatabase.getInstance().getReference("booked_drivers");
        ridestartref = FirebaseDatabase.getInstance().getReference("ongoing ride");
        num_pass = getIntent().getIntExtra("num_pass", 0);
        recyclerview = findViewById(R.id.recyclerView);
        recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerview.setHasFixedSize(true);
        location1 = findViewById(R.id.location1);
        btn_start = findViewById(R.id.buttonstart);

        mLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //Toast.makeText(chooseLocation2.this, keydrivers, Toast.LENGTH_SHORT).show();

        LoadData();
        adapterItems = new ArrayAdapter<String>(this, R.layout.gender_list_item, dataArray);
        location1.setAdapter(adapterItems);
        location1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item1 = adapterView.getItemAtPosition(i).toString();
                //Toast.makeText(ChooseLocation3.this, "Item: " + item, Toast.LENGTH_SHORT);
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //        testRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").setValue(name.getEditText().getText().toString());
                //        testRef = FirebaseDatabase.getInstance().getReference("users");

                driverref.orderByChild("key_driver").equalTo(keydrivers).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshots : snapshot.getChildren()) {
                                bookedDriversClas event = snapshots.getValue(bookedDriversClas.class);
                                String empty = "";
                                ridestartref.child(event.getPassenger_id()).child("location1").setValue(item1);
                                ridestartref.child(event.getPassenger_id()).child("destination").setValue(Location);
                                ridestartref.child(event.getPassenger_id()).child("count").setValue(num_pass);
                                ridestartref.child(event.getPassenger_id()).child("test").setValue(true);

                                mLocationClient.getLastLocation().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        locations = task.getResult();
                                        ridestartref.child(event.getPassenger_id()).child("currentlat").setValue(locations.getLatitude());
                                        ridestartref.child(event.getPassenger_id()).child("currentlng").setValue(locations.getLongitude());
                                    }

                                });

                            }
                        } else {
                            //Toast.makeText(HomeScreen.this, "", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //ridestartref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").setValue(name.getEditText().getText().toString());

                Intent intent = new Intent(chooseLocation1.this, Maps3.class);
                intent.putExtra("ID", keydrivers);
                intent.putExtra("driverkeyid", driverkeyid);
                intent.putExtra("ids", ids);
                intent.putExtra("num_pass", num_pass);
                intent.putExtra("destination1", item1);
                intent.putExtra("Location", Location);
                intent.putExtra("driverskey", driverkey);
                //intent.putExtra("userID", model.getPassenger_id());

                startActivity(intent);
            }
        });


    }

    private void LoadData() {

        options = new FirebaseRecyclerOptions.Builder<bookedDriversClas>().setQuery(ongoingref.orderByChild("key_driver").equalTo(keydrivers), bookedDriversClas.class).build();
        adapter = new FirebaseRecyclerAdapter<bookedDriversClas, chooselocationActivityHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull chooselocationActivityHolder holder, int position, @NonNull bookedDriversClas model) {
                holder.name.setText(model.getName());
                holder.location.setText(model.getDriver_event());
                dataArray.add(model.getDriver_event());
            }

            @NonNull
            @Override
            public chooselocationActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_choose_location, parent, false);
                return new chooselocationActivityHolder(v);
            }
        };
        adapter.startListening();
        recyclerview.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(chooseLocation1.this, showDriver.class);
        startActivity(intent);

    }
}
