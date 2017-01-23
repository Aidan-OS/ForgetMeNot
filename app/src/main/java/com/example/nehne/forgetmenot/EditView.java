package com.example.nehne.forgetmenot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import android.location.Address;
import android.location.Geocoder;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;


public class EditView extends FragmentActivity implements Serializable
{
    protected String name;
    protected double radius;
    protected double latitude;
    protected double longitude;
    protected int minutes;

    //These are used so that if a person choses a new location for the edit, it wont screw up if they choose to delete instead
    protected double absLat;
    protected double absLng;

    @Override
	public void onCreate (Bundle savedInstanceState)
    {
	    super.onCreate (savedInstanceState);
	    setContentView (R.layout.edit_view);

        latitude = (double) getIntent().getSerializableExtra("lat");
        longitude = (double) getIntent().getSerializableExtra("lng");

        absLat = latitude;
        absLng = longitude;

        name = (String) getIntent().getSerializableExtra("name");
        minutes = (int) getIntent().getSerializableExtra("time");
        radius = (double) getIntent().getSerializableExtra("radius");

        TextView nameBox = (TextView) findViewById (R.id.nameOfPoint);  //Fills in the name
	    nameBox.setText ("" + name);

	    TextView radiusBox = (TextView) findViewById (R.id.radius); // Fills in the radius
	    radiusBox.setText ("" + radius);

	    TextView myTextBox = (TextView) findViewById (R.id.textView2); // fills in the lat
	    myTextBox.setText ("" + latitude);

	    TextView myTextBox2 = (TextView) findViewById (R.id.textView4);  // fills in the long
	    myTextBox2.setText ("" + longitude);

	    TextView minutesBox = (TextView) findViewById (R.id.time); // fills in the grace period
	    minutesBox.setText ("" + minutes);
    }


    public void onSearch (View view)  //WHEN PERSON SEARCHES FOR ADDRESS. SET UP TO BE ABLE TO SUPPORT MAP LATER IF WANTED
    {
	    EditText locationBox = (EditText) findViewById (R.id.address);
	    String location = locationBox.getText ().toString ();
        Log.e("Searching", "I am searching for: " + location);

        List < Address > addressList = null;

        //Tells user they didnt enter anything to search for
        if (location.equals("") || location == null) {
            Toast.makeText(this, "You did not enter an address", Toast.LENGTH_SHORT).show();
            return;
        }

        else
        {
            Geocoder geocoder = new Geocoder (this);
            try
            {
                //TODO Put this in a selectable list
                addressList = geocoder.getFromLocationName (location, 1);

                if (addressList.toString().equals("[]")) {
                    Toast.makeText(this, "You did not enter a valid address", Toast.LENGTH_SHORT).show();// IF GMAPS COULDNT FIND WHAT THEY WERE LOOKING FOR
                    return;
                }
            }

            catch (IOException e)
            {
                e.printStackTrace ();
            }

            Address address = addressList.get (0);
            latitude = address.getLatitude ();
            longitude = address.getLongitude ();

            TextView myTextBox = (TextView) findViewById (R.id.textView2);//FILLS LATITUDE BIX
            myTextBox.setText ("" + latitude);

            TextView myTextBox2 = (TextView) findViewById (R.id.textView4);//FILLS LONG BOX
            myTextBox2.setText ("" + longitude);
        }
    }

    public void onDelete (View view)
    {
        //When the person is deleting, this will delete the point they selected by sending its info to main
        Intent returnIntnent = new Intent ();
        returnIntnent.putExtra("deleted", true);
        returnIntnent.putExtra("lat", absLat);
        returnIntnent.putExtra("lng", absLng);
        setResult (Activity.RESULT_OK, returnIntnent);
        finish();
    }

    public void onFinish (View view)
    {
        //All of the _____.equals statements and the toasts are used to send to the screen that you didnt fill it all out and to not proceed with a failed return

	    //GET NAME FROM NAME BOX
	    EditText nameBox = (EditText) findViewById (R.id.nameOfPoint);
	    name = nameBox.getText ().toString ();

        if (name.equals("") || name.equals("Current Location")) {
            Toast.makeText(this, "You did not enter a valid name", Toast.LENGTH_SHORT).show();
            return;
        }

	    //GET RADIUS FROM RADIUS BOX
	    EditText radiusBox = (EditText) findViewById (R.id.radius);

        String radiusText = radiusBox.getText ().toString ();

        if (radiusText.equals("")) {
            Toast.makeText(this, "You did not enter a radius", Toast.LENGTH_SHORT).show();
            return;
        }

        radius = Double.parseDouble(radiusText);

	    //GET MINUTES FROM MINUTE BOX
	    EditText minutesBox = (EditText) findViewById (R.id.time);

        String minutesText = minutesBox.getText().toString();
        if (minutesText.equals("")) {
            Toast.makeText(this, "You did not enter a grace period", Toast.LENGTH_SHORT).show();
            return;
        }
	    minutes = Integer.parseInt (minutesText);


        Intent returnIntnent = new Intent ();
        returnIntnent.putExtra("deleted", false);
        returnIntnent.putExtra("name", name);
        returnIntnent.putExtra("radius", radius);
        returnIntnent.putExtra("lat", latitude);
        returnIntnent.putExtra("lng", longitude);
        returnIntnent.putExtra("time", minutes);
        returnIntnent.putExtra("absLat", absLat);
        returnIntnent.putExtra("absLng", absLng);
        setResult (Activity.RESULT_OK, returnIntnent);

	    //END EDIT VIEW
	    finish ();
    }
}


