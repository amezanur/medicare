package com.example.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Report extends AppCompatActivity {

    TextView datereceived;
    TextView timereceived;
    TextView phonenumberreceived;
    TextView namereceived;
    TextView reasonreceived;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        datereceived = (TextView) findViewById(R.id.datereport);
        timereceived = (TextView) findViewById(R.id.timereport);
        namereceived = (TextView) findViewById(R.id.namereport);
        phonenumberreceived = (TextView) findViewById(R.id.phonenumberreport);
        reasonreceived = (TextView) findViewById(R.id.reasonreport);

        //receive data from bookingdr
        Intent receiverIntent = getIntent();
        String datereceivedValue = receiverIntent.getStringExtra("date");
        String timereceivedValue = receiverIntent.getStringExtra("time");
        String namereceivedValue = receiverIntent.getStringExtra("name");
        String phonenumberreceivedValue = receiverIntent.getStringExtra("phonenumber");
        String reasonreceivedValue = receiverIntent.getStringExtra("reason");

        //display appointment report/details
        datereceived.setText(datereceivedValue);
        timereceived.setText(timereceivedValue);
        namereceived.setText(namereceivedValue);
        phonenumberreceived.setText(phonenumberreceivedValue);
        reasonreceived.setText(reasonreceivedValue);
    }
}