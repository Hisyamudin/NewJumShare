package com.example.newjumshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newjumshare.databinding.ActivityMaps2Binding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class mappassenger3 extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{

    private GoogleMap mMap;
    private ActivityMaps2Binding binding;
    String keydrivers, driverkeyid, ids, driverkey, item1, Location, destination1, destination2, destination3, destination4, destination5, destination6, url;
    int num_pass;
    private FusedLocationProviderClient mLocationClient;
    LocationRequest locationRequest;
    DatabaseReference ongoingref, ridestartref,delref,ongoingdelref, userref2;
    //android.location.Location location;
    String latstart, lngstart, driveruserid,uid;
    double latz;
    Button btn_arrived;

    //test current location
    private GoogleApiClient mGoogleApiClient;
    private android.location.Location mlocation, location;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 15000;
    private long FASTEST_INTERVAL = 18000;
    private LocationManager locationManager;
    private LatLng latLng;
    private boolean ispermission;
    androidx.appcompat.app.AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mappassenger3);
        driveruserid = getIntent().getStringExtra("driveruserid");
        builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        keydrivers = getIntent().getStringExtra("ID");
        driverkeyid = getIntent().getStringExtra("driverkeyid");
        ids = getIntent().getStringExtra("ids");
        Location = getIntent().getStringExtra("Location");
        driverkey = getIntent().getStringExtra("driverskey");
        num_pass = getIntent().getIntExtra("num_pass", 0);
//        destination1 = getIntent().getStringExtra("destination1");
//        destination2 = getIntent().getStringExtra("destination2");
//        destination3 = getIntent().getStringExtra("destination3");
//        destination4 = getIntent().getStringExtra("destination4");
//        destination5 = getIntent().getStringExtra("destination5");
//        destination6 = getIntent().getStringExtra("destination6");
        ongoingref = FirebaseDatabase.getInstance().getReference("ongoing ride");
        ridestartref = FirebaseDatabase.getInstance().getReference("ongoing ride");
        mLocationClient = LocationServices.getFusedLocationProviderClient(this);
        btn_arrived = findViewById(R.id.btnarrived);
        //delref = FirebaseDatabase.getInstance().getReference("ongoing ride");
        ongoingdelref = FirebaseDatabase.getInstance().getReference().child("ongoing ride");
        userref2 = FirebaseDatabase.getInstance().getReference("users");

        if (requestSinglePermission()) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            checkLocation();
        }

        btn_arrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("Ending the session")
                        .setMessage("By clicking Arrived button, It means that you are arrived at your desired location. Are you sure to end this session?")
                        .setCancelable(true)
                        .setPositiveButton("End Session", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                userref2.child(uid).child("passengernum").setValue(ServerValue.increment(-1));

                                ongoingdelref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            for (DataSnapshot snapshots : snapshot.getChildren()) {
                                                //bookedDriversClas event = snapshots.getValue(bookedDriversClas.class);
                                                String empty = "";

                                                delref = FirebaseDatabase.getInstance().getReference().child("ongoing ride").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                delref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Intent intent = new Intent(mappassenger3.this,FeedbackActivity.class);
                                                        intent.putExtra("keydrivers", driveruserid);

                                                        startActivity(intent);
                                                    }
                                                });
//                                                delref2 = FirebaseDatabase.getInstance().getReference().child("drivers_data").child(event.getDriver_id());
//                                                delref2.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                    @Override
//                                                    public void onSuccess(Void unused) {
//                                                        //startActivity(new Intent(getApplicationContext(),HomeScreen.class));
//                                                        //Toast.makeText(requestActivity.this, "Successful Accept the Driver!", Toast.LENGTH_SHORT).show();
//
//                                                    }
//                                                });
                                            }
                                        } else {
                                            //Toast.makeText(HomeScreen.this, "", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

//                                Intent intent = new Intent(mappassenger3.this,FeedbackActivity.class);
//                                intent.putExtra("keydrivers", driveruserid);
//
//                                startActivity(intent);

//                                delref=FirebaseDatabase.getInstance().getReference().child("request_booking").child(key);
//                                delref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        startActivity(new Intent(getApplicationContext(),HomeScreen.class));
//                                    }
//                                });
                            }
                        })
                        .setNegativeButton("Don't End Session", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
            }
        });
    }

    private boolean checkLocation() {

        if (!isLocationisEnabled()) {
            showAlert();
        }
        return isLocationisEnabled();
    }

    private void showAlert() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'\n Please Enable Location  use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();

    }

    private boolean isLocationisEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean requestSinglePermission() {

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        ispermission = true;
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        if (permissionDeniedResponse.isPermanentlyDenied()) {
                            ispermission = false;
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();

        return ispermission;

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(new LatLng(6.0351810, 116.1212355))
                .include(new LatLng(6.030479231849159, 116.1176959)).build();
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, point.x, 350, 30));

        //getCurrenLoc();
        /*if (latLng != null) {
            mMap.addMarker(new MarkerOptions().position(latLng).title("CurrentLocation"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14F));
        }*/

        //drawPolyline();

    }

     /* mLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        locationRequest = LocationRequest.create();
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        locationRequest.setFastestInterval(5000);
                        locationRequest.setFastestInterval(3000);
                        Toast.makeText(Maps2.this,"Location"+locationResult.getLastLocation().getLatitude()+": "+locationResult.getLastLocation().getLongitude()+"",Toast.LENGTH_SHORT).show();
                        direction(num_pass, String.valueOf(locationResult.getLastLocation().getLatitude()), String.valueOf(locationResult.getLastLocation().getLongitude()));
                    }
                }, Looper.getMainLooper());*/


    private void getCurrenLoc() {
        mLocationClient.getLastLocation().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                location = task.getResult();
                //direction(num_pass, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
               // direction(num_pass, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));

                /*Geocoder geocoder = new Geocoder(Maps2.this, Locale.getDefault());
                try {
                    List<Address> listAddressdest = geocoder.getFromLocationName(Location, 1);

                    if (listAddressdest.size() > 0) {
                        //googleMap.clear();
                        LatLng latLngdest = new LatLng(listAddressdest.get(0).getLatitude(), listAddressdest.get(0).getLongitude());
                        latz = listAddressdest.get(0).getLatitude();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                boolean found = false;
                while(!found) {
                    direction(num_pass, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));

                    if(location.getLatitude() == latz){
                        found = true;
                    }*/
                // }
                //anroid.location.Location location = task.getResult();
                //gotoLocation(location.getLatitude(),location.getLongitude());
            }

        });
    }

//.appendQueryParameter("waypoints", "-6.9249233,107.6345122")
    //.appendQueryParameter("origin", "-6.9249233,107.6345122")
    //ref.orderByChild("childKey").ascending().get()                    //////////////////PENTING

    private void direction(int num_pass, String latstart, String lngstart,String destination1,String destination2, String destination3,String destination4,String destination5, String destination6) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        if (num_pass == 1) {
            url = Uri.parse("https://maps.googleapis.com/maps/api/directions/json")
                    .buildUpon()
                    .appendQueryParameter("destination", Location)
                    .appendQueryParameter("origin", latstart + "," + lngstart)
                    .appendQueryParameter("waypoints", destination1)
                    .appendQueryParameter("mode", "driving")
                    .appendQueryParameter("key", "AIzaSyB-wFbjf2_m_q_TtXarglFSBBkACjtLzws")
                    .toString();

            Geocoder geocoder = new Geocoder(mappassenger3.this, Locale.getDefault());
            try {
                List<Address> listAddress = geocoder.getFromLocationName(destination1, 1);
                List<Address> listAddressdest = geocoder.getFromLocationName(Location, 1);

                if (listAddress.size() > 0) {
                    //googleMap.clear();
                    LatLng latLng = new LatLng(listAddress.get(0).getLatitude(), listAddress.get(0).getLongitude());
                    LatLng latLngdest = new LatLng(listAddressdest.get(0).getLatitude(), listAddressdest.get(0).getLongitude());

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.title(destination1);
                    markerOptions.position(latLng);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1));
                    mMap.addMarker(markerOptions);

                    MarkerOptions markerOptionsdest = new MarkerOptions();
                    markerOptionsdest.title(Location);
                    markerOptionsdest.position(latLngdest);
                    markerOptionsdest.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                    mMap.addMarker(markerOptionsdest);


                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (num_pass == 2) {
            url = Uri.parse("https://maps.googleapis.com/maps/api/directions/json")
                    .buildUpon()
                    .appendQueryParameter("destination", Location)
                    .appendQueryParameter("origin", latstart + "," + lngstart)
                    .appendQueryParameter("waypoints", destination1 + "|" + destination2)
                    .appendQueryParameter("mode", "driving")
                    .appendQueryParameter("key", "AIzaSyB-wFbjf2_m_q_TtXarglFSBBkACjtLzws")
                    .toString();

            Geocoder geocoder = new Geocoder(mappassenger3.this, Locale.getDefault());
            try {
                List<Address> listAddress = geocoder.getFromLocationName(destination1, 1);
                List<Address> listAddress2 = geocoder.getFromLocationName(destination2, 1);
                List<Address> listAddressdest = geocoder.getFromLocationName(Location, 1);

                if (listAddress.size() > 0) {
                    //googleMap.clear();
                    LatLng latLng = new LatLng(listAddress.get(0).getLatitude(), listAddress.get(0).getLongitude());
                    LatLng latLng2 = new LatLng(listAddress2.get(0).getLatitude(), listAddress2.get(0).getLongitude());
                    LatLng latLngdest = new LatLng(listAddressdest.get(0).getLatitude(), listAddressdest.get(0).getLongitude());

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.title(destination1);
                    markerOptions.position(latLng);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1));

                    MarkerOptions markerOptions2 = new MarkerOptions();
                    markerOptions2.title(destination2);
                    markerOptions2.position(latLng2);
                    markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker2));

                    MarkerOptions markerOptionsdest = new MarkerOptions();
                    markerOptionsdest.title(Location);
                    markerOptionsdest.position(latLngdest);
                    markerOptionsdest.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                    mMap.addMarker(markerOptions);
                    mMap.addMarker(markerOptions2);
                    mMap.addMarker(markerOptionsdest);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (num_pass == 3) {
            url = Uri.parse("https://maps.googleapis.com/maps/api/directions/json")
                    .buildUpon()
                    .appendQueryParameter("destination", Location)
                    .appendQueryParameter("origin", "6.030479231849159, 116.1176959")
                    .appendQueryParameter("waypoints", destination1 + "|" + destination2 + "|" + destination3)
                    .appendQueryParameter("mode", "driving")
                    .appendQueryParameter("key", "AIzaSyB-wFbjf2_m_q_TtXarglFSBBkACjtLzws")
                    .toString();

            Geocoder geocoder = new Geocoder(mappassenger3.this, Locale.getDefault());
            try {
                List<Address> listAddress = geocoder.getFromLocationName(destination1, 1);
                List<Address> listAddress2 = geocoder.getFromLocationName(destination2, 1);
                List<Address> listAddress3 = geocoder.getFromLocationName(destination3, 1);
                List<Address> listAddressdest = geocoder.getFromLocationName(Location, 1);

                if (listAddress.size() > 0) {
                    //googleMap.clear();
                    LatLng latLng = new LatLng(listAddress.get(0).getLatitude(), listAddress.get(0).getLongitude());
                    LatLng latLng2 = new LatLng(listAddress2.get(0).getLatitude(), listAddress2.get(0).getLongitude());
                    LatLng latLng3 = new LatLng(listAddress3.get(0).getLatitude(), listAddress3.get(0).getLongitude());
                    LatLng latLngdest = new LatLng(listAddressdest.get(0).getLatitude(), listAddressdest.get(0).getLongitude());

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.title(destination1);
                    markerOptions.position(latLng);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1));

                    MarkerOptions markerOptions2 = new MarkerOptions();
                    markerOptions2.title(destination2);
                    markerOptions2.position(latLng2);
                    markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker2));

                    MarkerOptions markerOptions3 = new MarkerOptions();
                    markerOptions3.title(destination3);
                    markerOptions3.position(latLng3);
                    markerOptions3.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker3));

                    MarkerOptions markerOptionsdest = new MarkerOptions();
                    markerOptionsdest.title(Location);
                    markerOptionsdest.position(latLngdest);
                    markerOptionsdest.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                    mMap.addMarker(markerOptions);
                    mMap.addMarker(markerOptions2);
                    mMap.addMarker(markerOptions3);
                    mMap.addMarker(markerOptionsdest);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (num_pass == 4) {
            url = Uri.parse("https://maps.googleapis.com/maps/api/directions/json")
                    .buildUpon()
                    .appendQueryParameter("destination", Location)
                    .appendQueryParameter("origin", "6.030479231849159, 116.1176959")
                    .appendQueryParameter("waypoints", destination1 + "|" + destination2 + "|" + destination3 + "|" + destination4)
                    .appendQueryParameter("mode", "driving")
                    .appendQueryParameter("key", "AIzaSyB-wFbjf2_m_q_TtXarglFSBBkACjtLzws")
                    .toString();

            Geocoder geocoder = new Geocoder(mappassenger3.this, Locale.getDefault());
            try {
                List<Address> listAddress = geocoder.getFromLocationName(destination1, 1);
                List<Address> listAddress2 = geocoder.getFromLocationName(destination2, 1);
                List<Address> listAddress3 = geocoder.getFromLocationName(destination3, 1);
                List<Address> listAddress4 = geocoder.getFromLocationName(destination4, 1);
                List<Address> listAddressdest = geocoder.getFromLocationName(Location, 1);

                if (listAddress.size() > 0) {
                    //googleMap.clear();
                    LatLng latLng = new LatLng(listAddress.get(0).getLatitude(), listAddress.get(0).getLongitude());
                    LatLng latLng2 = new LatLng(listAddress2.get(0).getLatitude(), listAddress2.get(0).getLongitude());
                    LatLng latLng3 = new LatLng(listAddress3.get(0).getLatitude(), listAddress3.get(0).getLongitude());
                    LatLng latLng4 = new LatLng(listAddress4.get(0).getLatitude(), listAddress4.get(0).getLongitude());
                    LatLng latLngdest = new LatLng(listAddressdest.get(0).getLatitude(), listAddressdest.get(0).getLongitude());

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.title(destination1);
                    markerOptions.position(latLng);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1));

                    MarkerOptions markerOptions2 = new MarkerOptions();
                    markerOptions2.title(destination2);
                    markerOptions2.position(latLng2);
                    markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker2));

                    MarkerOptions markerOptions3 = new MarkerOptions();
                    markerOptions3.title(destination3);
                    markerOptions3.position(latLng3);
                    markerOptions3.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker3));

                    MarkerOptions markerOptions4 = new MarkerOptions();
                    markerOptions4.title(destination4);
                    markerOptions4.position(latLng4);
                    markerOptions4.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker4));

                    MarkerOptions markerOptionsdest = new MarkerOptions();
                    markerOptionsdest.title(Location);
                    markerOptionsdest.position(latLngdest);
                    markerOptionsdest.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                    mMap.addMarker(markerOptions);
                    mMap.addMarker(markerOptions2);
                    mMap.addMarker(markerOptions3);
                    mMap.addMarker(markerOptions4);
                    mMap.addMarker(markerOptionsdest);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (num_pass == 5) {
            url = Uri.parse("https://maps.googleapis.com/maps/api/directions/json")
                    .buildUpon()
                    .appendQueryParameter("destination", Location)
                    .appendQueryParameter("origin", "6.030479231849159, 116.1176959")
                    .appendQueryParameter("waypoints", destination1 + "|" + destination2 + "|" + destination3 + "|" + destination4 + "|" + destination5)
                    .appendQueryParameter("mode", "driving")
                    .appendQueryParameter("key", "AIzaSyB-wFbjf2_m_q_TtXarglFSBBkACjtLzws")
                    .toString();

            Geocoder geocoder = new Geocoder(mappassenger3.this, Locale.getDefault());
            try {
                List<Address> listAddress = geocoder.getFromLocationName(destination1, 1);
                List<Address> listAddress2 = geocoder.getFromLocationName(destination2, 1);
                List<Address> listAddress3 = geocoder.getFromLocationName(destination3, 1);
                List<Address> listAddress4 = geocoder.getFromLocationName(destination4, 1);
                List<Address> listAddress5 = geocoder.getFromLocationName(destination5, 1);
                List<Address> listAddressdest = geocoder.getFromLocationName(Location, 1);

                if (listAddress.size() > 0) {
                    //googleMap.clear();
                    LatLng latLng = new LatLng(listAddress.get(0).getLatitude(), listAddress.get(0).getLongitude());
                    LatLng latLng2 = new LatLng(listAddress2.get(0).getLatitude(), listAddress2.get(0).getLongitude());
                    LatLng latLng3 = new LatLng(listAddress3.get(0).getLatitude(), listAddress3.get(0).getLongitude());
                    LatLng latLng4 = new LatLng(listAddress4.get(0).getLatitude(), listAddress4.get(0).getLongitude());
                    LatLng latLng5 = new LatLng(listAddress5.get(0).getLatitude(), listAddress5.get(0).getLongitude());
                    LatLng latLngdest = new LatLng(listAddressdest.get(0).getLatitude(), listAddressdest.get(0).getLongitude());

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.title(destination1);
                    markerOptions.position(latLng);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1));

                    MarkerOptions markerOptions2 = new MarkerOptions();
                    markerOptions2.title(destination2);
                    markerOptions2.position(latLng2);
                    markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker2));

                    MarkerOptions markerOptions3 = new MarkerOptions();
                    markerOptions3.title(destination3);
                    markerOptions3.position(latLng3);
                    markerOptions3.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker3));

                    MarkerOptions markerOptions4 = new MarkerOptions();
                    markerOptions4.title(destination4);
                    markerOptions4.position(latLng4);
                    markerOptions4.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker4));

                    MarkerOptions markerOptions5 = new MarkerOptions();
                    markerOptions5.title(destination5);
                    markerOptions5.position(latLng5);
                    markerOptions5.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker5));

                    MarkerOptions markerOptionsdest = new MarkerOptions();
                    markerOptionsdest.title(Location);
                    markerOptionsdest.position(latLngdest);
                    markerOptionsdest.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                    mMap.addMarker(markerOptions);
                    mMap.addMarker(markerOptions2);
                    mMap.addMarker(markerOptions3);
                    mMap.addMarker(markerOptions4);
                    mMap.addMarker(markerOptions5);
                    mMap.addMarker(markerOptionsdest);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (num_pass == 6) {
            url = Uri.parse("https://maps.googleapis.com/maps/api/directions/json")
                    .buildUpon()
                    .appendQueryParameter("destination", Location)
                    .appendQueryParameter("origin", "6.030479231849159, 116.1176959")
                    .appendQueryParameter("waypoints", destination1 + "|" + destination2 + "|" + destination3 + "|" + destination4 + "|" + destination5 + "|" + destination6)
                    .appendQueryParameter("mode", "driving")
                    .appendQueryParameter("key", "AIzaSyB-wFbjf2_m_q_TtXarglFSBBkACjtLzws")
                    .toString();

            Geocoder geocoder = new Geocoder(mappassenger3.this, Locale.getDefault());
            try {
                List<Address> listAddress = geocoder.getFromLocationName(destination1, 1);
                List<Address> listAddress2 = geocoder.getFromLocationName(destination2, 1);
                List<Address> listAddress3 = geocoder.getFromLocationName(destination3, 1);
                List<Address> listAddress4 = geocoder.getFromLocationName(destination4, 1);
                List<Address> listAddress5 = geocoder.getFromLocationName(destination5, 1);
                List<Address> listAddress6 = geocoder.getFromLocationName(destination5, 1);
                List<Address> listAddressdest = geocoder.getFromLocationName(Location, 1);

                if (listAddress.size() > 0) {
                    //googleMap.clear();
                    LatLng latLng = new LatLng(listAddress.get(0).getLatitude(), listAddress.get(0).getLongitude());
                    LatLng latLng2 = new LatLng(listAddress2.get(0).getLatitude(), listAddress2.get(0).getLongitude());
                    LatLng latLng3 = new LatLng(listAddress3.get(0).getLatitude(), listAddress3.get(0).getLongitude());
                    LatLng latLng4 = new LatLng(listAddress4.get(0).getLatitude(), listAddress4.get(0).getLongitude());
                    LatLng latLng5 = new LatLng(listAddress5.get(0).getLatitude(), listAddress5.get(0).getLongitude());
                    LatLng latLng6 = new LatLng(listAddress6.get(0).getLatitude(), listAddress6.get(0).getLongitude());
                    LatLng latLngdest = new LatLng(listAddressdest.get(0).getLatitude(), listAddressdest.get(0).getLongitude());

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.title(destination1);
                    markerOptions.position(latLng);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1));

                    MarkerOptions markerOptions2 = new MarkerOptions();
                    markerOptions2.title(destination2);
                    markerOptions2.position(latLng2);
                    markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker2));

                    MarkerOptions markerOptions3 = new MarkerOptions();
                    markerOptions3.title(destination3);
                    markerOptions3.position(latLng3);
                    markerOptions3.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker3));

                    MarkerOptions markerOptions4 = new MarkerOptions();
                    markerOptions4.title(destination4);
                    markerOptions4.position(latLng4);
                    markerOptions4.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker4));

                    MarkerOptions markerOptions5 = new MarkerOptions();
                    markerOptions5.title(destination5);
                    markerOptions5.position(latLng5);
                    markerOptions5.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker5));

                    MarkerOptions markerOptions6 = new MarkerOptions();
                    markerOptions6.title(destination6);
                    markerOptions6.position(latLng6);
                    markerOptions6.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker6));

                    MarkerOptions markerOptionsdest = new MarkerOptions();
                    markerOptionsdest.title(Location);
                    markerOptionsdest.position(latLngdest);
                    markerOptionsdest.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                    mMap.addMarker(markerOptions);
                    mMap.addMarker(markerOptions2);
                    mMap.addMarker(markerOptions3);
                    mMap.addMarker(markerOptions4);
                    mMap.addMarker(markerOptions5);
                    mMap.addMarker(markerOptions6);
                    mMap.addMarker(markerOptionsdest);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("OK")) {
                        JSONArray routes = response.getJSONArray("routes");
                        ArrayList<LatLng> points;
                        PolylineOptions polylineOptions = null;

                        for (int i = 0; i < routes.length(); i++) {
                            points = new ArrayList<>();
                            polylineOptions = new PolylineOptions();
                            JSONArray legs = routes.getJSONObject(i).getJSONArray("legs");

                            for (int j = 0; j < legs.length(); j++) {
                                JSONArray steps = legs.getJSONObject(j).getJSONArray("steps");

                                for (int k = 0; k < steps.length(); k++) {
                                    String polyline = steps.getJSONObject(k).getJSONObject("polyline").getString("points");
                                    List<LatLng> list = decodePolyLine(polyline);

                                    for (int l = 0; l < list.size(); l++) {
                                        LatLng position = new LatLng((list.get(l)).latitude, (list.get(l)).longitude);
                                        points.add(position);

                                    }

                                }
                            }
                            polylineOptions.addAll(points);
                            polylineOptions.width(10);
                            polylineOptions.color(ContextCompat.getColor(mappassenger3.this, R.color.purple_500));
                            polylineOptions.geodesic(true);
                            //mMap.addMarker(new MarkerOptions().position(new LatLng(6.0351810, 116.1212355)).title("Marker 1"));
                        }
                        mMap.addPolyline(polylineOptions);
                        //mMap.addMarker(new MarkerOptions().position(new LatLng(6.0351810, 116.1212355)).title("Marker 1"));
                        //mMap.addMarker(new MarkerOptions().position(new LatLng(6.030479231849159, 116.1176959)).title("Marker 1"));



                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RetryPolicy retryPolicy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(jsonObjectRequest);
    }


    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }

    private void checkmyPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Toast.makeText(mappassenger3.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startLocationUpdates();
        mlocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mlocation == null) {
            startLocationUpdates();
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    private void startLocationUpdates() {

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location locationz) {
        //direction(num_pass, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
        mMap.clear();

        latLng = new LatLng(locationz.getLatitude(), locationz.getLongitude());
        //direction(num_pass, String.valueOf(locationz.getLatitude()), String.valueOf(locationz.getLongitude()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
        mLocationClient.getLastLocation().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                location = task.getResult();
                //direction(num_pass, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                ongoingref.orderByChild("driverkey").equalTo(keydrivers).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshots : snapshot.getChildren()) {
                                Ongoingrideclass event = snapshots.getValue(Ongoingrideclass.class);
                                String empty = "";
                                mMap.clear();

                                destination1 = event.getLocation1();
                                destination2 = event.getLocation2();
                                destination3 = event.getLocation3();
                                destination4 = event.getLocation4();
                                destination5 = event.getLocation5();
                                destination6 = event.getLocation6();

                                //ridestartref.child(event.getPassengerkey()).child("currentlat").setValue(location.getLatitude());
                                //ridestartref.child(event.getPassengerkey()).child("currentlng").setValue(location.getLongitude());

                                direction(num_pass, String.valueOf(event.getCurrentlat()), String.valueOf(event.getCurrentlng()),destination1,destination2,destination3,destination4,destination5,destination6);
                                LatLng latzlngz = new LatLng(event.getCurrentlat(),event.getCurrentlng());
                                MarkerOptions markerOptions2 = new MarkerOptions();
                                markerOptions2.title("Driver");
                                markerOptions2.position(latzlngz);
                                markerOptions2.anchor((float) 0.5,(float) 0.5);
                                markerOptions2.rotation(location.getBearing());
                                markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.carimage));
                                mMap.addMarker(markerOptions2);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

        });
        //latsz =location.getLatitude();
        //lngsz = location.getLongitude();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }
}

