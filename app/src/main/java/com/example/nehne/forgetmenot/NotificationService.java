package com.example.nehne.forgetmenot;

import android.*;
import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.Serializable;

/**
 * Created by Aidan Smitb on 2017-01-09.
 */

public class NotificationService extends Service implements Serializable, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static AidanGeoFenceLinkList notificationServiceListOfFences = new AidanGeoFenceLinkList ();
    private static RandomAccessFile raf = null;
    private boolean inGeofence = false;
    private long whenGeoFenceEntered = 0;
    private int currentGracePeriod = 0;
    private LocationRequest nLocationRequest;

    private GoogleApiClient nGoogleApiClient = null;
    @Override
    public IBinder onBind (Intent intent) {
        return null;
    }

    //CREATES AN INSTANCE OF THE FILE
    @Override
    public void onCreate (){
        super.onCreate();

        Log.e ("Notification Service", "I'm Starting!");

    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        nLocationRequest = new LocationRequest();
        nLocationRequest.setInterval(1000);
        nLocationRequest.setFastestInterval(1000);
        nLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(nGoogleApiClient, nLocationRequest, this);
        }
    }

    protected synchronized void nbuildGoogleApiClient ()
    {
        if (nGoogleApiClient == null) {
            nGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            nGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //WHEN FILE IS STARTED, THIS RUNS
    @Override
    public int onStartCommand (Intent intent, int flags, int startId){

        Log.e ("Notification Service", "My onStartCommand has Run");

        loadAllGeoFences();

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                nbuildGoogleApiClient();
            }
        }
        else {
            nbuildGoogleApiClient();
        }

        return super.onStartCommand(intent, flags, startId);
    }


    private void checkAndNotify () {

        if (nGoogleApiClient != null) {
            //If you were in a geofence
            if (inGeofence) {
                //Gets location
                Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(nGoogleApiClient);

                double geoLat;
                double geoLong;

                long currentTime = System.currentTimeMillis();

                //sees if you are still in any geofences
                AidanGeoFence currentGeofence = notificationServiceListOfFences.getTop();
                while (currentGeofence != null) {

                    geoLat = currentGeofence.getLatitude();
                    geoLong = currentGeofence.getLongitude();
                    Location fenceLocation = new Location("");
                    fenceLocation.setLatitude(geoLat);
                    fenceLocation.setLongitude(geoLong);

                    Log.e("Notification Service", "Distance to point " + currentGeofence.getName() + " is : " + currentLocation.distanceTo(fenceLocation));

                    if (currentLocation.distanceTo(fenceLocation) <= currentGeofence.getRadius()) {

                        inGeofence = true;
                        break;
                    } else {
                        inGeofence = false;
                    }

                    currentGeofence = currentGeofence.getNextGeoFence();
                }

                //If not in any geofences, alert must be made
                if (inGeofence == false && (currentTime - whenGeoFenceEntered) * 6000 >= currentGracePeriod) {
                    notifyAboutItem();
                }
            }

            //If wasnt in a geofence before
            else {
                //get location
                Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(nGoogleApiClient);

                //Co-ordinates off the map, nothing can happen with these
                double geoLat;
                double geoLong;


                //Check if now in a geofence, and set the variable for next time to be true
                AidanGeoFence currentGeofence = notificationServiceListOfFences.getTop();
                while (currentGeofence != null) {

                    geoLat = currentGeofence.getLatitude();
                    geoLong = currentGeofence.getLongitude();
                    Location fenceLocation = new Location("");
                    fenceLocation.setLatitude(geoLat);
                    fenceLocation.setLongitude(geoLong);


                    Log.e("Notification Service", "Distance to point " + currentGeofence.getName() + " is : " + currentLocation.distanceTo(fenceLocation));

                    if (currentLocation.distanceTo(fenceLocation) <= currentGeofence.getRadius()) {
                        inGeofence = true;
                        whenGeoFenceEntered = System.currentTimeMillis();
                        currentGracePeriod = currentGeofence.getTime();
                        break;
                    } else {
                        inGeofence = false;
                    }

                    currentGeofence = currentGeofence.getNextGeoFence();
                }
            }
        }
    }

    //CODE FOR WHEN THE BACKGROUND SERVICES FINISH
    public void onDestroy (){
        super.onDestroy();
    }

    //BUILDS THE NOTIFICATION

    public void notifyAboutItem () {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction ("RSSPullService");

        Intent myIntent = new Intent (Intent.ACTION_VIEW, Uri.parse(""));
        PendingIntent pendingIntent = PendingIntent.getActivity (getBaseContext(), 0, myIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
        Context context = getApplicationContext();

        Notification.Builder builder;

            builder = new Notification.Builder (context)
                    .setContentTitle("Forget Me Not")
                    .setContentText ("Don't forget anything!")
                    .setContentIntent(pendingIntent)
                    .setDefaults (Notification.DEFAULT_SOUND)
                    .setAutoCancel (true)
                    .setSmallIcon (R.drawable.ic_launcher);

        Notification notification = builder.build();

        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify (1, notification);
    }

    private void loadAllGeoFences ()
    {
        Log.e ("Notification Service", "I am loading all of the Geofences");
        try{
            raf = new RandomAccessFile(new File(getFilesDir(), "locations.bin"), "rw");

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
                    notificationServiceListOfFences.addNode(temp);

                }
            }
            raf.close();

        } catch (java.io.IOException e){

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        checkAndNotify();

    }
}
