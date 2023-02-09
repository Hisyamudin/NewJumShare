package com.example.newjumshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FeedbackActivity extends AppCompatActivity {

    TextView tvFeedback, name,rating1;
    RatingBar tbStars;
    EditText editText;
    int rating,new_rating;
    String driverKey, passengerid,key,drivername,currentid,currentzname;
    DatabaseReference reference, userref,testref,dataref;
    ImageView profile_pic;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        name = findViewById(R.id.drivername);
        send= findViewById(R.id.button_send);
        profile_pic = findViewById(R.id.profile_image);
        tvFeedback = findViewById(R.id.test);
        tbStars = findViewById(R.id.ratingBar);
        editText = findViewById(R.id.edit_text);
        driverKey = getIntent().getStringExtra("keydrivers");
        passengerid  = getIntent().getStringExtra("passengerid");
        reference = FirebaseDatabase.getInstance().getReference("feedback");
        userref = FirebaseDatabase.getInstance().getReference().child("users").child(driverKey);
        testref = FirebaseDatabase.getInstance().getReference("users");
        rating1 = findViewById(R.id.rating);
        currentid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataref = FirebaseDatabase.getInstance().getReference().child("users");

        //editText.getText().toString();
        dataref.child(currentid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentzname = snapshot.child("name").getValue().toString();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                drivername = snapshot.child("name").getValue().toString();
                name.setText(snapshot.child("name").getValue().toString());
                rating1.setText(snapshot.child("rating").getValue().toString());
                String pic = snapshot.child("pic").getValue().toString();
                Picasso.get().load(pic).into(profile_pic);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        tbStars.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (v==0){
                    tvFeedback.setText("Very Dissatisfied");
                    rating = 0;
                }
                else if (v==1){
                    tvFeedback.setText("Dissatisfied");
                    rating = 1;
                }
                else if (v==2){
                    tvFeedback.setText("Sligtly Dissatisfied");
                    rating = 2;
                }
                else if (v==3){
                    tvFeedback.setText("Sligtly Satisfied");
                    rating = 3;
                }
                else if (v==4){
                    tvFeedback.setText("Satisfied");
                    rating = 4;
                }
                else if (v==5){
                    tvFeedback.setText("Very Satisfied");
                    rating = 5;
                }
            }

        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                key = FirebaseDatabase.getInstance().getReference("feedback").push().getKey().toString();
                feedbackclass feedback = new feedbackclass(editText.getText().toString(),driverKey,passengerid,rating,key,currentzname,drivername);
                reference.child(key).setValue(feedback);

                userref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        double ratings = snapshot.child("rating").getValue(double.class);
                        double ratingz = (rating+ratings)/2;

                        testref.child(driverKey).child("rating").setValue(ratingz);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Toast.makeText(FeedbackActivity.this, "Feedback sumitted!", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(FeedbackActivity.this, HomeScreen.class));

            }
        });

        /**/


    }
}