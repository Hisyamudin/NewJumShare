package com.example.newjumshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SearchMap extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap googleMap;
    ImageView imageViewSearch;
    EditText inputLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_map);

        imageViewSearch=findViewById(R.id.imageViewSearch);
        inputLocation=findViewById(R.id.inputLocation);

        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.contaner, supportMapFragment).commit();
        supportMapFragment.getMapAsync(this);

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = inputLocation.getText().toString();
                if(location!=null){
                    Toast.makeText(SearchMap.this,"Type any location name", Toast.LENGTH_SHORT).show();

                }else{

                    Geocoder geocoder=new Geocoder(SearchMap.this, Locale.getDefault());
                    try {
                        List<Address>listAddress=geocoder.getFromLocationName(location,1);
                        if(listAddress.size()>0){
                            LatLng latLng = new LatLng(listAddress.get(0).getLatitude(),listAddress.get(0).getLongitude());

                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.title("My Search Position");
                            markerOptions.position(latLng);
                            googleMap.addMarker(markerOptions);
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,5);
                            googleMap.animateCamera(cameraUpdate);

                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng latLng = new LatLng(45.262041,-104.434847);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("My Position");
        markerOptions.position(latLng);
        googleMap.addMarker(markerOptions);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,5);
        googleMap.animateCamera(cameraUpdate);

        return;
    }
}