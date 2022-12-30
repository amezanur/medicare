package com.example.medicare;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivityMedicine extends AppCompatActivity {

    Button btnadd,btndetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_main);

        btnadd = findViewById(R.id.addbtn);
        btnadd.setOnClickListener(c ->
        {
            Intent intent = new Intent(MainActivityMedicine.this, com.example.medicare.AddMedicine.class);
            startActivity(intent);
        });

        btndetails = findViewById(R.id.detailsbtn);
        btndetails.setOnClickListener(c ->
        {
            Intent intent = new Intent(MainActivityMedicine.this, com.example.medicare.ListRV.class);
            startActivity(intent);
        });
    }


}