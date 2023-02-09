package com.example.newjumshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class bookedDriverData extends AppCompatActivity {

    String[] gendery = {"Male", "Female"};
    String[] location = {"Fakulti Kejuruteraan","Dewan Canselor","Pantai Odec","Kolej Tun Mustapha", "Kolej Tun Fuad", "Kampung E","Padang Kawad","Library UMS", "1 Borndeo","Dewan Kulah Pusat (Baru)","Dewan Kuliah Pusat (Lama)", "Fakulti Psikologi Dan Pendidikan", "Fakulti Kemanusiaan, Seni Dan Warisan", "Fakulti Sains Dan Sumber Alam", "Fakulti Kewangan Antarabangsa Labuan", "Fakulti Perniagaan, Ekonomi Dan Perakaunan", "Fakulti Pertanian Lestari", "Fakulti Perubatan Dan Sains Kesihatan", "Fakulti Komputeran Dan Informatik", "Fakulti Sains Makanan Dan Pemakanan"};
    TextInputLayout name, phone;
    Button btn_book;
    String key, data_gender, data_location,uid, driverKey,lectevent,driverkeyid,carsid;
    DatabaseReference reference,userRef,ref, requestref;
    FirebaseDatabase rootNode;

    AutoCompleteTextView autoCompleteTextViewgender, autoCompleteTextViewlocation;
    ArrayAdapter<String> adapterItems, adapterItems2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_driver_data);

        autoCompleteTextViewgender = findViewById(R.id.genders);
       // autoCompleteTextViewlocation = findViewById(R.id.pickupLocation);
        name = findViewById(R.id.fullName);
        phone = findViewById(R.id.phonez);
        btn_book = findViewById(R.id.button_book);
        rootNode = FirebaseDatabase.getInstance();
        //reference = rootNode.getReference("booked_drivers");
        ref = rootNode.getReference("drivers_data");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        driverKey = getIntent().getStringExtra("driverKey");
        driverkeyid = getIntent().getStringExtra("driveridkey");
        carsid = getIntent().getStringExtra("carid");
        lectevent = getIntent().getStringExtra("lectevent");
        requestref = rootNode.getReference("request_booking");


        adapterItems = new ArrayAdapter<String>(this,R.layout.gender_list_item,gendery);
        adapterItems2 = new ArrayAdapter<String>(this,R.layout.gender_list_item,location);

        autoCompleteTextViewgender.setAdapter(adapterItems);
//        autoCompleteTextViewlocation.setAdapter(adapterItems2);

/*        autoCompleteTextViewlocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                data_location = adapterView.getItemAtPosition(i).toString();
            }
        });*/

        autoCompleteTextViewgender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                data_gender = adapterView.getItemAtPosition(i).toString();

            }
        });

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.getEditText().setText(snapshot.child("name").getValue().toString());
                phone.getEditText().setText(snapshot.child("phoneNo").getValue().toString());
                autoCompleteTextViewgender.setText(snapshot.child("gender").getValue().toString(),false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(bookedDriverData.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        /*ref.orderByChild("id").equalTo(driverKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                driverevent = (String) snapshot.child("event_name").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(bookedDriverData.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/

        btn_book.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                key = FirebaseDatabase.getInstance().getReference("drivers_data").push().getKey().toString();
                String namez = name.getEditText().getText().toString();
                String phonez = phone.getEditText().getText().toString();
                String driveevent =lectevent;
                //Toast.makeText(bookedDriverData.this, driverKey, Toast.LENGTH_SHORT).show();
                int count =0;
               //bookeddriver bookeddriver = new bookeddriver(key, namez, data_gender, phonez, lectevent,data_location , driverKey, uid,driverkeyid, carsid, count);

               //requestref.child(key).setValue(bookeddriver);
               // Toast.makeText(bookedDriverData.this, "Successfully Request a Driver!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(bookedDriverData.this,Map2.class);
                //intent.putExtra("driverKey",driver_uid);
                intent.putExtra("key",key);
                intent.putExtra("data_gender",data_gender);
                intent.putExtra("namez",namez);
                intent.putExtra("phonez",phonez);
                intent.putExtra("lectevent",lectevent);
                intent.putExtra("data_location",data_location);
                intent.putExtra("driverKey",driverKey);
                intent.putExtra("uid",uid);
                intent.putExtra("driverkeyid",driverkeyid);
                intent.putExtra("carsid",carsid);
                intent.putExtra("count",count);
                //intent.putExtra("driverKey",driver_uid);
                /*Penting*///intent.putExtra("lectevent",lectevent);
                //.putExtra("driveridkey",driverkey_id);
                //intent.putExtra("carid",car_id);
                startActivity(intent);
            }
        });
    }
}