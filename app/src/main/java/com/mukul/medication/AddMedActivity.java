package com.mukul.medication;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.journaldev.sqlite.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class AddMedActivity extends Activity implements OnClickListener {

    private Button addTodoBtn;
    private EditText et_med;
    private EditText et_notes;
    private EditText et_dose;
    private EditText et_time;
    private EditText et_date;
    private EditText et_units;
    private EditText et_freq;
    //private int _id = R.id.add_record;



    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Add Medication");

        setContentView(R.layout.activity_add_med);

        et_med = (EditText) findViewById(R.id.et_med);
        et_notes = (EditText) findViewById(R.id.et_notes);
        et_dose = (EditText) findViewById(R.id.et_dose);
        et_time = (EditText) findViewById(R.id.et_time);
        et_date = (EditText) findViewById(R.id.et_date);
        et_units = (EditText) findViewById(R.id.et_units);
        et_freq = (EditText) findViewById(R.id.et_freq);


        addTodoBtn = (Button) findViewById(R.id.add_record);

        dbManager = new DBManager(this);
        dbManager.open();
        addTodoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_record:


                final String name = et_med.getText().toString();
                final String desc = et_notes.getText().toString();
                final String dose = et_dose.getText().toString();
                final String time = et_time.getText().toString();
                final String date = et_date.getText().toString();
                final String units = et_units.getText().toString();
                final String frequency = et_freq.getText().toString();

                DateFormat formatter = new SimpleDateFormat("yyyy:MM:dd HH:mm");
                try {
                    Date dt = formatter.parse(date + " " + time);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dt);
                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                    int minute = cal.get(Calendar.MINUTE);
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    //int second = cal.get(Calendar.SECOND);
                    Calendar targetCal = Calendar.getInstance();
                    Calendar cal2 = Calendar.getInstance();
                    targetCal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), hour,
                            minute, 0);
                    Intent intent = new Intent(getBaseContext(), AlarmReceiver.class); //ALARM IS SET
                    Random r = new Random();
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(),4 ,intent ,0 );
                    AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
                    Toast.makeText(getApplicationContext(), "Date Set?", Toast.LENGTH_LONG).show();
                    //this._id++;
                    month++;
                    String newmin;
                    if (minute < 10) {
                        newmin = "0" + minute;
                    }
                    else {
                        newmin = "" + minute;
                    }

                    String timestamp = year + "-" + month + "-" + day + " " + hour + ":" + newmin;

                    dbManager.insert(name, desc, dose, timestamp, units, frequency);

                    Intent main = new Intent(AddMedActivity.this, MedListActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(main);

                    //Note, this can be used to cancel the alarm
                    //Intent intent = new Intent(this, Mote.class);
                    //PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1253, intent, 0);
                    //AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    //alarmManager.cancel(pendingIntent);


                } catch (ParseException e) {
                    // This can happen if you are trying to parse an invalid date, e.g., 25:19:12.
                    // Here, you should log the error and decide what to do next
                    e.printStackTrace();
                }



                //break;
        }
    }

}