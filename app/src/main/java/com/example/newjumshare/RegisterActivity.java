package com.example.newjumshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    Button register,regBtn;
    TextInputLayout regName,regEmail,regPhoneNo, regPassword;
    private FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    String[] gender_item={"Male","Female"};
    String[] occ_item = {"Student","Lecturer", "Non-Academic Staff"};
    String[] faculty_item={"Fakulti Kejuruteraan", "Fakulti Psikologi Dan Pendidikan", "Fakulti Kemanusiaan, Seni Dan Warisan", "Fakulti Sains Dan Sumber Alam", "Fakulti Kewangan Antarabangsa Labuan", "Fakulti Perniagaan, Ekonomi Dan Perakaunan", "Fakulti Pertanian Lestari", "Fakulti Perubatan Dan Sains Kesihatan", "Fakulti Komputeran Dan Informatik", "Fakulti Sains Makanan Dan Pemakanan","None"};
    AutoCompleteTextView autoCompleteTextViewgender, autoCompleteTextViewfaculty, autoCompleteTextViewocc;
    ArrayAdapter<String> adapterItems, adapterItems2, adapterItems3;
    String item_gender, item_faculty,item_occ,key;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        regName = findViewById(R.id.name);
        //regUsername = findViewById(R.id.username);
        regEmail = findViewById(R.id.email);
        regPhoneNo = findViewById(R.id.phoneNo);
        regPassword = findViewById(R.id.password);
        regBtn = findViewById(R.id.reg_btn);
        register = findViewById(R.id.register);

        autoCompleteTextViewgender = findViewById(R.id.genderr);
        adapterItems = new ArrayAdapter<String>(this, R.layout.gender_list_item,gender_item);
        autoCompleteTextViewgender.setAdapter(adapterItems);

        autoCompleteTextViewfaculty = findViewById(R.id.facultyy);
        adapterItems2 = new ArrayAdapter<String>(this, R.layout.faculty_list_item,faculty_item);
        autoCompleteTextViewfaculty.setAdapter(adapterItems2);

        autoCompleteTextViewocc = findViewById(R.id.occupationn);
        adapterItems3 = new ArrayAdapter<String>(this, R.layout.occupation_list_item,occ_item);
        autoCompleteTextViewocc.setAdapter(adapterItems3);

        autoCompleteTextViewgender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item_gender = adapterView.getItemAtPosition(i).toString();
            }
        });

        autoCompleteTextViewfaculty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item_faculty = adapterView.getItemAtPosition(i).toString();
            }
        });

        autoCompleteTextViewocc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item_occ = adapterView.getItemAtPosition(i).toString();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");

                //Get All Values
                String name = regName.getEditText().getText().toString();
                //String username = regUsername.getEditText().getText().toString();
                String email = regEmail.getEditText().getText().toString();
                String phoneNo = regPhoneNo.getEditText().getText().toString();
                String password = regPassword.getEditText().getText().toString();
                int report_num = 0;

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            key = user.getUid();

                            //Toast.makeText(RegisterActivity.this, "Success!",Toast.LENGTH_SHORT).show();
                            String email_data = email.replace('.', ',');
                            double rating = 5.00;
                            int pending = 0;
                            int drivernum = 0;
                            int passengernum = 0;
                            String pic = "";

                            UserHelperClass helperClass = new UserHelperClass(name,email,phoneNo,password,item_faculty,item_gender,item_occ,report_num,pic,key,rating, pending,drivernum, passengernum);

                            reference.child(user.getUid()).setValue(helperClass);
                            Intent intent = new Intent(RegisterActivity.this, add_profile_picture.class);
                            intent.putExtra("userKey",key);
                            startActivity(intent);

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Someone already used the email.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });



            }
        });

    }


}