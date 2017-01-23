package com.example.nehne.forgetmenot;

/**
 * Created by Aidan Smitb on 2017-01-18.
 */

public class AidanGeoFence {

    protected String geoname;
    protected int geotime;
    protected double geolatitude;
    protected double geolongitude;
    protected double georadius;
    protected AidanGeoFence nextGeoFence;

    public AidanGeoFence (String name, double radius, double longitude, double latitude, int time)
    {
        geoname = name;
        georadius = radius;
        geolatitude = latitude;
        geolongitude = longitude;
        geotime = time;
        nextGeoFence = null;
    }

    public String getName ()
    {
        return (geoname);
    }

    public double getRadius ()
    {
        return (georadius);
    }

    public double getLatitude ()
    {
        return (geolatitude);
    }

    public double getLongitude ()
    {
        return  (geolongitude);
    }

    public int getTime ()
    {
        return (geotime);
    }

    public AidanGeoFence getNextGeoFence ()
    {
        return (nextGeoFence);
    }

    public void setNextGeoFence (AidanGeoFence newNextGeoFence)
    {
        nextGeoFence = newNextGeoFence;
    }

    public void setLatitude (double latitude)
    {
        geolatitude = latitude;
    }

    public void setLongitude (double longitude)
    {
        geolongitude = longitude;
    }

    public double getDistance (double currentLon, double currentLat)
    {
        return (Math.sqrt (Math.pow (currentLon - geolongitude, 2.0) + Math.pow (currentLat - geolatitude, 2.0)));
    }
}
