package com.example.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;


public class AddMedicine extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        final EditText addnameMed = findViewById(R.id.addnameMed);
        final EditText addtypeMed = findViewById(R.id.addtypeMed);
        final EditText adduseMed = findViewById(R.id.adduseMed);
        final EditText adddose = findViewById(R.id.adddose);
        final EditText addintake = findViewById(R.id.addintake);
        final EditText addinfo = findViewById(R.id.addinfo);

        Button btnsave = findViewById(R.id.savebtn);
        com.example.medicare.Action act = new com.example.medicare.Action();
        btnsave.setOnClickListener(save ->
        {
            com.example.medicare.Medicine med = new com.example.medicare.Medicine(addnameMed.getText().toString(),addtypeMed.getText().toString(),adduseMed.getText().toString(),adddose.getText().toString(),addintake.getText().toString(),addinfo.getText().toString());
            act.add(med).addOnSuccessListener(suc ->
            {
                startActivity(new Intent(getApplicationContext(), ListRV.class));
                Toast.makeText(this, "SAVED!", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(er ->
            {
                Toast.makeText(this, ""+er.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });

        Button btnlist = findViewById(R.id.listbtn);
        btnlist.setOnClickListener(l ->
        {
            Intent intent = new Intent(AddMedicine.this, com.example.medicare.ListRV.class);
            startActivity(intent);
        });

    }
}