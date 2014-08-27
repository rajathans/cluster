package com.example.rajat.cluster;

/**
 * Created by rajat on 12/8/14.
 */

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

public class DisplayNotification extends Activity {

       /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //---get the notification ID for the notification;
        // passed in by the MainActivity---
        int notifID = getIntent().getExtras().getInt("NotifID");

        //---PendingIntent to launch activity if the user selects
        // the notification---
        Intent i = new Intent(this,NoteDisplay.class);
        i.putExtra("NotifID", notifID);
        i.putExtra("title",getIntent().getExtras().getString("txtTitle"));
        i.putExtra("body",getIntent().getExtras().getString("txtBody"));

        NotificationManager nm = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        Notification notif = new Notification(
                R.drawable.icon,
                getIntent().getExtras().getString("txtTitle"),
                System.currentTimeMillis());

        PendingIntent detailsIntent =
                PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);  //********GENIUS******//

        CharSequence from =getIntent().getExtras().getString("txtTitle");
        CharSequence message = getIntent().getExtras().getString("txtBody");
        notif.setLatestEventInfo(this, from, message, detailsIntent);

        finish();
        nm.notify(notifID, notif);
        notif.defaults |= Notification.DEFAULT_LIGHTS;
        // Cancel the notification after its selected
        notif.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
    }
}