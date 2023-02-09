package com.example.newjumshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class addstudentdriver extends AppCompatActivity {

    String[] car_item = {"Axia", "Saga", "Bezza", "Myvi", "Dmax", "Hilux", "Waja", "Wira", "Kancil", "Iriz", "Wish", "Jazz", "Avanza", "X70", "Alza", "Aruz"};
    String[] color_item = {"Red", "Blue", "Black", "White", "Grey", "Green", "Orange", "Yellow"};
    String[] capacity_item = {"4", "6"};
    String[] location = {"Fakulti Kejuruteraan FKJ", "Fakulti Komputeran dan Informatik (FKI)", "UMS ODEC beach", "Kolej TUN MUSTAPHA UMS, Kota Kinabalu, Sabah", "Bustop Kolej Kediaman Tun Fuad (CD)", "Kolej Kediaman E (Kampung E) Universiti Malaysia Sabah", "Padang Kawad UMS", "Perpustakaan Universiti Malaysia Sabah", "1 Borneo Mall", "Dewan Kuliah Pusat (DKP Baru) - UMS", "DKP Lama / Dewan Kuliah Pusat I", "Fakulti Psikologi dan Pendidikan", "Fakulti Kemanusian, Seni dan Warisan (FKSW)", "Fakulti Sains Dan Sumber Alam", "Faculty of Business, Economics and Accountancy", "Fakulti Perubatan dan Sains Kesihatan (FPSK)", "Fakulti Sains Makanan Dan Pemakanan", "Anjung Siswa, UMS", "Pusat Rawatan Warga UMS", "Kompleks Sukan UMS", "Pusat Penataran Ilmu dan Bahasa", "Masjid Universiti Malaysia Sabah", "Bahagian Keselamatan Universiti Malaysia Sabah"};
    TextInputLayout fullNames, car, plate_number, CarColor, phoneNum, time, eventnamez;
    DatePickerDialog.OnDateSetListener setListener;
    Button confirm;
    DatabaseReference userRef, reference, ref, test_ref, testRef1, carRef, userref2, eventref,locationref ;
    ;
    FirebaseDatabase rootNode;
    String lectevent, eventname;
    String gender, pic, key, uid, tarikh_data, date_data;
    TextInputLayout platenum;
    AutoCompleteTextView autoCompleteTextViewcar;
    AutoCompleteTextView autoCompleteTextViewcolor;
    AutoCompleteTextView autoCompleteTextViewcapacity;
    AutoCompleteTextView autoCompleteTextdestination;
    ArrayAdapter<String> adapterItems, adapterItems2, adapterItems3,arrayAdapter4;
    String item_car, item_color, item_capacity, carId, time2, item_destination;
    TextView date;
    final Calendar myCalendar = Calendar.getInstance();
    private EditText timePicker, datePicker;
    AlertDialog.Builder builder;
    ArrayList<String> dataArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstudentdriver);

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
        eventnamez = findViewById(R.id.event_name);
        //time = findViewById(R.id.time);
        confirm = findViewById(R.id.button_confirm);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("drivers_data");
        eventref = rootNode.getReference("lectevent");
        test_ref = FirebaseDatabase.getInstance().getReference("users");//.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("gender").setValue(data_gender);
        //key = rootNode.getReference("drivers_data").push().getKey();
        testRef1 = FirebaseDatabase.getInstance().getReference("user_car");
        carRef = FirebaseDatabase.getInstance().getReference().child("user_car");
        builder = new AlertDialog.Builder(this);
        locationref = FirebaseDatabase.getInstance().getReference().child("location_data");
        dataArray = new ArrayList<>();
        locationref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        pickuplocationclass event = snapshots.getValue(pickuplocationclass.class);
                        dataArray.add(event.getName());
                    }
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        userref2 = FirebaseDatabase.getInstance().getReference("users");

        autoCompleteTextViewcar = findViewById(R.id.car);
        adapterItems = new ArrayAdapter<String>(this, R.layout.gender_list_item, car_item);
        autoCompleteTextViewcar.setAdapter(adapterItems);

        autoCompleteTextViewcolor = findViewById(R.id.CarColor);
        adapterItems2 = new ArrayAdapter<String>(this, R.layout.gender_list_item, color_item);
        autoCompleteTextViewcolor.setAdapter(adapterItems2);

        autoCompleteTextViewcapacity = findViewById(R.id.CarCapacity);
        adapterItems3 = new ArrayAdapter<String>(this, R.layout.gender_list_item, capacity_item);
        autoCompleteTextViewcapacity.setAdapter(adapterItems3);

        autoCompleteTextdestination = findViewById(R.id.destination);
        arrayAdapter4 = new ArrayAdapter<String>(this, R.layout.gender_list_item, dataArray);
        autoCompleteTextdestination.setAdapter(arrayAdapter4);

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

        autoCompleteTextdestination.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item_destination = adapterView.getItemAtPosition(i).toString();
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
                    Toast.makeText(addstudentdriver.this, "No Data", Toast.LENGTH_SHORT).show();
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

        lectevent = getIntent().getStringExtra("keys");
        ref = FirebaseDatabase.getInstance().getReference().child("lectevent");

//        ref.child(lectevent).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    eventname = snapshot.child("Event_name").getValue().toString();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

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

                builder.setTitle("Confirmation")
                        .setMessage("Does the data entered is correct?")
                        .setCancelable(true)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                key = FirebaseDatabase.getInstance().getReference("drivers_data").push().getKey();
                                String name = fullNames.getEditText().getText().toString();
                                String car_data = item_car;
                                String plate_number_data = platenum.getEditText().getText().toString();
                                String phoneNum_data = phoneNum.getEditText().getText().toString();
                                String car_color_data = item_color;
                                //String time_data = time.getEditText().getText().toString();
                                String enddate="";
                                String eventnames = eventnamez.getEditText().getText().toString();
                                //String gender_data = gender;
                                userref2.child(uid).child("drivernum").setValue(ServerValue.increment(1));
                                //String eventnames= lectevent+ uid;
                                studentdriverclass driver = new studentdriverclass(name, car_data, plate_number_data, car_color_data, phoneNum_data, time2, eventnames, gender, pic, uid, item_capacity, carId, key, date_data,item_destination);
                                studentneweventclass event = new studentneweventclass(enddate, enddate, enddate, item_destination, eventnames, enddate,date_data, time2, lectevent);
                                reference.child(key).setValue(driver);
                                eventref.child(eventnames).setValue(event);;
                                Toast.makeText(addstudentdriver.this, "Successfuly became a Driver!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();


                //startActivity(new Intent(addDriver.this, View_Activity.class));
//                Intent intent = new Intent(new Intent(addstudentdriver.this, View_Activity.class));
//                intent.putExtra("lectevent", lectevent);
//                //intent.putExtra("currenteventdriver",key);
//                startActivity(intent);
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
                new DatePickerDialog(addstudentdriver.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
        timePickerDialog = new TimePickerDialog(addstudentdriver.this, new TimePickerDialog.OnTimeSetListener() {
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