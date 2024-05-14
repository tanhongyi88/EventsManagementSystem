package com.fit2081.assignment1;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.fit2081.assignment1.databinding.ActivityGoogleMapsBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityGoogleMapsBinding binding;
    private SupportMapFragment mapFragment;
    private Geocoder geocoder;

    private String location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGoogleMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        geocoder = new Geocoder(this, Locale.getDefault());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // get string from the intent
        location = getIntent().getStringExtra("locationName");
        Log.d("locationToSearchInGoogleMaps", "location: " + location);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng locationOnMap = new LatLng(3.1319,101.6841);
        List<Address> nameToAddressList = new ArrayList<>();

        if (location == null || location.isEmpty()) {
            location = "KualaLumpur";
        }

        try {
            nameToAddressList = geocoder.getFromLocationName(location, 1);
        } catch (IOException e) {
            Log.d("GoogleMapsLocationError", "Unable to get location for the given location name");
        }

        if(nameToAddressList != null && !nameToAddressList.isEmpty()) {
            Log.d("AddressFound", nameToAddressList.toString());
            locationOnMap = new LatLng(nameToAddressList.get(0).getLatitude(), nameToAddressList.get(0).getLongitude());
        }

        // Set the map type to Hybrid.
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Add a marker on the map coordinates.
        if (nameToAddressList != null && !nameToAddressList.isEmpty()) {
            mMap.addMarker(new MarkerOptions()
                    .position(locationOnMap)
                    .title("This is " + nameToAddressList.get(0).getAddressLine(0)));
        } else {
            mMap.addMarker(new MarkerOptions()
                    .position(locationOnMap)
                    .title("This is Kuala Lumpur, Malaysia"));
        }

        // Move the camera to the map coordinates and zoom in closer.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(locationOnMap));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(10));

        if (nameToAddressList.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Category address not found.", Toast.LENGTH_SHORT).show();
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){

            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                String msg;

                List<Address> latlongToAddressList = new ArrayList<>();

                try {
                    latlongToAddressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                } catch (IOException e) {
                    Log.d("GoogleMapsError", "Unable to get lat and long");
                }

                if(latlongToAddressList.isEmpty())
                    msg = "Sorry! No address available at this location!";
                else{
                    android.location.Address address = latlongToAddressList.get(0);
                    msg = "The address is " + address.getAddressLine(0);

                    mMap.addMarker(new MarkerOptions().position(latLng));
                }
                Snackbar.make(mapFragment.getView(), msg, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}