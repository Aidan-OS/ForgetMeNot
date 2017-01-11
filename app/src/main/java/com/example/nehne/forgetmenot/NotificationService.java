package com.example.nehne.forgetmenot;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

import static com.example.nehne.forgetmenot.MapsActivity.currentGeofence;
/**
 * Created by Aidan Smitb on 2017-01-09.
 */

public class NotificationService extends Service {

    public static Timer alertTimer;
    public final CountDownLatch latch = new CountDownLatch (1);

    @Override
    public IBinder onBind (Intent intent) {
        return null;
    }

    //CREATES AN INSTANCE OF THE FILE
    @Override
    public void onCreate (){
        super.onCreate();
        alertTimer = new Timer ();

    }

    //WHEN FILE IS STARTED, THIS RUNS
    @Override
    public int onStartCommand (Intent intent, int flags, int startId){
        try{

            long startTime = System.currentTimeMillis();

            latch.await();//WHEN THE PERSON LEAVES THE AREA, THIS WILL TRIGGER THE REST OF THe CODE

            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;

            if (elapsedTime >= (currentGeofence.getTime() * 6000))
            {
                notifyAboutItem();
            }

            // TODO USE THIS IN THE CLASS FOR AUTOUPDATES alertTimer.schedule(sendNotification, (30000));
        } catch (Exception e){
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /*THIS TELLS THE TIMER WHAT TO DO
    TimerTask sendNotification = new TimerTask(){
        @Override
        public void run(){
            //TODO CODE FOR MAP UPDATE
            notifyAboutItem();
        }
    };*/

    //CODE FOR WHEN THE BACKGROUND SERVICES FINISH
    public void onDestroy (){

        try{

        }catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent ("com.example.nehne.forgetmenot");
        intent.putExtra ("yourvalue", "torestore");
        sendBroadcast (intent);
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
}
