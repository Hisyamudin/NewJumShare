package com.example.newjumshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class requestActivity extends AppCompatActivity {
    FirebaseRecyclerOptions<bookeddriver> options;
    FirebaseRecyclerAdapter<bookeddriver,requestListHolder> adapter;
    RecyclerView recyclerView;
    DatabaseReference requestreference, dateref,userdata,reference,delref,reference2,userdata2,driverdata, userref2, test;
    String data_date, currentUid,picture,data_gender,phone_data,names,dest,date,pickuptime;
    //test = FirebaseDatabase.getInstance().getReference("users");
    AlertDialog.Builder builder;
    FirebaseDatabase rootNode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        builder = new AlertDialog.Builder(this);
        recyclerView=findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        requestreference = FirebaseDatabase.getInstance().getReference("request_booking");
        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("booked_drivers");
        reference2 = rootNode.getReference("ongoing ride");
        driverdata = rootNode.getReference("drivers_data");
        userref2 = FirebaseDatabase.getInstance().getReference("users");
        test = FirebaseDatabase.getInstance().getReference("drivers_data");

        LoadData();
    }

    private void LoadData() {
        options = new FirebaseRecyclerOptions.Builder<bookeddriver>().setQuery(requestreference.orderByChild("driver_id").equalTo(currentUid),bookeddriver.class).build();
        adapter = new FirebaseRecyclerAdapter<bookeddriver, requestListHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull requestListHolder holder, int position, @NonNull bookeddriver model) {
                test = FirebaseDatabase.getInstance().getReference("drivers_data");
                dateref = FirebaseDatabase.getInstance().getReference("lectevent");
                userdata = FirebaseDatabase.getInstance().getReference("users");
                userdata2= FirebaseDatabase.getInstance().getReference().child("users");
                dateref.orderByChild("Event_name").equalTo(model.getEvent_id()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshots : snapshot.getChildren()) {
                                data_date = (String) snapshots.child("Start_date").getValue();
                                holder.text3.setText(data_date);
                                dest = (String) snapshots.child("Event_location").getValue();
                            }
                        } else {
                            //Toast.makeText(requestActivity.this, "error.getMessage()", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                driverdata.orderByChild("driver_id").equalTo(model.getKey_driver()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshots : snapshot.getChildren()) {
                                date = (String) snapshots.child("date_data").getValue();
                                pickuptime = (String) snapshots.child("time").getValue();

                            }
                        } else {
                            //Toast.makeText(requestActivity.this, "error.getMessage()", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                userdata.orderByChild("uid").equalTo(model.getPassenger_id()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshots : snapshot.getChildren()) {
                                picture = (String) snapshots.child("pic").getValue();
                                data_gender=(String) snapshots.child("gender").getValue();
                                phone_data=(String) snapshots.child("phoneNo").getValue();

                                Picasso.get().load(picture).into(holder.profile_pic);
                                holder.text5.setText(data_gender);
                            }
                        } else {
                            //Toast.makeText(requestActivity.this, "error.getMessage()", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                userdata.orderByChild("uid").equalTo(currentUid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshots : snapshot.getChildren()) {
                                names = (String) snapshots.child("name").getValue();

                            }
                        } else {
                            //Toast.makeText(requestActivity.this, "error.getMessage()", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //Picasso.get().load(picture).into(holder.profile_pic);
                holder.text1.setText(model.getName());
                holder.text2.setText(model.getEvent_id());
                //holder.text3.setText(data_date);
                holder.text4.setText(model.getDriver_event());

                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String key = getRef(position).getKey();

                        builder.setTitle("Confirmation")
                                .setMessage("Do you want to accept "+model.getName()+" to ride with you?")
                                .setCancelable(true)
                                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        String empty= "";
                                        boolean test1 =false;
                                        double lats=0;
                                        int count=0;

                                        String keys =FirebaseDatabase.getInstance().getReference("booked_drivers").push().getKey();

                                        Ongoingrideclass ongoingrideclass = new Ongoingrideclass(model.getPassenger_id(), model.getKey_driver(), model.getCar_id(), model.getEvent_id(), empty, count, model.getDriver_id(), empty, empty, empty, empty, empty, empty, empty, empty, empty, empty, empty, lats, lats, test1);

                                        reference2.child(model.getPassenger_id()).setValue(ongoingrideclass);

                                        bookedDriversClas bookedDriversClas = new bookedDriversClas(model.getDriver_id(), model.getId(), model.getDriver_event(), model.getName(), model.getPassenger_id(),phone_data , model.getEvent_id(),model.getKey_driver(),model.getCar_id(),model.getCount(),key,model.getLatitude(),model.getLongitude(),dest, names,pickuptime,date);

                                        reference.child(model.getId()).setValue(bookedDriversClas);

                                        userref2.child(model.getDriver_id()).child("pending").setValue(ServerValue.increment(-1));
                                        userref2.child(model.getPassenger_id()).child("passengernum").setValue(ServerValue.increment(1));
                                        test.child(model.getKey_driver()).child("count").setValue(ServerValue.increment(1));


                                        delref=FirebaseDatabase.getInstance().getReference().child("request_booking").child(key);
                                        delref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                startActivity(new Intent(getApplicationContext(),HomeScreen.class));
                                                Toast.makeText(requestActivity.this, "Successful Accept the Passenger!", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        userref2.child(model.getDriver_id()).child("pending").setValue(ServerValue.increment(-1));

                                        delref=FirebaseDatabase.getInstance().getReference().child("request_booking").child(key);
                                        delref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                startActivity(new Intent(getApplicationContext(),HomeScreen.class));
                                            }
                                        });
                                    }
                                }).show();

                    }
                });

            }

            @NonNull
            @Override
            public requestListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_request_list,parent,false);
                return new requestListHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
}