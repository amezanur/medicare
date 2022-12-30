package com.example.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivityReminder extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_main);

        calendarView = (CalendarView) findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                    month = month+1;
                    String date = day+"/"+month+"/"+year;
                Log.d(TAG, "onSelectedDayChange : date "+ date);

                Intent intent = new Intent(MainActivityReminder.this, com.example.medicare.TimeActivity.class);
                intent.putExtra("date",date);
                startActivity(intent);
            }
        });
    }
}