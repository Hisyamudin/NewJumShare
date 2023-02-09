package com.example.newjumshare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class addEvent2 extends AppCompatActivity {

    String[] location = {"Fakulti Kejuruteraan", "Dewan Canselor", "Pantai Odec", "Kolej Tun Mustapha", "Kolej Tun Fuad", "Kampung E", "Padang Kawad", "Library UMS", "1 Borndeo", "Dewan Kulah Pusat (Baru)", "Dewan Kuliah Pusat (Lama)", "Fakulti Psikologi Dan Pendidikan", "Fakulti Kemanusiaan, Seni Dan Warisan", "Fakulti Sains Dan Sumber Alam", "Fakulti Kewangan Antarabangsa Labuan", "Fakulti Perniagaan, Ekonomi Dan Perakaunan", "Fakulti Pertanian Lestari", "Fakulti Perubatan Dan Sains Kesihatan", "Fakulti Komputeran Dan Informatik", "Fakulti Sains Makanan Dan Pemakanan"};
    AutoCompleteTextView autoCompleteTextViewlocation;
    TextInputLayout event_namez, phone_numz, descriptionz;
    private EditText timestartPicker, timeendPicker, datestartPicker, dateendPicker;
    ArrayAdapter<String> adapterItems;
    String data_location,tarikh_data,key,lectevent,tarikh_data2;
    final Calendar myCalendar = Calendar.getInstance();
    Button confirm;
    String time1,time2;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event2);

        lectevent = getIntent().getStringExtra("EVENTNAME");
        event_namez = findViewById(R.id.eventnamez);
        phone_numz = findViewById(R.id.phoneNum);
        descriptionz = findViewById(R.id.Description);
        autoCompleteTextViewlocation = findViewById(R.id.Location);
        datestartPicker= findViewById(R.id.start_date);
        dateendPicker = findViewById(R.id.end_date);
        timestartPicker = findViewById(R.id.start_time);
        timeendPicker = findViewById(R.id.end_time);
        adapterItems = new ArrayAdapter<String>(this,R.layout.gender_list_item,location);
        autoCompleteTextViewlocation.setAdapter(adapterItems);
        confirm = findViewById(R.id.button_confirm);

        reference = FirebaseDatabase.getInstance().getReference("lectevent");

        autoCompleteTextViewlocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                data_location = adapterView.getItemAtPosition(i).toString();
            }
        });

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        timestartPicker.setOnClickListener(v->selectTime());
        timeendPicker.setOnClickListener(v->selectTime2());
        selectDate();
        tarikh_data = updateData();
        selectDate2();
        tarikh_data2 = updateData();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //key = FirebaseDatabase.getInstance().getReference("drivers_data").push().getKey();
                String name = event_namez.getEditText().getText().toString();
                String phone = phone_numz.getEditText().getText().toString();
                String describe = descriptionz.getEditText().getText().toString();
                String type = lectevent;
                String location = data_location;
                String timeend = "";
                String timestart = "";

                eventClass2 event = new eventClass2(tarikh_data2, time2, describe, location, name, phone, tarikh_data, time1, type);

                reference.child(name).setValue(event);

                Toast.makeText(addEvent2.this, "Successfuly add an event!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(new Intent(addEvent2.this, View_Activity.class));
                intent.putExtra("lectevent", lectevent);
                intent.putExtra("currenteventdriver",key);
                startActivity(intent);

            }
        });
    }

    private void selectDate(){
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day){
                myCalendar.set(Calendar.YEAR,year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);

                datestartPicker.setText(updateData());

            }
        };

        datestartPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(addEvent2.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void selectDate2(){
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day){
                myCalendar.set(Calendar.YEAR,year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);

                dateendPicker.setText(updateData());

            }
        };

        dateendPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(addEvent2.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private String updateData(){
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        return dateFormat.format(myCalendar.getTime());
    }


    private void selectTime() {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(addEvent2.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                currentTime.set(Calendar.HOUR_OF_DAY, hour);
                currentTime.set(Calendar.MINUTE, minute);

                String myFormat = "HH:mm:ss";
                SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                timestartPicker.setText(dateFormat.format(currentTime.getTime()));

                time1 = dateFormat.format(currentTime.getTime());
            }
        }, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();

    }
    private void selectTime2() {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(addEvent2.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                currentTime.set(Calendar.HOUR_OF_DAY, hour);
                currentTime.set(Calendar.MINUTE, minute);

                String myFormat = "HH:mm:ss";
                SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                timeendPicker.setText(dateFormat.format(currentTime.getTime()));
                time2 = dateFormat.format(currentTime.getTime());
            }
        }, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();

    }
    /*private String updateDatatime(){
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        return dateFormat.format(myCalendar.getTime());
    }*/
}