package com.example.friend_finder;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONException;
import org.json.*;

/**
 * A Map App that shows my location, fetches other locations from a server and displays them all on the Map
 *
 * @author Nihinlolamiwa Fajemilehin
 * @version 2019.03.01
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String kentURL = "https://www.cs.kent.ac.uk/people/staff/iau/LocalUsers.php";
    private static final String LOG_TAG = "FriendFinderApp";
    private GoogleMap mMap;
    private String convertedJSON;

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

        // Add a marker over The Senate Building and also fetch and Display Locations of Friends
        LatLng senate = new LatLng(51.299005, 1.069871);
        mMap.addMarker(new MarkerOptions().position(senate).title("Senate Building University of Kent"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(senate, 15));

        // Allows zooming in and out of the Map, using the built-in zoom controls that appear in the bottom right hand corner of the map
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Allows zooming in and out using two fingers
        mMap.getUiSettings().setZoomGesturesEnabled(true);
      }

    /**
     *
     */

    public class GetFriends extends AsyncTask<String, Void, Void> {

        /**
         * Call onPreExecute Method
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * doInBackground Method, that makes connection to the server and fetches data
         * @param urls
         * @return Void Object
         */
        @Override
        protected Void doInBackground(String... urls) {
            try {
                HttpURLConnection conn = null;
                final StringBuilder json = new StringBuilder();
                try {
                    // Connect to the web service
                    URL url = new URL(urls[0]);
                    conn = (HttpURLConnection) url.openConnection();
                    InputStreamReader in = new InputStreamReader(conn.getInputStream());

                    // Read the JSON data into the StringBuilder
                    int read;
                    char[] buff = new char[1024];
                    while ((read = in.read(buff)) != -1) {
                        json.append(buff, 0, read);
                        convertedJSON = json.toString();
                    }
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error connecting to service", e);
                    throw new IOException("Error connecting to service", e);
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e(LOG_TAG, "doInBackground method entered an exception", ex);
            }
            return null;
        }

        /**
         *  onPostExecute Method that is called after all background process is complete
          */
        @Override
        protected void onPostExecute(Void unused) {
            try {
                // Call displayFriendsOnMap Method which displays Markers on the Map
                displayFriendsOnMap();
            } catch (JSONException ex) {
                ex.printStackTrace();
                Log.e(LOG_TAG, "onPostExecute method entered an exception", ex);
            }

        }

        /**
         * displayFriendsOnMap method which displays Friend Markers on the Map
         * @throws JSONException
         */
        protected void displayFriendsOnMap()
                throws JSONException {

            JSONObject jsonObject = new JSONObject(convertedJSON);
            JSONArray jsonArray = jsonObject.getJSONArray("Users");

            for (int i = 0; i < jsonArray.length(); i++) {
                // Create marker for each city in the JSON data
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                Log.i("Nihin An Object in the Array is ", jsonObj.toString());
                Log.i("Nihin ", "-----------------------------------------------------");
                Log.i("Nihin, Name is ", jsonObj.getString("name"));
                Log.i("Nihin, Latitude is ", jsonObj.getString("lat"));
                Log.i("Nihin, Longitude is ", jsonObj.getString("lon"));

                String markerTitle = jsonObj.getString("name");
                double lat = Double.parseDouble(jsonObj.getString("lat"));
                double lon = Double.parseDouble(jsonObj.getString("lon"));

                LatLng friend = new LatLng(lat, lon);
                Log.i("Nihin In the displayFriendsOnMap ", mMap.toString());

                mMap.addMarker(new MarkerOptions().position(friend).title(markerTitle));
            }
        }
    }
}