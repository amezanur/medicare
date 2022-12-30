package com.example.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivityAppointment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_main);

         Button btnshow = findViewById(R.id.showbtn);
        btnshow.setOnClickListener(c ->
        {
            Intent intent = new Intent(MainActivityAppointment.this,Bookingdr.class);
            startActivity(intent);
        });
    }

}