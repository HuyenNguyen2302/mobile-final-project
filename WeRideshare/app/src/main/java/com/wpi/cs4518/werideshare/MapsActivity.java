package com.wpi.cs4518.werideshare;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
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
import com.wpi.cs4518.werideshare.model.GMapV2Direction;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    //for logging purposes
    private static final String TAG = "MapsActivity";

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private Marker currLocationMarker;
    private static final int REQUEST_FINE_LOCATION = 0;
    private LatLng currentLocation;

    private Location sourceLocation;
    private Marker sourceLocationMarker;
    private Location destinationLocation;
    private Marker destinationLocationMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadPermissions(REQUEST_FINE_LOCATION);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        ImageButton mGetCurrentLocationButton = (ImageButton) findViewById(R.id.getCurrentLocation);
        mGetCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(MapsActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                            mGoogleApiClient);
                    if (mLastLocation != null) {
                        currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(currentLocation).title("current WeRideShareLocation").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                    }
                }
            }
        });
        setupPlaceAutoCompleteFragmentListeners();

    }

    private void setupPlaceAutoCompleteFragmentListeners() {
        PlaceAutocompleteFragment sourceAutocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.source_autocomplete_fragment);
        PlaceAutocompleteFragment destinationAutocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.destination_autocomplete_fragment);

        sourceAutocompleteFragment.setText("Source Search");
        destinationAutocompleteFragment.setText("Destination Search");
        sourceAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName());
                //when you select a new source, remove the current source marker
                if(sourceLocationMarker != null)
                    sourceLocationMarker.remove();
                sourceLocation = new Location(LocationManager.GPS_PROVIDER);
                sourceLocation.setLatitude(place.getLatLng().latitude);
                sourceLocation.setLongitude(place.getLatLng().longitude);
                //add this new source location marker
                sourceLocationMarker = mMap.addMarker(new MarkerOptions().position(
                        new LatLng(sourceLocation.getLatitude(),sourceLocation.getLongitude()))
                        .title("Source").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                moveCameraCaptureMarkers(new Marker[]{sourceLocationMarker});


            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        destinationAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName());
                //when you select a new source, remove the current source marker
                if(destinationLocationMarker!=null)
                    destinationLocationMarker.remove();
                destinationLocation = new Location(LocationManager.GPS_PROVIDER);
                destinationLocation.setLatitude(place.getLatLng().latitude);
                destinationLocation.setLongitude(place.getLatLng().longitude);
                //add this new source location marker
                destinationLocationMarker = mMap.addMarker(new MarkerOptions().position(
                        new LatLng(destinationLocation.getLatitude(),destinationLocation.getLongitude()))
                        .title("Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                if(sourceLocationMarker == null)
                    moveCameraCaptureMarkers(new Marker[]{destinationLocationMarker});
                else {
                    //animate camera to capture both locations:
                    moveCameraCaptureMarkers(new Marker[]{sourceLocationMarker, destinationLocationMarker});



                    //need <uses-permission android:name="android.permission.INTERNET"/>
                    //and GMAP does network operations so to avoid network on main thread exception, we spawn a thread
                    GMapV2Direction gdir = new GMapV2Direction(mMap);
                    LatLng src = new LatLng(sourceLocation.getLatitude(),sourceLocation.getLongitude());
                    LatLng des = new LatLng(destinationLocation.getLatitude(),destinationLocation.getLongitude());
                    gdir.execute(src ,des);


                }
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    private void loadPermissions(int requestCode) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MapsActivity.REQUEST_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    int permissionCheck = ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                mGoogleApiClient);
                        if (mLastLocation != null) {
                            currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(currentLocation).title("current WeRideShareLocation").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

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
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        checkIfGPSOn();
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                //mark current location
                currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

                currLocationMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("current WeRideShareLocation").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                //make the source initially the current location
                sourceLocation = mLastLocation;
                sourceLocationMarker = currLocationMarker;

            }


            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(5000); //5 seconds
            mLocationRequest.setFastestInterval(3000); //3 seconds
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        }


    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "onConnectionSuspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {

        //place marker at current position
        //mGoogleMap.clear();
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }

        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current WeRideShareLocation").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        //Toast.makeText(this, "Current WeRideShareLocation Updated", Toast.LENGTH_SHORT).show();


        //If you only need one location, unregister the listener
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }

    /**
     * Checks if GPS is enabled on the android device
     * if it isnt, it prompts the user , and changes screen to the GPS enable page
     */
    private void checkIfGPSOn() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please turn on your GPS", Toast.LENGTH_LONG).show();
            Intent gpsOptionsIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionsIntent);
        }

    }

    /**
     * Moves and animates the camera to capture all the markers after a user has selected source and destination.
     * Code Credit Thanks to :
     * http://stackoverflow.com/questions/14828217/android-map-v2-zoom-to-show-all-the-markers
     */
    private void moveCameraCaptureMarkers(Marker[] markers){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 50; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
    }

}
