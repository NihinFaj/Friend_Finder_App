package com.example.friend_finder;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.os.AsyncTask;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.util.Log;
import org.json.JSONException;
import org.json.*;

public class GetFriends extends AsyncTask<String, Void, Void> {
    private String json;
    private static final String LOG_TAG = "FriendFinderApp";
    private String convertedJSON;
    private GoogleMap nMap;

    // Call onPreExecute Method
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    // doInBackground Method
    @Override
    protected Void doInBackground(String... urls) {
        try {
            HttpURLConnection conn = null;
            final StringBuilder json = new StringBuilder();
            try {
                // Connect to the web service
                URL url = new URL(urls[0]);
                Log.i("Nihin", "About to establish connection");
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(conn.getInputStream());

                // Read the JSON data into the StringBuilder
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    json.append(buff, 0, read);
                    convertedJSON = json.toString();
                    Log.i("Nihin", "Established connection successfully");
                    Log.i("Nihin Data Received", convertedJSON);
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error connecting to service", e);
                throw new IOException("Error connecting to service", e);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            Log.e(LOG_TAG,"doInBackground method entered an exception" , ex);
        }
        return null;
    }

    // onPostExecute Method
    @Override
    protected void onPostExecute(Void unused) {
        try {
            Log.i("Nihin", "Just entered onPostExecute method successfully");
            // Call displayFriendsOnMap Method which displays Markers on the Map
//            nMap =
            displayFriendsOnMap();
        }
        catch (JSONException ex) {
            ex.printStackTrace();
            Log.e(LOG_TAG,"onPostExecute method entered an exception" , ex);
        }

    }

    // displayFriendsOnMap method which displays Friend Markers on the Map
    protected void displayFriendsOnMap()
            throws JSONException {
        Log.i("Nihin", "Just entered displayFriendsOnMap method successfully");

        //SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
          //      .getMap();

        JSONObject jsonObject = new JSONObject(convertedJSON);
        JSONArray jsonArray = jsonObject.getJSONArray("Users");

        Log.i("Nihin: Array Before Loop is ",jsonArray.toString());

        for(int i = 0; i < jsonArray.length(); i++) {
            // Create marker for each city in the JSON data
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            Log.i("Nihin An Object in the Array is ",jsonObj.toString());
            Log.i("Nihin ","-----------------------------------------------------");
            Log.i("Nihin, Name is ",jsonObj.getString("name" ));
            Log.i("Nihin, Latitude is ",jsonObj.getString("lat"));
            Log.i("Nihin, Longitude is ",jsonObj.getString("lon" ));

            String markerTitle = jsonObj.getString("name");
            double lat = Double.parseDouble(jsonObj.getString("lat"));
            double lon = Double.parseDouble(jsonObj.getString("lon"));

            LatLng friend = new LatLng(lat, lon);
            Log.i("Nihin In the displayFriendsOnMap ", nMap.toString());

            nMap.addMarker(new MarkerOptions().position(friend).title(markerTitle));

//            Marker locations = googleMap.addMarker(new MarkerOptions()
//                    .position(local)
//                    .title(TAG_NAME)
//                    .snippet(TAG_VILLE)
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.gps)));
        }
    }
}
