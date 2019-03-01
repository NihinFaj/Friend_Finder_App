package com.example.friend_finder;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.util.Log;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String kentURL = "https://www.cs.kent.ac.uk/people/staff/iau/LocalUsers.php";
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        new GetFriends().execute(kentURL);
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

        Log.i("Nihin In the onMapReady ",mMap.toString());

        // Add a marker over The Senate Building and also fetch and Display Locations of Friends
        LatLng senate = new LatLng(51.299005, 1.069871);
        mMap.addMarker(new MarkerOptions().position(senate).title("Senate Building University of Kent"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(senate, 15));

        // Allows zooming in and out of the Map, using the built-in zoom controls that appear in the bottom right hand corner of the map
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Allows zooming in and out using two fingers
        mMap.getUiSettings().setZoomGesturesEnabled(true);

      }
}
