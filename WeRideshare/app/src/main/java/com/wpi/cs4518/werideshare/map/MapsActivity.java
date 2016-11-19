package com.wpi.cs4518.werideshare.map;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wpi.cs4518.werideshare.R;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    Marker currLocationMarker;
    private static final int REQUEST_FINE_LOCATION=0;
    Button mReFocusButton;
    ImageButton mGetCurrentLocationButton;
    LatLng currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadPermissions(Manifest.permission.ACCESS_FINE_LOCATION,REQUEST_FINE_LOCATION);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mReFocusButton = (Button) findViewById(R.id.reFocus);
        mReFocusButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
            if(currentLocation != null)
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

            }
        });

        mGetCurrentLocationButton = (ImageButton) findViewById(R.id.getCurrentLocation);
        mGetCurrentLocationButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                int permissionCheck = ContextCompat.checkSelfPermission(MapsActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if(permissionCheck==PackageManager.PERMISSION_GRANTED) {
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                            mGoogleApiClient);
                    if (mLastLocation != null) {
                        currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(currentLocation).title("current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                    }

                    mLocationRequest = new LocationRequest();
                    mLocationRequest.setInterval(5000); //5 seconds
                    mLocationRequest.setFastestInterval(3000); //3 seconds
                    mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MapsActivity.this);

                }
            }
        });
    }
    private void loadPermissions(String perm,int requestCode) {
        if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
//            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                requestPermissions(new String[]{perm},requestCode);
//            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    int permissionCheck = ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION);
                    if(permissionCheck==PackageManager.PERMISSION_GRANTED) {
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                mGoogleApiClient);
                        if (mLastLocation != null) {
                            currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(currentLocation).title("current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

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
                }

                else{
                    Toast.makeText(this,"no permission was given for fine grain location", Toast.LENGTH_LONG ).show();
                }
                return;
            }

            default:
                Toast.makeText(this,"no permission was given for fine grain location", Toast.LENGTH_LONG ).show();

        }

    }

//    @Override
//    protected void onResume(){
//        int permissionCheck = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION);
//        if(permissionCheck==PackageManager.PERMISSION_GRANTED && mGoogleApiClient.isConnected()) {
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//        }
//
//        super.onResume();
//    }

    @Override
    protected void onStop(){
        if(mGoogleApiClient!=null && mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        super.onStop();
    }

    @Override
    protected void onDestroy(){
        if(mGoogleApiClient.isConnected())
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
        if(permissionCheck==1) {
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
                    .build();

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck==PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(currentLocation).title("current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

            }
            else{
                Toast.makeText(this,"Please turn on your GPS", Toast.LENGTH_LONG).show();
                Intent gpsOptionsIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(gpsOptionsIntent);
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
        Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }

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
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        Toast.makeText(this,"Location Updated",Toast.LENGTH_SHORT).show();



        //If you only need one location, unregister the listener
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }


}
