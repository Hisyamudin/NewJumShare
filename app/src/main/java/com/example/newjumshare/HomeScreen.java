package com.example.newjumshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //variables
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FirebaseUser currentUser;
    private TextView name, phonenum, pendings, driverlog,passengerlog,tsession,tsessiond;
    //TextView name, phonenum
    DatabaseReference userRef;
    DatabaseReference DataRef;
    DatabaseReference ongoingref;
    DatabaseReference driverref;
    DatabaseReference dataref2;
    DatabaseReference carref;
    DatabaseReference eventref,locationref,refe;
    Query DataRef2, driversdataref, driversdataref2, userdateref,passengerdateref,query1;
    RecyclerView recyclerView, recyclerView2, recyclerView3, recyclerView4, recyclerView5, recyclerviewpickuplocation;
    FirebaseRecyclerOptions<Adapter> options, options2, option5;
    FirebaseRecyclerOptions<Ongoingrideclass> options4;
    FirebaseRecyclerOptions<pickuplocationclass> options6;
    FirebaseRecyclerAdapter<Adapter, MyViewHolder> adapter, adapter2;
    FirebaseRecyclerAdapter<Ongoingrideclass, MyViewHolder3> adapter4;
    FirebaseRecyclerAdapter<Adapter, MyViewHolder4> adapter5;
    FirebaseRecyclerAdapter<pickuplocationclass, pickuplocationviewholder> adapter6;
    ArrayList<MainModel> mainModels;
    MainAdapter mainAdapter;
    FirebaseAuth mAuth;
    ImageView ProfilePicture;
    StorageReference storageReference;
    String profilepic, keydriver,event_pic,uid;
    AlertDialog.Builder builder;
    MainAdapter.RecyclerViewClickListener listener;
    int reportnumz;
    LocalDate datetoday;
    TextView noevent,ongoingride;
    LinearLayout pendinglayout,activedriver,activepassenger, availabilitylayout;
    DateTimeFormatter formatters;
    LocalDate ridenow, datenows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        mAuth = FirebaseAuth.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView2 = findViewById(R.id.recyclerView2);
        //recyclerView3 = findViewById(R.id.recyclerView3);
        recyclerView4 = findViewById(R.id.recyclerView4);
        recyclerView5 = findViewById(R.id.recyclerViewtoday);
        recyclerviewpickuplocation = findViewById(R.id.recyclerViewlocation);
        builder = new AlertDialog.Builder(this);
        datetoday= LocalDate.now();
        noevent = findViewById(R.id.noevent);
        pendings = findViewById(R.id.pendingz);
        ongoingride = findViewById(R.id.ongoingride);
        driverlog = findViewById(R.id.driverlog);
        passengerlog= findViewById(R.id.passengerlog);
        tsession = findViewById(R.id.tsession);
        tsessiond = findViewById(R.id.tsessiond);
        pendinglayout = findViewById(R.id.request);
        availabilitylayout = findViewById(R.id.test3);
        activedriver = findViewById(R.id.activedriver);
        activepassenger = findViewById(R.id.activepassenger);
        Instant instant = datetoday.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        long timeInMilliseconds = instant.toEpochMilli();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(linearLayoutManager.HORIZONTAL);

        //recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);

//        recyclerView3.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
//        recyclerView3.setHasFixedSize(true);

        recyclerView4.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView4.setHasFixedSize(true);

        recyclerView5.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView5.setHasFixedSize(true);

        recyclerviewpickuplocation.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerviewpickuplocation.setHasFixedSize(true);

        Integer[] eventpic = {R.drawable.shoppingicon, R.drawable.bookicon};
        String[] stuevent = {"Groceries Trip", "Study"};

        DataRef = FirebaseDatabase.getInstance().getReference().child("lectevent");
        ongoingref = FirebaseDatabase.getInstance().getReference("ongoing ride");
        locationref = FirebaseDatabase.getInstance().getReference().child("location_data");
        String datee = datetoday.toString();
        DataRef2 = FirebaseDatabase.getInstance().getReference("lectevent").orderByChild("Start_date").equalTo(datee);
        driversdataref = FirebaseDatabase.getInstance().getReference("drivers_data").orderByChild("date_data").equalTo(datee);
        driversdataref2 = FirebaseDatabase.getInstance().getReference("booked_drivers").orderByChild("driverdate").equalTo(datee);
        userdateref = FirebaseDatabase.getInstance().getReference("drivers_data").orderByChild("id").equalTo(uid);
        passengerdateref = FirebaseDatabase.getInstance().getReference("booked_drivers").orderByChild("passenger_id").equalTo(uid);
        //Query query = DataRef.orderByChild("Start_date").startAt(datee);
        //DataRef.addValueEventListener(new ValueEventListener() {
        DataRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    noevent.setText("Today Event");
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        Adapter event = snapshots.getValue(Adapter.class);
                        String enddate1 = event.getEnd_date();
                        LoadDatatoday(datee, enddate1);
                    }
                } else {
                    noevent.setText("No Today Event");
                    //Toast.makeText(HomeScreen.this, "No today event", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userdateref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                datenows = LocalDate.now();
                if (snapshot.exists()) {
                    tsession.setText("Unavailable");
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        driverClass event = snapshots.getValue(driverClass.class);
                        String ridedate = event.getDate_data();
                        formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        ridenow = LocalDate.parse(ridedate, formatters);

//                        if (ridenow.isAfter(datenows) || ridenow.isBefore(datenows) ){
//                            tsession.setText("Unavailable");
//
//                        }
                        if (ridenow.isEqual(datenows)) {
                            tsession.setText("Available");
//                            if(ridenow.isBefore(datenows)||ridenow.isAfter(datenows)) {
//                                tsession.setText("Unavailable");
//                                //Toast.makeText(HomeScreen.this, "No today event", Toast.LENGTH_SHORT).show();
//                            }
                        }

                    }

                } else {
                    tsession.setText("Unavailable");
                    //Toast.makeText(HomeScreen.this, "No today event", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        passengerdateref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                datenows = LocalDate.now();
                if (snapshot.exists()) {
                    tsessiond.setText("Unavailable");
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        bookedDriversClas event = snapshots.getValue(bookedDriversClas.class);
                        String ridedate = event.getDriverdate();
                        formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        ridenow = LocalDate.parse(ridedate, formatters);

                        if (ridenow.isEqual(datenows)) {
                            tsessiond.setText("Available");

                        }

                    }

                } else {
                    tsessiond.setText("Unavailable");
                    //Toast.makeText(HomeScreen.this, "No today event", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tsession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, showDriver.class);
                startActivity(intent);
            }
        });

        pendinglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, requestActivity.class);
                startActivity(intent);
            }
        });

        availabilitylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, showDriver.class);
                startActivity(intent);
            }
        });

        activepassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, showPassenger.class);
                startActivity(intent);
            }
        });

        activedriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, showDriver.class);
                startActivity(intent);
            }
        });


        LoadData();
        //LoadData2();

        driverref = FirebaseDatabase.getInstance().getReference("booked_drivers");

        driverref.orderByChild("passenger_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        bookedDriversClas event = snapshots.getValue(bookedDriversClas.class);
                        keydriver = event.getKey_driver();
                        LoadData3(keydriver);
                        ongoingride.setText("Accepted Ride");
                    }
                } else {
                    ongoingride.setText("No Accepted Ride");                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        //hide or show items

        /*Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_logout).setVisible(false);*/

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);

        //Initialize ArrayList
        mainModels = new ArrayList<>();
        for (int i = 0; i < eventpic.length; i++) {
            MainModel model = new MainModel(eventpic[i], stuevent[i]);
            mainModels.add(model);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(HomeScreen.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());

        setOnClickListener();
        mainAdapter = new MainAdapter(HomeScreen.this, mainModels, listener);
        recyclerView2.setAdapter(mainAdapter);

        //View headerview = navigationView.getHeaderView(0);
        //name = (TextView) headerview.findViewById(R.id.name);
        //phonenum = (TextView) headerview.findViewById(R.id.phoneNo);

        //name.setText(UserHelperClass.buildWelcomeMessage());

        name = navigationView.getHeaderView(0).findViewById(R.id.name);
        phonenum = navigationView.getHeaderView(0).findViewById(R.id.phoneNumber);
        ProfilePicture = navigationView.getHeaderView(0).findViewById(R.id.profilePic);


        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.child("name").getValue().toString());
                phonenum.setText(snapshot.child("email").getValue().toString());
                pendings.setText(snapshot.child("pending").getValue().toString());
                driverlog.setText(snapshot.child("drivernum").getValue().toString());
                passengerlog.setText(snapshot.child("passengernum").getValue().toString());
                profilepic = snapshot.child("pic").getValue().toString();
                Picasso.get().load(profilepic).into(ProfilePicture);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(HomeScreen.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Loadpickuplocationdata();
    }

    private void LoadData2() {

        options2 = new FirebaseRecyclerOptions.Builder<Adapter>().setQuery(DataRef.orderByChild("type").equalTo("Student"), Adapter.class).build();
        adapter2 = new FirebaseRecyclerAdapter<Adapter, MyViewHolder>(options2) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull Adapter model) {
                holder.texts1.setText(model.getEvent_name());
                holder.texts2.setText(model.getStart_date());
                /*holder.texts3.setText(model.getEnd_date());
                holder.texts4.setText(model.getStart_time());
                holder.texts5.setText(model.getEnd_time());
                holder.texts6.setText(model.getEvent_location());
                holder.texts7.setText(model.getEvent_description());
                holder.texts8.setText(model.getPhone());*/
                //holder.text9.setText(model.getCount());
                holder.text9.setText(String.valueOf(model.getCount()));

                String end_date = model.getEnd_date();
                //SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

                //Date parsedend_date = sdf1.parse(end_date);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate enddate = LocalDate.parse(end_date,formatter);
                LocalDate datenow = LocalDate.now();


                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(enddate.isAfter(datenow)){
                            Toast.makeText(HomeScreen.this, "The event is has ended. Can not attend the event", Toast.LENGTH_SHORT).show();

                        }else{

                        Intent intent = new Intent(HomeScreen.this, View_Activity.class);
                        intent.putExtra("lectevent", getRef(position).getKey());
                        startActivity(intent);
                        }
                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_home, parent, false);

                return new MyViewHolder(v);
            }
        };
        adapter2.startListening();
        recyclerView3.setAdapter(adapter2);
    }

    private void setOnClickListener() {
        listener = new MainAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), studentEvent2.class);
                intent.putExtra("keys", mainModels.get(position).getStuevent());
                //intent.putExtra("keys",mainModels.get(position).getStuevent());
                startActivity(intent);
            }
        };
    }

    private void LoadData() {
        options = new FirebaseRecyclerOptions.Builder<Adapter>().setQuery(DataRef.orderByChild("type").equalTo("Lecture"), Adapter.class).build();
        adapter = new FirebaseRecyclerAdapter<Adapter, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull Adapter model) {
                holder.texts1.setText(model.getEvent_name());
                holder.texts2.setText(model.getStart_date());
                /*holder.texts3.setText(model.getEnd_date());
                holder.texts4.setText(model.getStart_time());
                holder.texts5.setText(model.getEnd_time());
                holder.texts6.setText(model.getEvent_location());
                holder.texts7.setText(model.getEvent_description());
                holder.texts8.setText(model.getPhone());*/
                //holder.text9.setText(model.getCount());
                holder.text9.setText(String.valueOf(model.getCount()));
                holder.text11.setText(String.valueOf(model.getEnd_date()));


                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String end_date = model.getEnd_date();
                        //SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

                        //Date parsedend_date = sdf1.parse(end_date);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate enddate = LocalDate.parse(end_date,formatter);
                        LocalDate datenow = LocalDate.now();
                        //datenow.isAfter(enddate)
                        if( datenow.isEqual(enddate) ||datenow.isBefore(enddate)){
                            Intent intent = new Intent(HomeScreen.this, View_Activity.class);
                            intent.putExtra("lectevent", getRef(position).getKey());
                            startActivity(intent);
                        }
                        else{

                            Toast.makeText(HomeScreen.this, "The event is has ended. Can not attend the event", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view, parent, false);

                return new MyViewHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void LoadData3(String keydriver) {

        options4 = new FirebaseRecyclerOptions.Builder<Ongoingrideclass>().setQuery(ongoingref.orderByChild("driverkey").equalTo(keydriver), Ongoingrideclass.class).build();
        adapter4 = new FirebaseRecyclerAdapter<Ongoingrideclass, MyViewHolder3>(options4) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder3 holder, @SuppressLint("RecyclerView") final int position, @NonNull Ongoingrideclass model) {

                /*texts1 = itemView.findViewById(R.id.text1);
                texts2 = itemView.findViewById(R.id.drivernamer);
                texts3 = itemView.findViewById(R.id.cartype);
                texts4 = itemView.findViewById(R.id.cartcolor);
                texts5 = itemView.findViewById(R.id.carplate);
                texts6 = itemView.findViewById(R.id.pickuplocation);
                texts7 = itemView.findViewById(R.id.destination);*/

                dataref2 = FirebaseDatabase.getInstance().getReference().child("users").child(model.getPassenger1());

                //holder.texts6.setText(model.get)
                dataref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String namez = snapshot.child("name").getValue().toString();

                        holder.texts2.setText(namez);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //Toast.makeText(HomeScreen.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                carref = FirebaseDatabase.getInstance().getReference().child("user_car").child(model.getCarkey());
                carref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String cartypes = (String) snapshot.child("cartype").getValue();
                        String cartcolors = (String) snapshot.child("carcolour").getValue();
                        String cartplates = (String) snapshot.child("carplate").getValue();

                        holder.texts3.setText(cartypes);
                        holder.texts4.setText(cartcolors);
                        holder.texts5.setText(cartplates);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //Toast.makeText(HomeScreen.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
//                DatabaseReference carref2;
//                carref2 = FirebaseDatabase.getInstance().getReference().child("user_car").child(model.getCarkey());
//                carref2.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        String cartypes = (String) snapshot.child("cartype").getValue();
//                        String cartcolors = (String) snapshot.child("carcolour").getValue();
//                        String cartplates = (String) snapshot.child("carplate").getValue();
//
//                        holder.texts3.setText(cartypes);
//                        holder.texts4.setText(cartcolors);
//                        holder.texts5.setText(cartplates);
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(HomeScreen.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });

//                Query bookeddata2 = FirebaseDatabase.getInstance().getReference("booked_drivers").orderByChild("passenger_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                bookeddata2.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//                            String passengerid = childSnapshot.child("key_driver").getValue(String.class);
//                            if (passengerid.equals(model.getDriverkey())){
//                                String passengerid = childSnapshot.child("key_driver").getValue(String.class);
//                            }
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        //Log.w("Data removed", "failure", databaseError.toException());
//                    }

//                refe = FirebaseDatabase.getInstance().getReference("booked_drivers");
//
//                query1 = FirebaseDatabase.getInstance().getReference("booked_drivers").orderByChild("driver_key").equalTo(model.getDriverkey());
//
//                query1.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                        String eventnames = snapshot.child("driver_event").getValue().toString();
//                        holder.texts6.setText(eventnames);
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        //Toast.makeText(HomeScreen.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });

                eventref = FirebaseDatabase.getInstance().getReference().child("lectevent").child(model.getEventkey());

                eventref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String eventnames = snapshot.child("Event_name").getValue().toString();
                        String dest = snapshot.child("Event_location").getValue().toString();

                        holder.texts1.setText(eventnames);
                        holder.texts7.setText(dest);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //Toast.makeText(HomeScreen.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                if(model.isTest() == true){
                    holder.texts8.setText("The ride is started!");

                    holder.v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(HomeScreen.this, mappassenger3.class);
                            intent.putExtra("ID", model.getDriverkey());
                            intent.putExtra("carkey", model.getCarkey());
                            intent.putExtra("passengerkey", model.getPassengerkey());
                            intent.putExtra("driveruserid", model.getPassenger1());
                            intent.putExtra("num_pass", model.getCount());
                            intent.putExtra("Location", model.getDestination());
                            intent.putExtra("destination1", model.getLocation1());
                            intent.putExtra("destination2", model.getLocation2());
                            intent.putExtra("destination3", model.getLocation3());
                            intent.putExtra("destination4", model.getLocation4());
                            intent.putExtra("destination5", model.getLocation5());
                            intent.putExtra("destination6", model.getLocation6());

                            startActivity(intent);
                        }
                    });
                }
                else{
                    holder.texts8.setText("The ride is not started");

                    holder.v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(HomeScreen.this, "Your Driver has not start the ride yet.", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
                /*holder.texts6.setText(model.getDriver_event());
                //holder.texts2.setText(model.getStart_date());
                holder.texts3.setText(model.getEnd_date());
                holder.texts4.setText(model.getStart_time());
                holder.texts5.setText(model.getEnd_time());
                holder.texts6.setText(model.getEvent_location());
                holder.texts7.setText(model.getEvent_description());
                holder.texts8.setText(model.getPhone());
                //holder.text9.setText(model.getCount());
                //holder.text9.setText(String.valueOf(model.getCount()));*/

            }

            @NonNull
            @Override
            public MyViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_home_ongoing, parent, false);

                return new MyViewHolder3(v);
            }
        };
        adapter4.startListening();
        recyclerView4.setAdapter(adapter4);
        adapter4.notifyDataSetChanged();
    }

    private void LoadDatatoday(String datetooday, String dateend) {
        option5 = new FirebaseRecyclerOptions.Builder<Adapter>().setQuery(DataRef.orderByChild("Start_date").equalTo(datetooday), Adapter.class).build();
        adapter5 = new FirebaseRecyclerAdapter<Adapter, MyViewHolder4>(option5) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder4 holder, @SuppressLint("RecyclerView") final int position, @NonNull Adapter model) {
                holder.texts1.setText(model.getEvent_name());
                holder.texts2.setText(model.getStart_date());
                /*holder.texts3.setText(model.getEnd_date());
                holder.texts4.setText(model.getStart_time());
                holder.texts5.setText(model.getEnd_time());
                holder.texts6.setText(model.getEvent_location());
                holder.texts7.setText(model.getEvent_description());
                holder.texts8.setText(model.getPhone());*/
                //holder.text9.setText(model.getCount());
                //holder.text9.setText(String.valueOf(model.getCount()));

                String num = Integer.toString(model.getCount());
                holder.texts3.setText(num);
                holder.texts7.setText(model.getEvent_location());


                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String end_date = model.getEnd_date();
                        //SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

                        //Date parsedend_date = sdf1.parse(end_date);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate enddate = LocalDate.parse(end_date,formatter);
                        LocalDate datenow = LocalDate.now();
                        //datenow.isAfter(enddate)
                        if( datenow.isEqual(enddate) ||datenow.isBefore(enddate)){
                            Intent intent = new Intent(HomeScreen.this, View_Activity.class);
                            intent.putExtra("lectevent", getRef(position).getKey());
                            startActivity(intent);
                        }
                        else{

                            Toast.makeText(HomeScreen.this, "The event is has ended. Can not attend the event", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolder4 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_todayevent, parent, false);

                return new MyViewHolder4(v);
            }
        };
        adapter5.startListening();
        recyclerView5.setAdapter(adapter5);
    }

    private void Loadpickuplocationdata() {
        options6 = new FirebaseRecyclerOptions.Builder<pickuplocationclass>().setQuery(locationref, pickuplocationclass.class).build();
        adapter6 = new FirebaseRecyclerAdapter<pickuplocationclass, pickuplocationviewholder>(options6) {
            @Override
            protected void onBindViewHolder(@NonNull pickuplocationviewholder holder, @SuppressLint("RecyclerView") final int position, @NonNull pickuplocationclass model) {
                holder.texts1.setText(model.getName());
                Picasso.get().load(model.getPicture()).into(holder.image1);
            }

            @NonNull
            @Override
            public pickuplocationviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_pickup_location, parent, false);

                return new pickuplocationviewholder(v);
            }
        };
        adapter6.startListening();
        recyclerviewpickuplocation.setAdapter(adapter6);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_profle:
                Intent intent = new Intent(HomeScreen.this, UserProfile.class);
                startActivity(intent);
                break;
            case R.id.nav_ride:
                Intent intents = new Intent(HomeScreen.this, showDriver.class);
                startActivity(intents);
                break;
            /*case R.id.nav_car:
                Intent intentz = new Intent(HomeScreen.this, showCar.class);
                startActivity(intentz);
                break;*/
            case R.id.nav_request:
                Intent intente = new Intent(HomeScreen.this, requestActivity.class);
                startActivity(intente);
                break;
            case R.id.nav_sing_out:
                builder.setTitle("Sign Out")
                        .setMessage("Do you Really want to sign out?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(HomeScreen.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }).show();
                break;
        }
        return true;

    }

}
