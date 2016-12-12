package com.wpi.cs4518.werideshare.model;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.wpi.cs4518.werideshare.MapsActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by Will Wen on 12/6/2016.
 *
 * Most of this code is from :
 * https://stackoverflow.com/questions/14444228/android-how-to-draw-route-directions-google-maps-api-v2-from-current-location-t
 */


public class GMapV2Direction extends AsyncTask {
    public final static String MODE_DRIVING = "driving";
    public final static String MODE_WALKING = "walking";
    private final static String googleMapsDirectionsAPI = "AIzaSyBQ_MyP_rlp5rhDoQJQpWFS_k5WyMR9m1Q";
    public JSONObject jsonObj;

    GoogleMap mapController;

    public GMapV2Direction(GoogleMap mapController) {
        this.mapController = mapController;
    }


    @Override
    protected JSONObject doInBackground(Object[] objects) {
        return getJSONResponse((LatLng)objects[0], (LatLng)objects[1]);
    }

    @Override
    protected void onPostExecute(Object o) {
        addJSONPath((JSONObject)o);
    }


    public JSONObject getJSONResponse(LatLng start, LatLng end) {
        String url = "https://maps.googleapis.com/maps/api/directions/json?"
                + "origin=" + start.latitude + "," + start.longitude
                + "&destination=" + end.latitude + "," + end.longitude
                + "&units=metric&mode=driving&key=" + googleMapsDirectionsAPI;
        Log.d("url", url);
        try {
            URL urlObj = new URL(url);

            HttpsURLConnection urlConnection = (HttpsURLConnection) urlObj.openConnection();

            InputStream in =  new BufferedInputStream(urlConnection.getInputStream());
            jsonObj = new JSONObject(getStringFromInputStream(in));
            return jsonObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    // convert InputStream to String
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }


    public void addJSONPath(JSONObject obj){
        PolylineOptions rectLine = new PolylineOptions().width(10).color(
                Color.RED);
        try {
            JSONArray steps = obj.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
            for (int i = 0 ; i < steps.length() ; i++ ){
                LatLng nextLocation;
                JSONObject endLocation = steps.getJSONObject(i).getJSONObject("end_location");
                double lat = endLocation.getDouble("lat");
                double lng = endLocation.getDouble("lng");
                nextLocation = new LatLng (lat,lng);
                rectLine.add(nextLocation);

            }
            Polyline polylin = mapController.addPolyline(rectLine);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}