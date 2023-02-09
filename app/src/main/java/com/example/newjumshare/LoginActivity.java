package com.example.newjumshare;

import static com.example.newjumshare.UserHelperClass.currentUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    //private final static int LOGIN_REQUEST_CODE = 7171;

    boolean isPermissionGranted;

    private FirebaseAuth.AuthStateListener listener;
    private EditText email, password;
    private Button btnLogin, register;
    private TextView textRegister;
    private FirebaseAuth mAuth;
    DatabaseReference reference;
    FirebaseFirestore fStore;
    int reportnumz;
    String uid;

    //@Override
    /*protected void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(listener);

    }

    @Override
    protected void onStop() {
        if(firebaseAuth != null&& listener != null)
            firebaseAuth.removeAuthStateListener(listener);
        super.onStop();
    }*/

    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkMyPermission();
        firebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.login);
        fStore = FirebaseFirestore.getInstance();
        register = findViewById(R.id.register);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("users");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }



    private void checkMyPermission() {

        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                //Toast.makeText(LoginActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                isPermissionGranted = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }


    private void login() {

        reference = FirebaseDatabase.getInstance().getReference("users");
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        String user = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String email_data = user.replace('.', ',');
        String stu = "Student";


        if (user.isEmpty()) {
            email.setError("Email can not be empty");
        }
        if (pass.isEmpty()) {
            password.setError("Password can not be empty");
        } else {

            //reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            //@Override
            //public void onComplete(@NonNull Task<DataSnapshot> task) {
            //String user = email.getText().toString().trim();
            //String pass = password.getText().toString().trim();
            //if (task.isSuccessful()) {
            //if (task.getResult().exists()) {
            //   DataSnapshot dataSnapshot = task.getResult();
            //  String occ = String.valueOf(dataSnapshot.child("occupation").getValue());
            //  Toast.makeText(LoginActivity.this, "Successfully Read " , Toast.LENGTH_SHORT).show();

            // if (occ.equals("User")) {
            firebaseAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                reportnumz = snapshot.child("report_num").getValue(Integer.class);
                                uid = (String) snapshot.child("uid").getValue();
                                if (reportnumz <= 2) {
                                    //Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, HomeScreen.class));
                                } else {
                                    FirebaseAuth.getInstance().signOut();
                                    startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                                    Toast.makeText(LoginActivity.this, "You are being banned. Please contact the authority.", Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                //Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });



                        //UserHelperClass helperClass = new UserHelperClass(name,username,email,phoneNo,password,faculty,gender,occupation,report_num,pic);

                    } else {
                        Toast.makeText(LoginActivity.this, "Login Failed " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } //else {
        //Toast.makeText(LoginActivity.this, "Did not have same occupation ", Toast.LENGTH_SHORT).show();
        //}
        //} else {
        // Toast.makeText(LoginActivity.this, "Failed to read ", Toast.LENGTH_SHORT).show();
        //}
        //  } else {
        //Toast.makeText(LoginActivity.this, "Nothing to read ", Toast.LENGTH_SHORT).show();
        // }
        //}
        //});

           /*mAuth.signInWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"Login Failed "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });*/
        //}
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //FirebaseAuth.getInstance().signOut();
        if (currentUser != null) {
          reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //reportnumz = snapshot.child("report_num").getValue(Integer.class);
                    //if (reportnumz <= 2) {
                     //Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, HomeScreen.class));
                    //} else {
                        //FirebaseAuth.getInstance().signOut();
                        //Toast.makeText(LoginActivity.this, "Can not log in to account. Please contact the authority.", Toast.LENGTH_SHORT).show();

                        //startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                    //}
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                   // Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            //startActivity(new Intent(LoginActivity.this, HomeScreen.class));
            //Toast.makeText(LoginActivity.this, "test"+, Toast.LENGTH_SHORT).show();

        } else {

        }
    }
}