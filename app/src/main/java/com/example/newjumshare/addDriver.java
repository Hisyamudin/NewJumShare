package com.example.newjumshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class addDriver extends AppCompatActivity {

    String[] car_item = {"Axia", "Saga", "Bezza", "Myvi", "Dmax", "Hilux", "Waja", "Wira", "Kancil", "Iriz", "Wish", "Jazz", "Avanza", "X70", "Alza", "Aruz"};
    String[] color_item = {"Red", "Blue", "Black", "White", "Grey", "Green", "Orange", "Yellow"};
    String[] capacity_item = {"4", "6"};
    TextInputLayout fullNames, car, plate_number, CarColor, phoneNum, time;
    DatePickerDialog.OnDateSetListener setListener;
    Button confirm;
    DatabaseReference userRef, reference, ref, test_ref, testRef1, carRef, userref2;
    FirebaseDatabase rootNode;
    String lectevent, eventname;
    String gender, pic, key, uid, tarikh_data, date_data;
    TextInputLayout platenum;
    AutoCompleteTextView autoCompleteTextViewcar;
    AutoCompleteTextView autoCompleteTextViewcolor;
    AutoCompleteTextView autoCompleteTextViewcapacity;
    ArrayAdapter<String> adapterItems, adapterItems2, adapterItems3;
    String item_car, item_color, item_capacity, carId, time2, startdate1, enddate1,starttime1, endtime1;
    TextView date;
    final Calendar myCalendar = Calendar.getInstance();
    private EditText timePicker, datePicker;
    DateTimeFormatter formatters;
    LocalDate ridedateInput, ridetimeInput, ridedats, ridetimes, datenows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver);

        //hook
        timePicker = findViewById(R.id.time_picker);
        datePicker = findViewById(R.id.date_picker);
        //date = findViewById(R.id.datepick);
        platenum = findViewById(R.id.plate_number);
        fullNames = findViewById(R.id.fullNamez);
        //car = findViewById(R.id.car);
        plate_number = findViewById(R.id.plate_number);
        //CarColor = findViewById(R.id.CarColor);
        phoneNum = findViewById(R.id.phoneNum);
        //time = findViewById(R.id.time);
        confirm = findViewById(R.id.button_confirm);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("drivers_data");
        test_ref = FirebaseDatabase.getInstance().getReference("users");//.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("gender").setValue(data_gender);
        //key = rootNode.getReference("drivers_data").push().getKey();
        testRef1 = FirebaseDatabase.getInstance().getReference("user_car");
        carRef = FirebaseDatabase.getInstance().getReference().child("user_car");
        ;
        userref2 = FirebaseDatabase.getInstance().getReference("users");
        startdate1 = getIntent().getStringExtra("startdate");
        enddate1 = getIntent().getStringExtra("enddate");
        starttime1 = getIntent().getStringExtra("starttime");
        endtime1 = getIntent().getStringExtra("endtime");
        datenows = LocalDate.now();

        autoCompleteTextViewcar = findViewById(R.id.car);
        adapterItems = new ArrayAdapter<String>(this, R.layout.gender_list_item, car_item);
        autoCompleteTextViewcar.setAdapter(adapterItems);

        autoCompleteTextViewcolor = findViewById(R.id.CarColor);
        adapterItems2 = new ArrayAdapter<String>(this, R.layout.gender_list_item, color_item);
        autoCompleteTextViewcolor.setAdapter(adapterItems2);

        autoCompleteTextViewcapacity = findViewById(R.id.CarCapacity);
        adapterItems3 = new ArrayAdapter<String>(this, R.layout.gender_list_item, capacity_item);
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

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        /*date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        addDriver.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        , setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1 = month + 1;
                String dates = day + "/" + i1 + "/" + year;
                date.setText(dates);
            }
        };*/
        timePicker.setOnClickListener(v -> selectTime());
        selectDate();
        tarikh_data = updateData();
        testRef1.orderByChild("userId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        //autoCompleteTextViewcolor.setText((String) snapshots.child("carcolour").getValue());
                        platenum.getEditText().setText((String) snapshots.child("carplate").getValue());
                        String name = (String) snapshots.child("cartype").getValue();
                        String name1 = (String) snapshots.child("carcolour").getValue();
                        //;
                        String name2 = (String) snapshots.child("num_passenger").getValue();
                        //autoCompleteTextViewcapacity.setText((String) snapshots.child("num_passenger").getValue());
                        carId = (String) snapshots.child("carId").getValue();

                        autoCompleteTextViewcar.setText(name, false);
                        autoCompleteTextViewcolor.setText(name1, false);
                        autoCompleteTextViewcapacity.setText(name2, false);
                        adapterItems.notifyDataSetChanged();
                        adapterItems2.notifyDataSetChanged();
                        adapterItems3.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(addDriver.this, "No Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(addDriver.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        test_ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    gender = snapshot.child("gender").getValue().toString();
                    pic = snapshot.child("pic").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lectevent = getIntent().getStringExtra("EVENTNAME");
        ref = FirebaseDatabase.getInstance().getReference().child("lectevent");

        ref.child(lectevent).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    eventname = snapshot.child("Event_name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fullNames.getEditText().setText(snapshot.child("name").getValue().toString());
                phoneNum.getEditText().setText(snapshot.child("phoneNo").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(addDriver.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                ridedateInput = LocalDate.parse(date_data, formatters);
                //ridetimeInput = LocalDate.parse(time2, formatters);
                ridedats = LocalDate.parse(startdate1, formatters);
                ridetimes = LocalDate.parse(enddate1, formatters);
                LocalTime starttime11 = LocalTime.parse(starttime1,DateTimeFormatter.ofPattern("HH:mm"));
                LocalTime endtime11 = LocalTime.parse(endtime1,DateTimeFormatter.ofPattern("HH:mm"));
                LocalTime inputtime = LocalTime.parse(time2,DateTimeFormatter.ofPattern("HH:mm"));
                if (ridedateInput.isBefore(ridedats) || ridedateInput.isAfter(ridetimes)) {
                    Toast.makeText(addDriver.this, "The pickup date is invalid because the event is not started yet or is ended during that date.", Toast.LENGTH_SHORT).show();
                }
                else if(inputtime.isBefore(starttime11) || inputtime.isAfter(endtime11)){
                    Toast.makeText(addDriver.this, "The pickup time is invalid because the event is not started yet or is ended during that time.", Toast.LENGTH_SHORT).show();
                }else {
                    key = FirebaseDatabase.getInstance().getReference("drivers_data").push().getKey();
                    String name = fullNames.getEditText().getText().toString();
                    String car_data = item_car;
                    String plate_number_data = platenum.getEditText().getText().toString();
                    String phoneNum_data = phoneNum.getEditText().getText().toString();
                    String car_color_data = item_color;
                    //String time_data = time.getEditText().getText().toString();

                    //String gender_data = gender;
                    userref2.child(uid).child("drivernum").setValue(ServerValue.increment(1));

                    driverClass driver = new driverClass(name, car_data, plate_number_data, car_color_data, phoneNum_data, time2, eventname, gender, pic, uid, item_capacity, carId, key, date_data);

                    reference.child(key).setValue(driver);
                    Toast.makeText(addDriver.this, "Successfuly became a Driver!", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(addDriver.this, View_Activity.class));
                    Intent intent = new Intent(new Intent(addDriver.this, View_Activity.class));
                    intent.putExtra("lectevent", lectevent);
                    //intent.putExtra("currenteventdriver",key);

                }
            }
        });
    }

    private void selectDate() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);

                datePicker.setText(updateData());
                date_data = updateData();
            }
        };
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(addDriver.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private String updateData() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        return dateFormat.format(myCalendar.getTime());
    }


    private void selectTime() {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(addDriver.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                currentTime.set(Calendar.HOUR_OF_DAY, hour);
                currentTime.set(Calendar.MINUTE, minute);

                String myFormat = "HH:mm";
                SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                timePicker.setText(dateFormat.format(currentTime.getTime()));
                time2 = dateFormat.format(currentTime.getTime());
            }
        }, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();

    }
}