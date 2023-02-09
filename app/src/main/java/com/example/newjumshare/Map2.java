package com.example.newjumshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Map2 extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks ,
GoogleApiClient.OnConnectionFailedListener{

    String[] location = {"Fakulti Kejuruteraan FKJ", "Fakulti Komputeran dan Informatik (FKI)", "UMS ODEC beach", "Kolej TUN MUSTAPHA UMS, Kota Kinabalu, Sabah", "Bustop Kolej Kediaman Tun Fuad (CD)", "Kolej Kediaman E (Kampung E) Universiti Malaysia Sabah", "Padang Kawad UMS", "Perpustakaan Universiti Malaysia Sabah", "1 Borneo Mall", "Dewan Kuliah Pusat (DKP Baru) - UMS", "DKP Lama / Dewan Kuliah Pusat I", "Fakulti Psikologi dan Pendidikan", "Fakulti Kemanusian, Seni dan Warisan (FKSW)", "Fakulti Sains Dan Sumber Alam", "Faculty of Business, Economics and Accountancy", "Fakulti Perubatan dan Sains Kesihatan (FPSK)", "Fakulti Sains Makanan Dan Pemakanan", "Anjung Siswa, UMS", "Pusat Rawatan Warga UMS", "Kompleks Sukan UMS", "Pusat Penataran Ilmu dan Bahasa", "Masjid Universiti Malaysia Sabah", "Bahagian Keselamatan Universiti Malaysia Sabah"};
    GoogleMap mgoogleMap;
    ImageView imageViewSearch;
    //EditText inputLocation;
    AutoCompleteTextView inputLocation;
    ArrayAdapter<String> adapterItems;
    String driverKey, key1, data_gender1, namez1, phonez1, lectevent1, data_location1, driverKey1, uid1, driverkeyid1, carsid1;
    int count1, new_pending,pendings;
    Button confirm;
    DatabaseReference requestref,userref,userref2,locationref;
    FirebaseDatabase rootNode;
    AlertDialog.Builder builder;
    private FusedLocationProviderClient mLocationClient;
    SupportMapFragment mapFragment;
    private FloatingActionButton button;
    //ref.orderByChild("childKey").ascending().get()                    //////////////////PENTING
    ArrayList<String> dataArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);

        //hook
        imageViewSearch = findViewById(R.id.imageViewSearch);
        inputLocation = findViewById(R.id.inputLocation);
        //driverKey = getIntent().getStringExtra("driverKey");
        key1 = getIntent().getStringExtra("key");
        data_gender1 = getIntent().getStringExtra("data_gender");
        namez1 = getIntent().getStringExtra("namez");
        phonez1 = getIntent().getStringExtra("phonez");
        lectevent1 = getIntent().getStringExtra("lectevent");
        data_location1 = getIntent().getStringExtra("data_location");
        driverKey1 = getIntent().getStringExtra("driverKey");
        uid1 = getIntent().getStringExtra("uid");
        driverkeyid1 = getIntent().getStringExtra("driverkeyid");
        carsid1 = getIntent().getStringExtra("carsid");
        confirm = findViewById(R.id.btncnfrm);
        rootNode = FirebaseDatabase.getInstance();
        requestref = rootNode.getReference("request_booking");
        locationref = FirebaseDatabase.getInstance().getReference().child("location_data");

        builder = new AlertDialog.Builder(this);
        button = findViewById(R.id.ActionButtonId);
        new_pending = 0;
        pendings = 0;
        userref2 = FirebaseDatabase.getInstance().getReference("users");
        dataArray = new ArrayList<>();
        checkmyPermission();
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
        adapterItems = new ArrayAdapter<String>(this, R.layout.gender_list_item, dataArray);
        inputLocation.setAdapter(adapterItems);

        inputLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                //Toast.makeText(getApplicationContext(), "Item: " + item, Toast.LENGTH_SHORT);
            }
        });

        initMap();

        mLocationClient = LocationServices.getFusedLocationProviderClient(this) ;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrenLoc();
            }
        });

        //client = LocationServices.getFusedLocationProviderClient(this);

        /*if(ActivityCompat.checkSelfPermission(Map2.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            getCurrentLocation();
        }else{
            ActivityCompat.requestPermissions(Map2.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }*/



        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = inputLocation.getText().toString();
                if (location == null) {
                    Toast.makeText(Map2.this, "Type any location name", Toast.LENGTH_SHORT).show();

                } else {

                    Geocoder geocoder = new Geocoder(Map2.this, Locale.getDefault());
                    try {
                        List<Address> listAddress = geocoder.getFromLocationName(location, 1);
                        if (listAddress.size() > 0) {
                            //googleMap.clear();
                            LatLng latLng = new LatLng(listAddress.get(0).getLatitude(), listAddress.get(0).getLongitude());


                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.title(location);
                            markerOptions.position(latLng);
                            mgoogleMap.addMarker(markerOptions);
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 140);
                            mgoogleMap.animateCamera(cameraUpdate);
                            //Toast.makeText(Map2.this, latLng.toString(), Toast.LENGTH_SHORT).show();

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder.setTitle("Confirm!")
                        .setMessage("Did you confirmed your location?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String location = inputLocation.getText().toString();
                                if (location == null) {
                                    Toast.makeText(Map2.this, "Choose a pickup location", Toast.LENGTH_SHORT).show();

                                } else {

                                    Geocoder geocoder = new Geocoder(Map2.this, Locale.getDefault());
                                    try {
                                        List<Address> listAddress = geocoder.getFromLocationName(location, 1);
                                        if (listAddress.size() > 0) {
                                            //googleMap.clear();

                                            LatLng latLng = new LatLng(listAddress.get(0).getLatitude(), listAddress.get(0).getLongitude());

                                            double lat = latLng.latitude;
                                            double lng = latLng.longitude;

//                                            userref = FirebaseDatabase.getInstance().getReference().child("users").child(driverKey1);
//                                            userref.addValueEventListener(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                    UserHelperClass event = snapshot.getValue(UserHelperClass.class);
//                                                    pendings = event.getPending();
//                                                    pendings++;
//
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError error) {
//                                                    //Toast.makeText(UserProfile.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                                                }
//                                            });
                                            userref2.child(driverKey1).child("pending").setValue(ServerValue.increment(1));

                                            int count =0;
                                            bookeddriver bookeddriver = new bookeddriver(key1, namez1, data_gender1, phonez1, lectevent1,location , driverKey1, uid1,driverkeyid1, carsid1, count1, lat, lng);
                                            requestref.child(key1).setValue(bookeddriver);

                                            //calculation




                                            Intent intent = new Intent(Map2.this, View_Activity.class);
                                            /*intent.putExtra("driverKey", driver_uid);
                                            tent.putExtra("lectevent", driver_event);
                                            inintent.putExtra("driveridkey", driverkey_id);
                                            intent.putExtra("carid", car_id);*/
                                            intent.putExtra("lectevent",lectevent1);
                                            Toast.makeText(Map2.this, "Successfully Request a Driver!", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                            //finish();

                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();

            }
        });

    }

    private void initMap() {
        mapFragment= (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
    }

    private void getCurrenLoc() {
        mLocationClient.getLastLocation().addOnCompleteListener(task -> {

            if(task.isSuccessful()){
                Location location = task.getResult();
                gotoLocation(location.getLatitude(),location.getLongitude());

            }

        });
    }

    private void gotoLocation(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude,longitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(latitude,longitude));
        markerOptions.title("maps");
        mgoogleMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,18);
        mgoogleMap.moveCamera(cameraUpdate);
        mgoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }

    /*private void getCurrentLocation() {
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location !=null){
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

                            MarkerOptions options = new MarkerOptions().position(latLng)
                                    .title("I am there");

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                            googleMap.addMarker(options);
                        }
                    });
                }
            }
        });
    }*/

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 44){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }*/

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mgoogleMap = googleMap;
        mgoogleMap.setMyLocationEnabled(true);
        //drawPolyline();
        getCurrenLoc();
        /*
        LatLng latLng = new LatLng(45.262041, -104.434847);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("My Position");
        markerOptions.position(latLng);
        googleMap.addMarker(markerOptions);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 5);
        googleMap.animateCamera(cameraUpdate);*/

        return;
    }

    /*private void drawPolyline(GoogleMap map) {
        // Create a list of points that define the polyline
        List<LatLng> points = new ArrayList<>();
        points.add(new LatLng(37.7749, -122.4194));
        points.add(new LatLng(37.7665, -122.4192));
        points.add(new LatLng(37.7580, -122.4180));
        points.add(new LatLng(37.7530, -122.4178));

        // Create the polyline and add it to the map
        PolylineOptions options = new PolylineOptions()
                .width(5)
                .color(Color.RED)
                .geodesic(true);
        options.addAll(points);
        Polyline polyline = map.addPolyline(options);
    }*/
    /*private void drawDirections(GoogleMap map, LatLng origin, LatLng destination) {
        // Add a marker at the origin and destination locations
        map.addMarker(new MarkerOptions().position(origin));
        map.addMarker(new MarkerOptions().position(destination));

        // Use the Directions API to get the route between the origin and destination
        DirectionsApiRequest request = DirectionsApi.getDirections(context, origin.latitude + "," + origin.longitude, destination.latitude + "," + destination.longitude);
        try {
            DirectionsResult result = request.await();

            // Add a polyline to the map for each leg of the route
            for (int i = 0; i < result.routes.length; i++) {
                DirectionsRoute route = result.routes[i];
                List<LatLng> decodedPath = PolyUtil.decode(route.overviewPolyline.getEncodedPath());
                map.addPolyline(new PolylineOptions().addAll(decodedPath));
            }
        } catch (Exception e) {
            // Handle any errors
        }
    }*/
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void checkmyPermission(){
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                //Toast.makeText(Map2.this,"Permission Granted",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",getPackageName(),null);
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }
}