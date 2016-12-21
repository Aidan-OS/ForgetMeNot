package com.example.nehne.forgetmenot;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import java.io.File;
import java.io.RandomAccessFile;

/**
 * Created by Aidan Smith on 2016-12-19.
 */

public class SettingsView extends FragmentActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_view);
    }

    public void onClearCache (View view)
    {
        RandomAccessFile raf = null;
        try
        {
            raf = new RandomAccessFile (new File(getFilesDir(), "locations.bin"), "rw");
            raf.writeInt (0);
            raf.close();
        }
        catch (java.io.IOException e)
        {
            //Dont know why it thinks this is needed, but for some reason it thinks the file wont exist when it is making it.
        }
    }
}
