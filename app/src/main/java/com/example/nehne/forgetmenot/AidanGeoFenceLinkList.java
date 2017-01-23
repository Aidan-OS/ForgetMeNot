package com.example.nehne.forgetmenot;

import android.util.Log;

/**
 * Created by Aidan Smitb on 2017-01-18.
 */

public class AidanGeoFenceLinkList {

    protected AidanGeoFence thisIsAComplicatedNameForTop = null;

    public AidanGeoFenceLinkList ()
    {
        thisIsAComplicatedNameForTop = null;
    }

    public void addNode (AidanGeoFence input)
    {
        if (thisIsAComplicatedNameForTop == null)
        {
            thisIsAComplicatedNameForTop = input;
        }

        else
        {
            AidanGeoFence current = null;
            current = thisIsAComplicatedNameForTop;

            while (current.getNextGeoFence() != null)
            {
                current = current.getNextGeoFence();
            }

            current.setNextGeoFence(input);
        }
        Log.e ("Add Node", "The Linked List is adding the node: " + input.getName() + "Lat: " + input.getLatitude() + "Lng: " + input.getLongitude());
    }

    public AidanGeoFence searchLocation (double lngKey, double latKey)
    {
        AidanGeoFence current = thisIsAComplicatedNameForTop;
        AidanGeoFence location = new AidanGeoFence("", 0.0, 0.0, 0.0, 1);

        while (current.getNextGeoFence () != null)
        {
            if (current.getLongitude() == lngKey)
            {
                if (current.getLatitude() == latKey)
                {
                    location = current;
                    return location;

                }

            }
            current = current.getNextGeoFence ();
        }
        if (current.getLongitude() == lngKey)
        {
            if (current.getLatitude() == latKey)
            {
                location = current;
            }
        }
        return location;
    }

    public AidanGeoFence getTop ()
    {
        return (thisIsAComplicatedNameForTop);
    }

    public void deleteNode (double lat, double lng)
    {
        AidanGeoFence current = thisIsAComplicatedNameForTop;
        int recordNumber = 0;

        do {

            Log.e ("Deleting", "Linked List is Deleting co-ordinates: Lat: " + lat + " Long: " + lng);
            if (lat == current.getLatitude() && lng == current.getLongitude())
            {
                if (current.getNextGeoFence() == null) // IF WE ARE DELETING THE LAST NODE
                {
                    current = thisIsAComplicatedNameForTop;
                    for (int i = 0; i < recordNumber - 1; i++)
                    {
                        current = current.getNextGeoFence();
                    }
                    current.setNextGeoFence(null);

                }

                else if (current == thisIsAComplicatedNameForTop)
                {
                    thisIsAComplicatedNameForTop = current.getNextGeoFence();
                }

                else
                {
                    AidanGeoFence oneBefore = thisIsAComplicatedNameForTop;
                    int i = 0;

                    while (i != (recordNumber -1))
                    {
                        oneBefore = oneBefore.getNextGeoFence();
                        i++;
                    }

                    AidanGeoFence oneAfter = current.getNextGeoFence();

                    oneBefore.setNextGeoFence(oneAfter);
                }
                Log.e ("Deleting", "Linked List is Deleting co-ordinates: Lat: " + lat + " Long: " + lng);
                break;
            }
            recordNumber++;
            current = current.getNextGeoFence();
        }while (current != null);
    }

    public int linkListLength ()
    {
        int counter = 0;
        AidanGeoFence current = thisIsAComplicatedNameForTop;

        if (thisIsAComplicatedNameForTop == null)
        {
            return (0);
        }

        else {
            while (current.getNextGeoFence() != null) {
                counter++;
                current = current.getNextGeoFence();
            }
            counter++;
        }

        return (counter);
    }

    public String getAllNames ()
    {
        AidanGeoFence current = thisIsAComplicatedNameForTop;
        String names = "";

        while (current != null)
        {
            names += " " + current.getName();
            current = current.getNextGeoFence();
        }
        return (names);
    }

    public void clear ()
    {
        thisIsAComplicatedNameForTop = null;
    }
}
