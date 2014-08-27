package com.example.rajat.cluster;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.content.Intent;

/**
 * Created by rajat on 14/8/14.
 */
public class NoteDisplay extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_notif);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getIntent().getExtras().getString("title"));
        alertDialogBuilder.setMessage(getIntent().getExtras().getString("body"));
        final Intent h = new Intent(getApplicationContext(), ClusterMain.class);

        alertDialogBuilder.setPositiveButton("Okay",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        finish();
                    }
                });
        alertDialogBuilder.setNegativeButton("Check notes",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(h);

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

