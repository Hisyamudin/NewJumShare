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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class View_Activity extends AppCompatActivity {

    TextView textView, textDate, textEndDate, startTimes, endTimes, eventLocation, Phone, descr;
    Button btnadd;
    DatabaseReference ref, DataRef, test, carRef, userref,ref2;
    String eventsname, lectevent,startdate,enddate,starttime, endtime;
    int testr,capacity = 0;
    private List<event_driver> driverlist;

    RecyclerView recyclerView;
    FirebaseRecyclerOptions<event_driver> options;
    FirebaseRecyclerAdapter<event_driver, MyViewHolder2> adapter2;

    private RequestQueue mRequestQue;
    private String postUrl = "https://fcm.googleapis.com/fcm/send";
    private final String fcmServerKey= "AAAAWnQttHE:APA91bHrKD0ekziB9ZRG9MvIPU4hDuqVu-tOaWNxBxYevX_ruIS_qbIDelFFCi6-vYTPadhNBsKHYJzZCuMiPj2Wr3s0vKcOluZgkZORG8P9x5l0bwrxFUk0GX1_k5axR4J8uv4CSgvj";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        //childCount = 0;
        textView = findViewById(R.id.text1);
        textDate = findViewById(R.id.textDate);
        textEndDate = findViewById(R.id.textEndDate);
        startTimes = findViewById(R.id.startTimes);
        endTimes = findViewById(R.id.endTimes);
        eventLocation = findViewById(R.id.eventLocation);
        Phone = findViewById(R.id.Phone);
        descr = findViewById(R.id.desc);
        btnadd = findViewById(R.id.btnadd);
        ref = FirebaseDatabase.getInstance().getReference().child("lectevent");
        ref2 = FirebaseDatabase.getInstance().getReference("lectevent");
        DataRef = FirebaseDatabase.getInstance().getReference().child("drivers_data");
        carRef = FirebaseDatabase.getInstance().getReference().child("user_car");
        userref = FirebaseDatabase.getInstance().getReference().child("users");
        lectevent = getIntent().getStringExtra("lectevent");
        recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        mRequestQue = Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        test = FirebaseDatabase.getInstance().getReference("drivers_data");

        ref.child(lectevent).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String eventname = (String) snapshot.child("Event_name").getValue();
                    eventsname = (String) snapshot.child("Event_name").getValue();
                    startdate = (String) snapshot.child("Start_date").getValue();
                    enddate = (String) snapshot.child("End_date").getValue();
                    starttime = (String) snapshot.child("Start_time").getValue();
                    endtime = (String) snapshot.child("End_time").getValue();
                    String fon = (String) snapshot.child("Phone").getValue();
                    String desc = (String) snapshot.child("Event_description").getValue();
                    String loc = (String) snapshot.child("Event_location").getValue();
                    //ref.child(lectevent).child("count").setValue(childCount);
                    textView.setText(eventname);
                    textDate.setText(startdate);
                    textEndDate.setText(enddate);
                    startTimes.setText(starttime);
                    endTimes.setText(endtime);
                    eventLocation.setText(fon);
                    Phone.setText(fon);
                    descr.setText(desc);
                    eventLocation.setText(loc);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LoadData();

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(View_Activity.this, addDriver.class);
                intent.putExtra("EVENTNAME", eventsname);
                intent.putExtra("startdate", startdate);
                intent.putExtra("enddate", enddate);
                intent.putExtra("starttime", starttime);
                intent.putExtra("endtime", endtime);
                startActivity(intent);

            }
        });

    }


    private void LoadData() {
        options = new FirebaseRecyclerOptions.Builder<event_driver>().setQuery(test.orderByChild("event_name").equalTo(lectevent), event_driver.class).build();
        adapter2 = new FirebaseRecyclerAdapter<event_driver, MyViewHolder2>(options) {
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

                            String capacities = snapshot.child("num_passenger").getValue().toString();
                            capacity =  Integer.parseInt(capacities);
                            holder.textView4.setText(capacities);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                userref.orderByChild("name").equalTo(model.getFullNames()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                double rate = snapshot1.child("rating").getValue(double.class);

                                holder.getTextView5.setText(Double.toString(rate));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //}
                Picasso.get().load(model.getPic()).into(holder.imageView);
                testr=0;
                testr = adapter2.getItemCount();
                //Toast.makeText(View_Activity.this, testr+"", Toast.LENGTH_SHORT).show();

                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            Toast.makeText(View_Activity.this, " You can not book yourself", Toast.LENGTH_SHORT).show();

                        }
                        else if (model.getCount() == capacity){
                            Toast.makeText(View_Activity.this, "The ride is full", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            sendNotification();
                            Intent intent = new Intent(View_Activity.this, booked_driver.class);
                            intent.putExtra("driverKey", getRef(position).getKey());

                            intent.putExtra("carKey", model.getCar_id());
                            startActivity(intent);
                        }
                    }

                });

                ref2.child(lectevent).child("count").setValue(testr);
                //.makeText(View_Activity.this, testr+" adaaa", Toast.LENGTH_SHORT).show();

                if (testr == 0){
                    ref2.child(lectevent).child("count").setValue(0);
                }

                /*ref.child(lectevent).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            ref.child(lectevent).child("count").setValue(testr);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });*/

            }

            @NonNull
            @Override
            public MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_single_view, parent, false);
                return new MyViewHolder2(v);
            }
        };
        adapter2.startListening();
        adapter2.notifyDataSetChanged();
        recyclerView.setAdapter(adapter2);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(View_Activity.this, HomeScreen.class);
        //intent.putExtra("EVENTNAME", eventsname);
        startActivity(intent);
    }

    private void sendNotification() {

       /* JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", "/topics/"+"news");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", "any title");
            notificationObj.put("body", "any body");
            mainObj.put("notification", notificationObj);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    mainObj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    //return super.getHeaders();
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","AAAAWnQttHE:APA91bHrKD0ekziB9ZRG9MvIPU4hDuqVu-tOaWNxBxYevX_ruIS_qbIDelFFCi6-vYTPadhNBsKHYJzZCuMiPj2Wr3s0vKcOluZgkZORG8P9x5l0bwrxFUk0GX1_k5axR4J8uv4CSgvj");
                    return header;
                }
            };

            mRequestQue.add(request);

        }catch (JSONException e){
            e.printStackTrace();
        }*/

    }


}