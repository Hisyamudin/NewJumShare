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

import java.util.HashMap;

public class UserProfile extends AppCompatActivity {

    DatabaseReference userRef, testRef, picRef;
    TextInputLayout name, gender, faculty, email, phoneNum;
    TextView names, emel;
    ImageView profileimageView;
    Button update_btn, myprofile_btn, mycar_btn;
    StorageReference storageReference;
    String[] genders = {"Male","Female", "Prefer not to say"};
    String[] occupation = {"Student","Lecturer", "Academic Staff","Non-Academic Staff"};
    String[] facultyz = {"Fakulti Kejuruteraan", "Fakulti Psikologi Dan Pendidikan", "Fakulti Kemanusiaan, Seni Dan Warisan", "Fakulti Sains Dan Sumber Alam", "Fakulti Kewangan Antarabangsa Labuan", "Fakulti Perniagaan, Ekonomi Dan Perakaunan", "Fakulti Pertanian Lestari", "Fakulti Perubatan Dan Sains Kesihatan", "Fakulti Komputeran Dan Informatik", "Fakulti Sains Makanan Dan Pemakanan"};
    String data_gender, data_faculty,data_occupation, uid;

    AutoCompleteTextView genderz, facultys, occ;
    ArrayAdapter<String> adapterItems,adapterItems2, adapterItems3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //hooks
        names = findViewById(R.id.full_name);
        emel = findViewById(R.id.emails);
        name = findViewById(R.id.fullName);
        myprofile_btn=findViewById(R.id.buttonbprofile);
        mycar_btn=findViewById(R.id.buttonbcar);
        //gender = findViewById(R.id.Gender);
        //faculty = findViewById(R.id.faculty);
        email = findViewById(R.id.email);
        phoneNum = findViewById(R.id.phoneNum);
        profileimageView = findViewById(R.id.profile_image1);
        storageReference= FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profile.jpg");

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        picRef = FirebaseDatabase.getInstance().getReference().child("users");

        genderz = findViewById(R.id.genderz);
        facultys = findViewById(R.id.facultys);
        occ= findViewById(R.id.occ);

        adapterItems = new ArrayAdapter<String>(this, R.layout.gender_list_item,genders);
        adapterItems2 = new ArrayAdapter<String>(this, R.layout.faculty_list_item,facultyz);
        adapterItems3 = new ArrayAdapter<String>(this, R.layout.faculty_list_item,occupation);

        genderz.setAdapter(adapterItems);
        facultys.setAdapter(adapterItems2);
        occ.setAdapter(adapterItems3);

        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileimageView);
            }
        });

        genderz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                data_gender = adapterView.getItemAtPosition(i).toString();
            }
        });

        facultys.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                data_faculty = adapterView.getItemAtPosition(i).toString();
            }
        });

        occ.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                data_occupation = adapterView.getItemAtPosition(i).toString();
            }
        });


        testRef = FirebaseDatabase.getInstance().getReference("users");

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                names.setText(snapshot.child("name").getValue().toString());
                emel.setText(snapshot.child("email").getValue().toString());
                name.getEditText().setText(snapshot.child("name").getValue().toString());
                facultys.setText(snapshot.child("faculty").getValue().toString(),false);
                genderz.setText(snapshot.child("gender").getValue().toString(),false);
                occ.setText(snapshot.child("occupation").getValue().toString(),false);
                phoneNum.getEditText().setText(snapshot.child("phoneNo").getValue().toString());
                email.getEditText().setText(snapshot.child("email").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(UserProfile.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        update_btn = findViewById(R.id.button_upd);

        update_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(UserProfile.this,"Updated!", Toast.LENGTH_SHORT).show();
                update(v);
            }
        });
        profileimageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(UserProfile.this,"Choose your profile picture", Toast.LENGTH_SHORT).show();
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
            }
            });

        myprofile_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(UserProfile.this,"You are already in My Profile pages", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(showCar.this, UserProfile.class));
            }
        });

        mycar_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Toast.makeText(showCar.this,"You are already in My Car pages", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserProfile.this, showCar.class));
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
                                Toast.makeText(UserProfile.this,"Data Succesfully Uploaded!",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserProfile.this,"Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void update(View view){
        testRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").setValue(name.getEditText().getText().toString());
        testRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("phoneNo").setValue(phoneNum.getEditText().getText().toString());
        testRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").setValue(email.getEditText().getText().toString());
        testRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("gender").setValue(data_gender);
        testRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("faculty").setValue(data_faculty);
        testRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("occupation").setValue(data_occupation);
        testRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("uid").setValue(uid);
        //testRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").setValue(name.getEditText().getText().toString());
    }

}