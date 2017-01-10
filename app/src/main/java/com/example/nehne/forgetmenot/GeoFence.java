package com.example.nehne.forgetmenot;

public class GeoFence
{
    private double radius, longitude, latitude, distance, currentLon, currentLat;
    private String name;
    private int minutes;
    private GeoFence nextGeoFence;

    public GeoFence (String name, double radius, double longitude, double latitude, double currentLon , double currentLat, int minutes)
    {
	this.name = name;
	this.radius = radius;
	this.longitude = longitude;
	this.latitude = latitude;
	this.currentLon = currentLon;
	this.currentLat = currentLat;
	this.minutes = minutes;
	nextGeoFence = null;
	distance = Math.sqrt (Math.pow (currentLon - longitude, 2.0) + Math.pow (currentLat - latitude, 2.0));
    }


    public String getName ()
    {
	return name;
    }


    public void setName (String name)
    {
	this.name = name;
    }


    public double getRadius ()
    {
	return radius;
    }


    public void setRadius (double radius)
    {
	this.radius = radius;
    }


    public double getLatitude ()
    {
	return latitude;
    }


    public void setLatitude (double latitude)
    {
	this.latitude = latitude;
    }


    public double getLongitude ()
    {
	return longitude;
    }


    public void setLongitude (double longitude)
    {
	this.longitude = longitude;
    }
	
    public double getCurrentLat ()
    {
	return currentLat;
    }


    public void setCurrentLat (double currentLat)
    {
	this.currentLat = currentLat;
    }


    public double getCurrentLon ()
    {
	return currentLon;
    }


    public void setCurrentLon (double currentLon)
    {
	this.currentLon = currentLon;
    }   

    public GeoFence getNext ()
    {
	return nextGeoFence;
    }


    public void setNext (GeoFence nextGeoFence)
    {
	this.nextGeoFence = nextGeoFence;
    }


    public int getTime ()
    {
	return minutes;
    }


    public void setTime (int minutes)
    {
	this.minutes = minutes;
    }


    public double getDistance ()
    {
	return distance;
    }


    public void setDistance (double longitude, double latitude, double currentLon, double currentLat)
    {
	distance = Math.sqrt (Math.pow (currentLon - longitude, 2.0) + Math.pow (currentLat - latitude, 2.0));
    }
    
    public void setDistance (double distance)
    {
	this.distance = distance;
    }


    public String toString ()
    {
	return "Name: " + name + "\t Radius: " + radius + "\t Latitude: " + latitude + "\t Longitude: " + longitude + "\t time: " + minutes + "\t distance: " + distance;
    }


    public static GeoFence[] returnArray (int i, GeoFence lpr, LinkList list)
    {
	GeoFence current = list.getNode (lpr.getName ());
	GeoFence link[] = new GeoFence [i];
	for (int ctr = 0 ; ctr < link.length ; ctr++)
	{
	    link [ctr] = new GeoFence (current.getName (), current.getRadius (), current.getLongitude (), current.getLatitude (), current.getCurrentLon (), current.getCurrentLat (),current.getTime ());
	    current = current.getNext ();
	}
	return link;
    }
}
