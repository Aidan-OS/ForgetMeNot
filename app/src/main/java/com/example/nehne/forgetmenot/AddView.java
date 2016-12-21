package com.example.nehne.forgetmenot;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import android.location.Address;
import android.location.Geocoder;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;


/**
 * Created by Aidan Smith on 2016-12-16.
 */

public class AddView extends FragmentActivity {

    protected String name;
    protected double radius;
    protected double latitude;
    protected double longitude;
    protected int minutes;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_view);
    }

    public void onSearch (View view)//WHEN PERSON SEARCHES FOR ADDRESS. SET UP TO BE ABLE TO SUPPORT MAP LATER IF WANTED
    {
        EditText locationBox = (EditText)findViewById(R.id.address);
        String location = locationBox.getText().toString();
        List<Address> addressList = null;
        if(location != null || !location.equals(""))
        {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location , 1);


            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = addressList.get(0);
            latitude = address.getLatitude();
            longitude = address.getLongitude();
        }
    }

    public void onFinish (View view)
    {
        //GET NAME FROM NAME BOX
        EditText nameBox = (EditText)findViewById (R.id.nameOfPoint);
        name = nameBox.getText().toString();
        byte [] byteName = new byte [16];
        byteName = name.getBytes();

        //GET RADIUS FROM RADIUS BOX
        EditText radiusBox = (EditText)findViewById (R.id.radius);
        radius = Double.parseDouble (radiusBox.getText().toString() );

        //GET MINUTES FROM MINUTE BOX
        EditText minutesBox = (EditText)findViewById(R.id.time);
        minutes= Integer.parseInt (minutesBox.getText().toString() );

        GeoFence temp = new GeoFence (name, radius, longitude, latitude, minutes);

        /*RandomAccessFile raf = null;
        try
        {
            raf = new RandomAccessFile (new File(getFilesDir(), "locations.bin"), "rw");

            int numberOfGeofences = raf.readInt ();
            int newNumberOfGeofences = numberOfGeofences + 1;

            raf.writeInt (newNumberOfGeofences);

            raf.seek ((36 * numberOfGeofences) + 4);

            raf.write (byteName);
            raf.writeDouble (radius);
            raf.writeDouble (longitude);
            raf.writeDouble (latitude);
            raf.writeInt (minutes);

            raf.close();
        }
        catch (java.io.IOException e)
        {
            //Dont know why it thinks this is needed, but for some reason it thinks the file wont exist when it is making it.
        }*/

        //END ADD VIEW
        finish();
    }
}
