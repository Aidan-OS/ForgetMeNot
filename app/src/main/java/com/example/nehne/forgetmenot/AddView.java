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
        Log.e("Searching", "I am searching for: " + location);

        //Tells user they didnt enter anything to search for
        if (location.equals("")) {
            Toast.makeText(this, "You did not enter an address", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Address> addressList = null;
        if(location != null || !location.equals(""))
        {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location , 1);

                if (addressList.toString().equals("[]")) {
                    Toast.makeText(this, "You did not enter a valid address", Toast.LENGTH_SHORT).show();// IF GMAPS COULDNT FIND WHAT THEY WERE LOOKING FOR
                    return;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = addressList.get(0);
            latitude = address.getLatitude();
            longitude = address.getLongitude();

            TextView myTextBox = (TextView) findViewById(R.id.textView2); // FILLS LAT BOX
            myTextBox.setText("" + latitude);

            TextView myTextBox2 = (TextView) findViewById(R.id.textView4); // FILLS LONG BOX
            myTextBox2.setText("" + longitude);
        }
    }

    public void onFinish (View view)
    {
        //GET NAME FROM NAME BOX
        EditText nameBox = (EditText)findViewById (R.id.nameOfPoint);
        name = nameBox.getText().toString();

        if (name.equals("") || name.equals("Current Location")) {
            Toast.makeText(this, "You did not enter a valid name", Toast.LENGTH_SHORT).show(); //CHECKS IF NO NAME ENTERED
            return;
        }

        //GET RADIUS FROM RADIUS BOX
        EditText radiusBox = (EditText)findViewById (R.id.radius);
        String radiusText = radiusBox.getText().toString();

        if (radiusText.equals("")) {
            Toast.makeText(this, "You did not enter a radius", Toast.LENGTH_SHORT).show(); //CHECKS IF NO RADIUS WAS ENTERED
            return;
        }
        radius = Double.parseDouble (radiusText);

        //GET MINUTES FROM MINUTE BOX
        EditText minutesBox = (EditText)findViewById(R.id.time);
        String timeText = minutesBox.getText().toString();

        if (timeText.equals("")) {
            Toast.makeText(this, "You did not enter a grace period", Toast.LENGTH_SHORT).show();//CHECKS IF NO TIME WAS ENTERED
            return;
        }

        minutes = Integer.parseInt (timeText);

        //TODO ADD IF LAT AND LONG ARENT HERE IT CAN STILL CRASH

        //Returns all information to main
        Intent returnIntnent = new Intent ();
        returnIntnent.putExtra("name", name);
        returnIntnent.putExtra("radius", radius);
        returnIntnent.putExtra("lat", longitude);
        returnIntnent.putExtra("lng", latitude);
        returnIntnent.putExtra("time", minutes);
        setResult (Activity.RESULT_OK, returnIntnent);

        //END ADD VIEW
        finish();
    }
}
