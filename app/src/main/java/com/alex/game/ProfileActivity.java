package com.alex.game;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private TextView emailV;
    private TextView nameV;
    private TextView gpsV;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.singn_out).setOnClickListener(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        emailV = findViewById(R.id.email_setView);
        nameV = findViewById(R.id.name_setV);
        gpsV = findViewById(R.id.GPS_V);

        emailV.setText(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());

    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 * 10, 10, locationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @SuppressLint("MissingPermission")
        @Override
        public void onProviderEnabled(String provider) {
            showLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onProviderDisabled(String provider) {

        }

    };

    private void showLocation(Location location) {
        if (location == null)
            return;
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            gpsV.setText(formatLocation(location));
        }
    }

    @SuppressLint("DefaultLocale")
    private String formatLocation(Location location) {
        if (location == null)
            return "";
        return String.format(
                "lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
                location.getLatitude(), location.getLongitude(), new Date(
                        location.getTime()));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.singn_out) {
            mAuth.signOut();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }
}
