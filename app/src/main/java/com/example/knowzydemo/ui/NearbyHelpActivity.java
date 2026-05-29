package com.example.knowzydemo.ui;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.knowzydemo.R;

public class NearbyHelpActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST = 44;

    private String pendingQuery;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_help);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnPolice).setOnClickListener(v ->
                openNearbySearch("police station"));
        findViewById(R.id.btnHospital).setOnClickListener(v ->
                openNearbySearch("hospital"));
    }

    private void openNearbySearch(String query) {
        pendingQuery = query;
        if (!hasLocationPermission()) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST);
            return;
        }

        openWithBestLocation(query);
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void openWithBestLocation(String query) {
        Location location = getLastKnownLocation();
        if (location != null) {
            openMapsAtLocation(query, location.getLatitude(), location.getLongitude());
            return;
        }

        requestFreshLocation(query);
    }

    private Location getLastKnownLocation() {
        if (!hasLocationPermission() || locationManager == null) return null;

        Location bestLocation = null;
        String[] providers = {
                LocationManager.GPS_PROVIDER,
                LocationManager.NETWORK_PROVIDER,
                LocationManager.PASSIVE_PROVIDER
        };

        for (String provider : providers) {
            try {
                Location location = locationManager.getLastKnownLocation(provider);
                if (location == null) continue;
                if (bestLocation == null
                        || location.getTime() > bestLocation.getTime()) {
                    bestLocation = location;
                }
            } catch (SecurityException | IllegalArgumentException ignored) {
            }
        }
        return bestLocation;
    }

    private void requestFreshLocation(String query) {
        if (!hasLocationPermission() || locationManager == null) {
            openMapsNearUser(query);
            return;
        }

        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                try {
                    locationManager.removeUpdates(this);
                } catch (SecurityException ignored) {
                }
                openMapsAtLocation(query, location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                openMapsNearUser(query);
            }
        };

        try {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestSingleUpdate(
                        LocationManager.GPS_PROVIDER,
                        listener,
                        getMainLooper());
                Toast.makeText(this, "Getting your current location...", Toast.LENGTH_SHORT).show();
                return;
            }

            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestSingleUpdate(
                        LocationManager.NETWORK_PROVIDER,
                        listener,
                        getMainLooper());
                Toast.makeText(this, "Getting your current location...", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (SecurityException ignored) {
        }

        openMapsNearUser(query);
    }

    private void openMapsAtLocation(String query, double latitude, double longitude) {
        Uri geoUri = Uri.parse("geo:" + latitude + "," + longitude
                + "?q=" + Uri.encode(query));
        Uri browserUri = Uri.parse("https://www.google.com/maps/search/?api=1&query="
                + Uri.encode(query)
                + "&center=" + latitude + "," + longitude);
        openMapsIntent(geoUri, browserUri, query);
    }

    private void openMapsNearUser(String query) {
        Uri geoUri = Uri.parse("geo:0,0?q=" + Uri.encode("nearby " + query));
        Uri browserUri = Uri.parse("https://www.google.com/maps/search/?api=1&query="
                + Uri.encode("nearby " + query));
        openMapsIntent(geoUri, browserUri, query);
    }

    private void openMapsIntent(Uri geoUri, Uri browserUri, String query) {
        Intent googleMapsIntent = new Intent(Intent.ACTION_VIEW, geoUri);
        googleMapsIntent.setPackage("com.google.android.apps.maps");

        try {
            startActivity(googleMapsIntent);
            return;
        } catch (ActivityNotFoundException ignored) {
        }

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, geoUri));
            return;
        } catch (ActivityNotFoundException ignored) {
        }

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, browserUri));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(
                    this,
                    "Install Google Maps or a browser to find nearby " + query + ".",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != LOCATION_PERMISSION_REQUEST || pendingQuery == null) return;

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openWithBestLocation(pendingQuery);
        } else {
            Toast.makeText(
                    this,
                    "Location permission denied. Opening Maps nearby search instead.",
                    Toast.LENGTH_LONG).show();
            openMapsNearUser(pendingQuery);
        }
    }
}
