package com.example.newjumshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class showCar extends AppCompatActivity {


    String[] car_item={"Axia","Saga","Bezza","Myvi","Dmax","Hilux","Waja","Wira", "Kancil","Iriz", "Wish", "Jazz","Avanza","X70","Alza","Aruz"};
    String[] color_item={"Red","Blue","Black","White","Grey","Green","Orange","Yellow"};
    String[] capacity_item={"4","6"};
    AutoCompleteTextView autoCompleteTextViewcar, autoCompleteTextViewcolor,autoCompleteTextViewcapacity;
    ArrayAdapter<String> adapterItems, adapterItems2, adapterItems3;
    String  item_car,item_color,item_capacity, carId;
    Button confirm_btn, myprofile_btn, mycar_btn;
    private DatabaseReference testRef, test, picRef,userRef;
    TextInputLayout platenum;
    TextView names, emel;
    ImageView profileimageView;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_car);

        testRef = FirebaseDatabase.getInstance().getReference("user_car");
        platenum =findViewById(R.id.plate_number);
        picRef = FirebaseDatabase.getInstance().getReference().child("users");
        names = findViewById(R.id.full_name);
        emel = findViewById(R.id.emails);
        profileimageView = findViewById(R.id.profile_image1);
        myprofile_btn=findViewById(R.id.buttonbprofile);
        mycar_btn=findViewById(R.id.buttonbcar);
        storageReference= FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profile.jpg");

        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileimageView);
            }
        });

        testRef.orderByChild("userId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        //autoCompleteTextViewcolor.setText((String) snapshots.child("carcolour").getValue());
                        platenum.getEditText().setText((String) snapshots.child("carplate").getValue());
                        //autoCompleteTextViewcar.setText((String) snapshots.child("cartype").getValue());
                        //autoCompleteTextViewcapacity.setText((String) snapshots.child("num_passenger").getValue());
                        carId = (String) snapshots.child("carId").getValue();
                        String name = (String) snapshots.child("cartype").getValue();
                        String name1= (String) snapshots.child("carcolour").getValue();;
                        String name2= (String) snapshots.child("num_passenger").getValue();
                        autoCompleteTextViewcar.setText(name,false);
                        autoCompleteTextViewcolor.setText(name1,false);
                        autoCompleteTextViewcapacity.setText(name2,false);
                        adapterItems.notifyDataSetChanged();
                        adapterItems2.notifyDataSetChanged();
                        adapterItems3.notifyDataSetChanged();

                    }
                } else {
                    Toast.makeText(showCar.this, "No Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(showCar.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        autoCompleteTextViewcar = findViewById(R.id.car);
        adapterItems = new ArrayAdapter<String>(this, R.layout.gender_list_item,car_item);
        autoCompleteTextViewcar.setAdapter(adapterItems);

        autoCompleteTextViewcolor = findViewById(R.id.CarColor);
        adapterItems2 = new ArrayAdapter<String>(this, R.layout.gender_list_item,color_item);
        autoCompleteTextViewcolor.setAdapter(adapterItems2);

        autoCompleteTextViewcapacity = findViewById(R.id.capacity);
        adapterItems3 = new ArrayAdapter<String>(this, R.layout.gender_list_item,capacity_item);
        autoCompleteTextViewcapacity.setAdapter(adapterItems3);

        autoCompleteTextViewcar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item_car = adapterView.getItemAtPosition(i).toString();
            }
        });

        autoCompleteTextViewcolor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item_color = adapterView.getItemAtPosition(i).toString();
            }
        });

        autoCompleteTextViewcapacity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item_capacity = adapterView.getItemAtPosition(i).toString();
            }
        });



        confirm_btn = findViewById(R.id.button_confirm);

        confirm_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(showCar.this,"Updated!", Toast.LENGTH_SHORT).show();
                update(v);
            }
        });

        myprofile_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Toast.makeText(showCar.this,"Updated!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(showCar.this, UserProfile.class));
            }
        });

        mycar_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(showCar.this,"You are already in My Car pages", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(UserProfile.this, showCar.class));
            }
        });

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                names.setText(snapshot.child("name").getValue().toString());
                emel.setText(snapshot.child("email").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(showCar.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        profileimageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(showCar.this,"Choose your profile picture", Toast.LENGTH_SHORT).show();
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000){
            if (resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                //profileimageView.setImageURI(imageUri);

                UploadImageToFirebase(imageUri);
            }
        }
    }

    private void UploadImageToFirebase(Uri imageUri) {

        String key= picRef.push().getKey();

        StorageReference fileRef = storageReference.child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileimageView);

                        //HashMap hashMap=new HashMap();
                        String picUri = uri.toString();
                        testRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pic").setValue(picUri).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(showCar.this,"Data Succesfully Uploaded!",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(showCar.this,"Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void update(View view){
        testRef.child(carId).child("carcolour").setValue(item_color);
        testRef.child(carId).child("carplate").setValue(platenum.getEditText().getText().toString());
        testRef.child(carId).child("cartype").setValue(item_car);
        testRef.child(carId).child("num_passenger").setValue(item_capacity);

    }
}