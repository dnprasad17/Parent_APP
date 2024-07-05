package com.example.parent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ComponentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Map extends AppCompatActivity implements OnMapReadyCallback {
    FusedLocationProviderClient mFusedLocationClient;
    TextView latitudeTextView;
    int PERMISSION_ID = 44;
    Button confirm;
    TextInputLayout address;
    TextInputLayout local;
    TextInputLayout stage;
    TextInputLayout landmark;
    public static String Coordinates;
    public static TextInputLayout Flat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_map);


        address = findViewById(R.id.address);
        local = findViewById(R.id.local);
        stage = findViewById(R.id.Stage);
        landmark = findViewById(R.id.Landmark);
        confirm = findViewById(R.id.confirm_button);
       // back = findViewById(R.id.back_button);
        String phone_number= getIntent().getStringExtra("mobile");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((com.google.android.gms.maps.OnMapReadyCallback) this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Address = address.getEditText().getText().toString();
                String Local = local.getEditText().getText().toString();
                String Stage = stage.getEditText().getText().toString();
                String Landmark = landmark.getEditText().getText().toString();
                if(Address.equals("")){
                    Toast.makeText(Map.this,"Please fill uo adddress space",Toast.LENGTH_LONG).show();
                }else if(Local.equals("")){
                    Toast.makeText(Map.this,"Please Fill up Locality Page",Toast.LENGTH_LONG).show();
                } else if(Stage.equals("")){
                    Toast.makeText(Map.this,"Please Fill up the Stage part",Toast.LENGTH_LONG).show();
                } else if(Landmark.equals("")){
                    Toast.makeText(Map.this,"Please fill up Landmark Area",Toast.LENGTH_LONG).show();
                }
                 else {
//                     String fatherName=getIntent().getStringExtra("fatherName");
//                     String motherName=getIntent().getStringExtra("motherName");
//                     String studentName=getIntent().getStringExtra("studentName");
//                     String schoolName=getIntent().getStringExtra("schoolName");
//                    intent.putExtra("fatherName",fatherName);
//                    intent.putExtra("motherName",motherName);
//                    intent.putExtra("studentName",studentName);
//                    intent.putExtra("schoolName",schoolName);
//                    intent.putExtra("schoolName",schoolName);
                    Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                    intent.putExtra("Address",Address);
                    intent.putExtra("Local",Local);
                    intent.putExtra("Stage",Stage);
                    intent.putExtra("Landmark",Landmark);
                    intent.putExtra("Coordinates",Coordinates);
                    intent.putExtra("mobile",phone_number);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });

    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            Coordinates = ("" + location.getLatitude() + ", " + location.getLongitude());

                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            Coordinates = ("" + mLastLocation.getLatitude() + ", " + mLastLocation.getLongitude());
        }
    };

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }


    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync((com.google.android.gms.maps.OnMapReadyCallback) this);
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync((com.google.android.gms.maps.OnMapReadyCallback) this);
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        GoogleMap mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                mMap.clear();
                MarkerOptions marker = new MarkerOptions().position(new LatLng(point.latitude, point.longitude)).title("Your Location");
                if (ActivityCompat.checkSelfPermission(Map.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Map.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                mMap.setMyLocationEnabled(false);
                marker.position(new LatLng(point.latitude, point.longitude));
                mMap.addMarker(marker);
                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(point.latitude, point.longitude));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(11);
                mMap.moveCamera(center);
                mMap.animateCamera(zoom);
                Coordinates = ("" + point.latitude + ", " + point.longitude);
            }
        });
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(11);
                mMap.clear();
                MarkerOptions mp = new MarkerOptions();
                mp.position(new LatLng(location.getLatitude(), location.getLongitude()));
                mp.title("Your Location");
                mMap.addMarker(mp);
                mMap.moveCamera(center);
                mMap.animateCamera(zoom);
            }

        });

//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), UserProfile.class);
//                startActivity(intent);
//            }
//        });
    }


}