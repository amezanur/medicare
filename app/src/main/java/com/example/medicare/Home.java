package com.example.medicare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Home extends AppCompatActivity implements View.OnClickListener {

    CardView profile, medicine, reminder, journal, appointment, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        profile = findViewById(R.id.profile);
        medicine = findViewById(R.id.medicine);
        reminder = findViewById(R.id.reminder);
        journal = findViewById(R.id.journal);
        appointment = findViewById(R.id.appointment);
        logout = findViewById(R.id.logout);

        profile.setOnClickListener(this);
        medicine.setOnClickListener(this);
        reminder.setOnClickListener(this);
        journal.setOnClickListener(this);
        appointment.setOnClickListener(this);
        logout.setOnClickListener(this);

    }

    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.profile:
                i = new Intent(this, UserProfile.class);
                startActivity(i);
                break;

            case R.id.medicine:
                i = new Intent(this, MainActivityMedicine.class);
                startActivity(i);
                break;

            case R.id.reminder:
                i = new Intent(this, MainActivityReminder.class);
                startActivity (i);
                break;

            case R.id.journal:
                i = new Intent(this, MainActivityJournal.class);
                startActivity (i);
                break;

            case R.id.appointment:
                i = new Intent(this, MainActivityAppointment.class);
                startActivity (i);
                break;

            case R.id.logout:
                i = new Intent(this, SignIn.class);
                startActivity (i);
                finish();
        }
    }
}