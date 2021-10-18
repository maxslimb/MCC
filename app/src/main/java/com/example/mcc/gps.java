package com.example.mcc;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class gps extends AppCompatActivity {
    private String TAG = "GPS ACTIVITY";
    private FusedLocationProviderClient fusedLocationClient;
    private Button btnShowLocation;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        btnShowLocation = (Button) findViewById(R.id.show_Location);

        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                Log.d(TAG, "both permissions granted");
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                Log.d(TAG, "both permissions not granted");
                            } else {
                                Log.d(TAG, "NO permissions granted");
                            }
                        }
                );
        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        btnShowLocation.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "11both permissions granted");
                return;
            }
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(gps.this, location -> {

                        if (location != null) {
                            Log.d("loaction-lat", String.valueOf(location.getLatitude()));
                            Log.d("loaction-long", String.valueOf(location.getLongitude()));
                            String latitude = String.valueOf(location.getLatitude());
                            String longitude = String.valueOf(location.getLongitude());
                            Toast.makeText(getApplicationContext(), "Your Location is:\n Lat: " + latitude + "\n Long: " + longitude, Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}