package com.example.newjumshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class reportUserActivity extends AppCompatActivity {

    String name, gender, faculty, rating, passids, currentid, key,currentname;
    int reps;
    TextView namez, genderz, facultyz, ratingz, reportitem;
    DatabaseReference dataref, reportref, testRef;
    Button repors;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_user);

        passids = getIntent().getStringExtra("userID");
        dataref = FirebaseDatabase.getInstance().getReference().child("users");
        reportref = FirebaseDatabase.getInstance().getReference("reports");
        testRef = FirebaseDatabase.getInstance().getReference("users");

        rating = "0.0";
        namez = findViewById(R.id.userzname);
        genderz = findViewById(R.id.userzgende);
        facultyz = findViewById(R.id.userzfaculty);
        ratingz = findViewById(R.id.userzrating);
        reportitem = findViewById(R.id.report);
        currentid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        repors = findViewById(R.id.button_confirm);
        image = findViewById(R.id.profile_image);

        dataref.child(currentid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentname = (String) snapshot.child("name").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dataref.child(passids).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    name = (String) snapshot.child("name").getValue();
                    gender = (String) snapshot.child("phoneNo").getValue();
                    //ratingz = (String)snapshot.child("phoneNo").getValue();
                    faculty = (String) snapshot.child("faculty").getValue();
                    reps = snapshot.child("report_num").getValue(Integer.class);
                    String picsreps = (String) snapshot.child("pic").getValue();
                    double ratez =  snapshot.child("rating").getValue(double.class);
                    String str = ratez+""; // str is '123.45'

                    namez.setText(name);
                    genderz.setText(gender);
                    facultyz.setText(faculty);
                    Picasso.get().load(picsreps).into(image);
                    ratingz.setText(str);
                    //ratingz.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        repors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                key = FirebaseDatabase.getInstance().getReference("reports").push().getKey();
                String item = reportitem.getText().toString();
                reportClass report = new reportClass(currentid, passids, item,currentname,name);

                reportref.child(key).setValue(report);
                Toast.makeText(reportUserActivity.this, "User reported!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(reportUserActivity.this, HomeScreen.class);
                //intent.putExtra("userKey", key);
                startActivity(intent);


                reps = reps + 1;
                testRef.child(passids).child("report_num").setValue(reps);


            }
        });


    }
}