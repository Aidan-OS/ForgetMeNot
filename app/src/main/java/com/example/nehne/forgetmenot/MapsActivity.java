package com.example.nehne.forgetmenot;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

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

import android.Manifest;
import android.widget.TextView;


import java.io.*;

import static com.example.nehne.forgetmenot.R.layout.activity_maps;
import static com.google.android.gms.common.api.GoogleApiClient.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
    ConnectionCallbacks,
    OnConnectionFailedListener,
    LocationListener{


    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private GoogleMap mMap;
    public static Location mLastLocation;
    private Marker mCurrLocationMarker;
    public static LinkList listOfGeofences = new LinkList ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Generates list from memory
        RandomAccessFile raf = null;
        try
        {
            raf = new RandomAccessFile (new File(getFilesDir(), "locations.bin"), "rw");

            int numberOfGeofences = raf.readInt ();

            if (raf == null)
            {
                //Nothing in the file, nothing to load
            }

            else
            {
                for (int i = 0; i < numberOfGeofences; i++)
                {
                    byte[] byteName = new byte[16];
                    raf.read(byteName);
                    String name = new String(byteName, 0);

                    String firstLetter = name.substring(0, 1);
                    firstLetter.toUpperCase();
                    String afterFirstLetter = (name.substring(1));

                    name = firstLetter;
                    name += afterFirstLetter;

                    name = name.trim();

                    double radius = raf.readDouble();
                    double longitude = raf.readDouble();
                    double lattitude = raf.readDouble();
                    int minutes = raf.readInt();

                    double currentLat = mLastLocation.getLatitude();
                    double currentLong = mLastLocation.getLongitude();

                    GeoFence temp = new GeoFence(name, radius, longitude, lattitude, currentLong, currentLat, minutes);
                    listOfGeofences.addNode(temp);

                }
            }
            raf.close();
        }
        catch (java.io.IOException e)
        {
            //Dont know why it thinks this is needed, but for some reason it thinks the file wont exist when it is making it.
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }



    }

    protected synchronized void buildGoogleApiClient (){
        mGoogleApiClient = new GoogleApiClient.Builder (this)
                .addConnectionCallbacks (this)
                .addOnConnectionFailedListener (this)
                .addApi (LocationServices.API)
                .build ();
        mGoogleApiClient.connect ();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
        public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        updateMap();


        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        }

    public void createMapMarker (double lat, double lng, String name)
    {
        LatLng latLng = new LatLng (lat, lng);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position (latLng);
        markerOptions.title (name);
        markerOptions.icon (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
    }


    public void addButtonPressed (View v)
    {
        Intent i = new Intent (getApplicationContext(), AddView.class);
        startActivityForResult (i, 1);
    }

    public void settingsButtonPressed (View v)
    {
        Intent i = new Intent (getApplicationContext (), SettingsView.class);
        startActivityForResult (i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            updateMap();
        }
    }

    public void updateMap ()
    {
        GeoFence temp;
        do
        {
            temp = listOfGeofences.getTop ();

            if (temp == null)
            {
                break;
            }

            else
            {
                createMapMarker (temp.getLatitude(), temp.getLongitude(), temp.getName());
            }

        }while (temp.getNext () != null);
    }




}
