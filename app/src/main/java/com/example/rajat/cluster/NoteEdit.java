package com.example.rajat.cluster;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class NoteEdit extends Activity {

    public EditText mTitleText,mBodyText;
    TimePicker timePicker;
    DatePicker datePicker;
    private Long mRowId;
    NotificationManager nm;
    public ClusterDbAdapter mDbHelper;
    private PendingIntent pendingIntent;

    private boolean isEmpty(TextView etText) {              //this function checks if the txtview is empty or not
        return etText.getText().toString().trim().length() <= 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new ClusterDbAdapter(this);
        mDbHelper.open();

        setContentView(R.layout.note_edit);
        setTitle(R.string.edit_note);

        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);

        Button confirmButton = (Button) findViewById(R.id.confirm);
        final Button deleteButton = (Button) findViewById(R.id.delete);

        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(ClusterDbAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(ClusterDbAdapter.KEY_ROWID)
									: null;
		}

		populateFields();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                timePicker = (TimePicker) findViewById(R.id.timePicker);
                datePicker = (DatePicker) findViewById(R.id.datePicker);

                //---use the AlarmManager to trigger an alarm---
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                //---get current date and time---
                Calendar calendar = Calendar.getInstance();

                //---sets the time for the alarm to trigger---
                calendar.set(Calendar.YEAR, datePicker.getYear());
                calendar.set(Calendar.MONTH, datePicker.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                calendar.set(Calendar.SECOND, 0);

                //---PendingIntent to launch activity when the alarm triggers---
                Intent i = new Intent("com.example.rajat.cluster.DisplayNotification");

                //---assign an ID of 1---

                if(getIntent().hasExtra("txtTitle") && getIntent().hasExtra("txtBody") && getIntent().getExtras().getInt("NotifID") == 1)
                {
                    getIntent().removeExtra("txtTitle");
                    getIntent().removeExtra("txtBody");
                    getIntent().removeExtra("NotifID");
                }

                i.putExtra("NotifID", 1);
                i.putExtra("txtTitle",mTitleText.getText().toString());
                i.putExtra("txtBody",mBodyText.getText().toString());

                PendingIntent displayIntent = PendingIntent.getActivity(
                        getBaseContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);    //***************GENIUS************//

                //---sets the alarm to trigger---
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(), displayIntent);

                setResult(RESULT_OK);
                finish();
            }

        });

        deleteButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {

                if (isEmpty(mBodyText) && isEmpty(mTitleText)) {
                    Toast.makeText(getBaseContext(), "Cant do it man", Toast.LENGTH_LONG).show();
                } else {
                    open(view);
                }
            }
        });
    }

    private void populateFields() {
        if (mRowId != null) {
            Cursor note = mDbHelper.fetchNote(mRowId);
            startManagingCursor(note);
            mTitleText.setText(note.getString(
                    note.getColumnIndexOrThrow(ClusterDbAdapter.KEY_TITLE)));
            mBodyText.setText(note.getString(
                    note.getColumnIndexOrThrow(ClusterDbAdapter.KEY_BODY)));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(ClusterDbAdapter.KEY_ROWID, mRowId);
    }

    public void open(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure?" +
                "\nClick anywhere out of this dialog box to back to editing note");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        mDbHelper.deleteNote(mRowId);
                        Toast.makeText(getBaseContext(), "Note successfully deleted from the database.", Toast.LENGTH_LONG).show();
                        finish();

                    }
                });
        alertDialogBuilder.setNegativeButton("Back to main",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    private void saveState() {
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();

        if (mRowId == null) {
            long id = mDbHelper.createNote(title, body);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateNote(mRowId, title, body);
        }
    }

}
