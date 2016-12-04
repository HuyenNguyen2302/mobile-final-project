package com.wpi.cs4518.werideshare.map;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.wpi.cs4518.werideshare.R;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    //used for logging
    private static final String TAG = "MapsActivity";

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private Marker currLocationMarker;
    private static final int REQUEST_FINE_LOCATION = 0;
    private ImageButton mGetCurrentLocationButton;
    private LatLng currentLocation;
    private boolean mRequestingLocationUpdates;

    private PlaceAutocompleteFragment sourceSearch;
    private PlaceAutocompleteFragment destinationSearch;
    private LatLng source;
    private Marker sourceMarker;
    private LatLng destination;
    private Marker destinationMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadPermissions(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_FINE_LOCATION);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //get buttons and UI widgets
        mGetCurrentLocationButton = (ImageButton) findViewById(R.id.getCurrentLocation);

        sourceSearch = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.source_autocomplete_fragment);
        sourceSearch.getView().setBackgroundColor(0x80FFFFFF);

        destinationSearch = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.destination_autocomplete_fragment);
        destinationSearch.getView().setBackgroundColor(0x80FFFFFF);

        registerUI();

    }
    private void registerUI(){
        mGetCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (checkLocationPermission()) {
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                            mGoogleApiClient);
                    if (mLastLocation != null) {
                        currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                        sourceMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                    }
                }
            }
        });
        sourceSearch.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                //Log.i(TAG, "Place: " + place.getName());
                sourceMarker.remove();
                source = place.getLatLng();
                sourceMarker = mMap.addMarker(new MarkerOptions().position(source).title("Source").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

                mMap.moveCamera(CameraUpdateFactory.newLatLng(source));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        destinationSearch.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                if (destinationMarker != null)
                    destinationMarker.remove();
                destination = place.getLatLng();
                destinationMarker = mMap.addMarker(new MarkerOptions().position(destination).title("Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

                //calc bounds to move camera to capture both markers
                //http://stackoverflow.com/questions/14828217/android-map-v2-zoom-to-show-all-the-markers
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (Marker marker : new Marker[]{sourceMarker, destinationMarker}) {
                    builder.include(marker.getPosition());
                }
                LatLngBounds bounds = builder.build();
                int padding = 40; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mMap.animateCamera(cu);


                //draw a path between two markers
                PolylineOptions polylineOptions = new PolylineOptions()
                        .add(source).add(destination).geodesic(true).width(5); // Point B.

                Polyline polyline = mMap.addPolyline(polylineOptions);

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }
    private void loadPermissions(String perm, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
//            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
            requestPermissions(new String[]{perm}, requestCode);
//            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkLocationPermission()) {
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                mGoogleApiClient);
                        if (mLastLocation != null) {
                            currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                            sourceMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                        }

                        mLocationRequest = new LocationRequest();
                        mLocationRequest.setInterval(5000); //5 seconds
                        mLocationRequest.setFastestInterval(3000); //3 seconds
                        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

                    }
                } else {
                    Toast.makeText(this, "no permission was given for fine grain location", Toast.LENGTH_LONG).show();
                }
                return;
            }

            default:
                Toast.makeText(this, "no permission was given for fine grain location", Toast.LENGTH_LONG).show();

        }

    }

    @Override
    protected void onStop() {

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();

        super.onDestroy();
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
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == 1) {
            mMap.setMyLocationEnabled(true);
        }
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(Places.GEO_DATA_API)
                .build();

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                sourceMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

            } else {
                Toast.makeText(this, "Please turn on your GPS", Toast.LENGTH_LONG).show();
                Intent gpsOptionsIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(gpsOptionsIntent);
            }

            //ONLY IF WE CONSTANTLY WANT THE LOCATION
//            mLocationRequest = new LocationRequest();
//            mLocationRequest.setInterval(5000); //5 seconds
//            mLocationRequest.setFastestInterval(3000); //3 seconds
//            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//            //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter
//
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        }


    }



    //Will be called when the location updates returns a new location
    @Override
    public void onLocationChanged(Location location) {

        //place marker at current position
        //mGoogleMap.clear();
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
//        if(location == null){
//            Toast.makeText(this, "Are you sure you turned on GPS?", Toast.LENGTH_SHORT).show();
//            return;
//        }
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        sourceMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
        Toast.makeText(this, "Location Updated", Toast.LENGTH_SHORT).show();
        //If you only need one location, unregister the listener

    }


    private void registerLocationRequest() {
        if(checkLocationPermission()) {
            mLocationRequest.setInterval(5000); //5 seconds
            mLocationRequest.setFastestInterval(3000); //3 seconds
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MapsActivity.this);
            mRequestingLocationUpdates = true;
        }
    }
    private void unregisterLocationRequest(){
        if(mRequestingLocationUpdates) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mRequestingLocationUpdates = false;
        }
    }

    private boolean checkLocationPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "onConnectionSuspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();
    }
}
