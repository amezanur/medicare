package com.example.medicare;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class TimeActivity extends AppCompatActivity implements View.OnClickListener{

    private int notificationId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        // Set Onclick Listener.
        findViewById(R.id.setBtn).setOnClickListener(this);
        findViewById(R.id.cancelBtn).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        EditText editText = findViewById(R.id.editText);
        TimePicker timePicker = findViewById(R.id.timePicker);

        // Set notificationId & text.
        Intent intent = new Intent(TimeActivity.this, AlarmReceiver.class);
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("todo", editText.getText().toString());

        // getBroadcast(context, requestCode, intent, flags)
        PendingIntent alarmIntent = PendingIntent.getBroadcast(TimeActivity.this, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        switch (view.getId()) {
            case R.id.setBtn:
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();

                // Create time.
                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, hour);
                startTime.set(Calendar.MINUTE, minute);
                startTime.set(Calendar.SECOND, 0);
                long alarmStartTime = startTime.getTimeInMillis();

                // Set alarm.
                // set(type, milliseconds, intent)
                alarm.set(AlarmManager.RTC_WAKEUP, alarmStartTime, alarmIntent);
                startActivity(new Intent(getApplicationContext(), Home.class));
                Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.cancelBtn:
                alarm.cancel(alarmIntent);
                startActivity(new Intent(getApplicationContext(), MainActivityReminder.class));
                finish();
                Toast.makeText(this, "Canceled.", Toast.LENGTH_SHORT).show();
                break;
        }

    }

}

