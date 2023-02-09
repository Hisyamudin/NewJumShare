package com.example.newjumshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class add_profile_picture extends AppCompatActivity {

    ImageView profileimageView;

    StorageReference storageReference;
    DatabaseReference picRef, testRef;
    Button next_btn;
    String userkey,picUri;
    AlertDialog.Builder builder;
    String pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile_picture);

        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/profile.jpg");
        picRef = FirebaseDatabase.getInstance().getReference().child("users");
        profileimageView = findViewById(R.id.profile_image);
        userkey = getIntent().getStringExtra("userKey");
        testRef = FirebaseDatabase.getInstance().getReference("users");
        builder = new AlertDialog.Builder(this);
        picUri="";

        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileimageView);
            }
        });
        next_btn = findViewById(R.id.button_next);

        next_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (picUri.equals("")){
                    Toast.makeText(add_profile_picture.this,"Please add your profile picture.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(add_profile_picture.this, "Profile Picture Updated!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(add_profile_picture.this, user_car.class);
                    intent.putExtra("userKey", userkey);
                    startActivity(intent);
                }
            }
        });
        profileimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(add_profile_picture.this, "Choose your profile picture", Toast.LENGTH_SHORT).show();
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                //profileimageView.setImageURI(imageUri);

                UploadImageToFirebase(imageUri);
            }
        }
    }

    private void UploadImageToFirebase(Uri imageUri) {

        String key = picRef.push().getKey();

        StorageReference fileRef = storageReference.child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileimageView);

                        //HashMap hashMap=new HashMap();
                        picUri = uri.toString();
                        testRef.child(userkey).child("pic").setValue(picUri).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(add_profile_picture.this, "Data Succesfully Uploaded!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(add_profile_picture.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
