package com.example.nehne.forgetmenot;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
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
import android.util.Log;

import android.Manifest;


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
    private Location mLastLocation;
    private AidanGeoFenceLinkList listOfGeofences = new AidanGeoFenceLinkList ();
    private boolean cameraZoomedOnStartup = false;
    private boolean initRefresh = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        MultiDex.install(this);

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

                    name = name.trim();

                    double radius = raf.readDouble();
                    double longitude = raf.readDouble();
                    double lattitude = raf.readDouble();
                    int minutes = raf.readInt();


                    AidanGeoFence temp = new AidanGeoFence(name, radius, longitude, lattitude, minutes);
                    listOfGeofences.addNode(temp);

                }
            }
            raf.close();
        }
        catch (java.io.IOException e)
        {
            //Dont know why it thinks this is needed, but for some reason it thinks the file wont exist when it is making it.
        }

        /*
        AidanGeoFence test = new AidanGeoFence("Aidan's House", 5.0, -79.7298385, 43.4465535, 1);
        AidanGeoFence saul = new AidanGeoFence("Saul's House", 5.0, 0, 0, 1);
        AidanGeoFence currentLocationAddForTest = new AidanGeoFence("Oh god please", 0.5, -79.7335124, 43.4445535, 1);
        listOfGeofences.addNode(test);
        listOfGeofences.addNode(saul);
        listOfGeofences.addNode(currentLocationAddForTest);*/


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

        //////////LETTING THE MAP SEE THAT YOU ARE TOUCHING THE POINTS
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {
                String[] latlng;
                latlng = marker.getSnippet().split(", ");

                double lat = Double.parseDouble(latlng [0]);
                double lng = Double.parseDouble(latlng [1]);

                AidanGeoFence editingGeoFence = listOfGeofences.searchLocation(lng, lat);
                String editingName = editingGeoFence.getName();
                double editingRadius = editingGeoFence.getRadius();
                int editingTime = editingGeoFence.getTime();

                //starts the edit view
                Intent i = new Intent (getApplicationContext(), EditView.class);
                i.putExtra ("name", editingName);
                i.putExtra ("radius", editingRadius);
                i.putExtra ("lng", lng);
                i.putExtra ("lat", lat);
                i.putExtra ("time", editingTime);
                startActivityForResult (i, 2);

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                //DONT CARE HERE
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                //DONT CARE ON THIS
            }
        });
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
        //Start Notifications
        startService(new Intent(this, NotificationService.class));
        Log.e ("Maps Activity", "I have started the Notification Service");
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

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (!initRefresh)
        {
            refreshMap();
            initRefresh = true;
        }

        //move map camera on startup
        if (!cameraZoomedOnStartup) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            cameraZoomedOnStartup = true;
        }

        //stop location updates
        if (mGoogleApiClient == null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public void createMapMarker (double lat, double lng, String name)
    {
            LatLng latLng = new LatLng(lat, lng);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(name);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            markerOptions.draggable(true);
            markerOptions.snippet("" + lat + ", " + lng);
            mMap.addMarker(markerOptions);
    }


    public void addButtonPressed (View v)
    {
        Intent i = new Intent (getApplicationContext(), AddView.class);
        startActivityForResult (i, 1);
    }

    public void settingsButtonPressed (View v)
    {
        Intent i = new Intent (getApplicationContext (), SettingsView.class);
        startActivityForResult (i, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Check which request we're responding to

        //RETURNING FROM ADD
        if (requestCode == 1 && Activity.RESULT_OK == resultCode) {
            String name = (String) data.getSerializableExtra("name");
            double radius = (double) data.getSerializableExtra("radius");
            double latitude = (double) data.getSerializableExtra("lat");
            double longitude = (double) data.getSerializableExtra("lng");
            int time = (int) data.getSerializableExtra("time");

            AidanGeoFence temp = new AidanGeoFence(name, radius, latitude, longitude, time);
            listOfGeofences.addNode(temp);
        }

        //IF WAS EDITING
        if (requestCode == 2 && resultCode == Activity.RESULT_OK){
            boolean wasDeleted = (boolean) data.getSerializableExtra("deleted");
            Log.e ("Deleting off of an edit", "I am deleting node: " + wasDeleted);

            //IF ARENT CHANGING, JUST REMOVING
            if (wasDeleted == true)
            {
                double latitude = (double) data.getSerializableExtra("lat");
                double longitude = (double) data.getSerializableExtra("lng");
                listOfGeofences.deleteNode(latitude, longitude);


            }

            //IF ARE CHANGING
            else
            {
                String name = (String) data.getSerializableExtra("name");
                double radius = (double) data.getSerializableExtra("radius");
                double latitude = (double) data.getSerializableExtra("lat");
                double longitude = (double) data.getSerializableExtra("lng");
                int time = (int) data.getSerializableExtra("time");

                double absLatitude = (double) data.getSerializableExtra("absLat");
                double absLongitude = (double) data.getSerializableExtra("absLng");

                listOfGeofences.deleteNode(absLatitude, absLongitude);

                AidanGeoFence temp = new AidanGeoFence(name, radius, longitude, latitude, time);
                listOfGeofences.addNode(temp);
            }
        }

        //RETURNING FROM SETTINGS VIEW
        if (requestCode == 3 && resultCode == Activity.RESULT_OK){
            int whatToDo = (int) data.getSerializableExtra("doThis");

            if (whatToDo == 0)
            {
                listOfGeofences.clear ();
            }
        }

        cameraZoomedOnStartup = false;
        saveData();
        refreshMap();
    }

    public void refreshMap ()
    {
        //CLEARING THE MAP
        mMap.clear();


        //Regenerating all the markers
        AidanGeoFence temp;
        temp = listOfGeofences.getTop ();

        if (temp != null){

            while (temp.getNextGeoFence () != null)
            {

                if (temp == null)
                {
                    break;
                }

                else
                {
                    createMapMarker (temp.getLatitude(), temp.getLongitude(), temp.getName());
                    Log.e("Update Map", "I am adding a geofence to the map. Its name is: " + temp.getName());
                }

                temp = temp.getNextGeoFence();
            }


            if (temp != null) {
                createMapMarker(temp.getLatitude(), temp.getLongitude(), temp.getName());
                Log.e("Update Map", "I am adding a geofence to the map. Its name is: " + temp.getName());
            }
        }
    }

    public void saveData ()
    {
        RandomAccessFile raf;
        try
        {
            raf = new RandomAccessFile (new File(getFilesDir(), "locations.bin"), "rw");
            int numberOfGeofences = listOfGeofences.linkListLength();

            raf.writeInt(numberOfGeofences);

            if (numberOfGeofences > 0)
            {
                AidanGeoFence currentFence = listOfGeofences.getTop();
                for (int i = 0; i < numberOfGeofences; i++) {

                    byte[] byteName = new byte[16];
                    String name = currentFence.getName();

                    name.getBytes(0, name.length(), byteName, 0);

                    double radius = currentFence.getRadius();
                    double longitude = currentFence.getLongitude();
                    double latitude = currentFence.getLatitude();
                    int minutes = currentFence.getTime();

                    raf.write (byteName);
                    raf.writeDouble (radius);
                    raf.writeDouble (longitude);
                    raf.writeDouble (latitude);
                    raf.writeInt (minutes);

                    currentFence = currentFence.getNextGeoFence();
                }

                raf.close();
            }
        }
        catch (java.io.IOException e)
        {
            //Dont know why it thinks this is needed, but for some reason it thinks the file wont exist when it is making it.
        }
    }




}
