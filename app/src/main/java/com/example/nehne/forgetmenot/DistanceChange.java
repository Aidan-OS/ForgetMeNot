package com.example.nehne.forgetmenot;

public class DistanceChange
{
    private double longitude, latitude, currentLon, currentLat, distance;

    public DistanceChange (double longitude, double latitude, double currentLon, double currentLat)
    {
	distance = Math.sqrt (Math.pow (currentLon - longitude, 2.0) + Math.pow (currentLat - latitude, 2.0));
    }


    public double getDistance ()
    {
	return distance;
    }


    public void setDistance (double longitude, double latitude, double currentLon, double currentLat)
    {
	distance = Math.sqrt (Math.pow (currentLon - longitude, 2.0) + Math.pow (currentLat - latitude, 2.0));
    }


    public String toString ()
    {
	return "the distance from the geofence is: " + distance;
    }
}
