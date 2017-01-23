package com.example.nehne.forgetmenot;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import java.io.File;
import java.io.RandomAccessFile;
import android.net.Uri;


/**
 * Created by Aidan Smith on 2016-12-19.
 */

public class SettingsView extends FragmentActivity
{
    private String chosenRingtone;


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

        Intent i = new Intent ();
        i.putExtra ("doThis", 0);
        setResult (Activity.RESULT_OK, i);
        finish ();
    }

    public void onChangeRingtone (View view)
    {
        Intent intent = new Intent (RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
        this.startActivityForResult(intent, 5);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent)
    {
        if (resultCode == Activity.RESULT_OK && requestCode == 5)
        {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

            if (uri != null)
            {
                this.chosenRingtone = uri.toString();
            }
            else
            {
                this.chosenRingtone = null;
            }
        }
    }

    public void onTestRingtone (View view)
    {
        notifyAboutItem ();
    }

    private void notifyAboutItem() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction ("RSSPullService");

        Intent myIntent = new Intent (Intent.ACTION_VIEW, Uri.parse(""));
        PendingIntent pendingIntent = PendingIntent.getActivity (getBaseContext(), 0, myIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
        Context context = getApplicationContext();

        Notification.Builder builder;

        builder = new Notification.Builder (context)
                .setContentTitle("This is a Test Notification")
                .setContentText ("So did it work?")
                .setContentIntent(pendingIntent)
                .setDefaults (Notification.DEFAULT_SOUND)
                .setAutoCancel (true)
                .setSmallIcon (R.drawable.ic_launcher);

        Notification notification = builder.build();

        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify (1, notification);
    }

}
