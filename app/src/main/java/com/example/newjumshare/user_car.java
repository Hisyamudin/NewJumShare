package com.example.newjumshare;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class user_car extends AppCompatActivity {

    String[] car_item={"Axia","Saga","Bezza","Myvi","Dmax","Hilux","Waja","Wira", "Kancil","Iriz", "Wish", "Jazz","Avanza","X70","Alza","Aruz"};
    String[] color_item={"Red","Blue","Black","White","Grey","Green","Orange","Yellow"};
    String[] capacity_item={"4","6"};
    AutoCompleteTextView autoCompleteTextViewcar, autoCompleteTextViewcolor,autoCompleteTextViewcapacity;
    ArrayAdapter<String> adapterItems, adapterItems2, adapterItems3;
    String  item_car,item_color,item_capacity, userkey, plate,key;
    Button confirm_btn;
    TextView skip;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    TextInputLayout plate_num;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_car);

        confirm_btn= findViewById(R.id.button_confirm);
        skip = findViewById(R.id.text);
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("user_car");
        userkey = getIntent().getStringExtra("userKey");
        plate_num = findViewById(R.id.plate_number);
        builder = new AlertDialog.Builder(this);

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

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder.setTitle("Confirmation")
                        .setMessage("Are you sure to skip this step? (You can still set your car inside the system")
                        .setCancelable(true)
                        .setPositiveButton("Skip", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        item_car = "";
                                        item_color="";
                                        plate="";
                                        item_capacity="";
                                        key = FirebaseDatabase.getInstance().getReference("user_car").push().getKey();
                                        carClass carClass = new carClass(item_car,item_color,key,userkey,plate, item_capacity);
                                        reference.child(key).setValue(carClass);

                                        Toast.makeText(user_car.this, "Successfully Register!", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(user_car.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                })
                        .setNegativeButton("Stay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();


            }
        });

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plate = plate_num.getEditText().getText().toString();

                key = FirebaseDatabase.getInstance().getReference("user_car").push().getKey();
                carClass carClass = new carClass(item_car,item_color,key,userkey,plate, item_capacity);

                reference.child(key).setValue(carClass);
                Toast.makeText(user_car.this, "Successfully Register!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(user_car.this, LoginActivity.class);
                intent.putExtra("userKey",key);
                startActivity(intent);
            }
        });
    }
}