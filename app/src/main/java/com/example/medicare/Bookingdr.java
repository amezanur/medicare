package com.example.medicare;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

public class Bookingdr extends AppCompatActivity {

    EditText dateTXT;
    ImageView cal;
    private int mDate, mMonth, mYear;
    Button bookbutton;
    Button savebutton;
    EditText nameTXT;
    EditText phonenumberTXT;
    EditText reasonTXT;
    DatabaseReference reff;
    Appointment appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingdr);
        setContentView(R.layout.activity_bookingdr);
        timeTXT = findViewById(R.id.timeButton);
        timepicker = findViewById(R.id.timepicker);
        nameTXT = findViewById(R.id.name);
        phonenumberTXT = findViewById(R.id.phonenumber);
        reasonTXT = findViewById(R.id.reason);
        bookbutton = findViewById(R.id.bookbutton);
        savebutton = findViewById(R.id.savebutton);
        appointment = new Appointment();
        reff= FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Appointment");

//save data to firebase
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String name=nameTXT.getText().toString().trim();
                String phonenumber=phonenumberTXT.getText().toString().trim();
                String reason=reasonTXT.getText().toString().trim();
                String date=dateTXT.getText().toString().trim();
                String time=timeTXT.getText().toString().trim();
                appointment.setName(nameTXT.getText().toString().trim());
                appointment.setPhonenumber(phonenumber);
                appointment.setReason(reason);
                appointment.setDate(date);
                appointment.setTime(time);
                reff.child("appointment1").setValue(appointment);
                Toast.makeText(Bookingdr.this, "Data Inserted Successfully",Toast.LENGTH_LONG).show();
            }
        });

        //datepicker

        dateTXT=findViewById(R.id.date);
                cal=findViewById(R.id.datepicker);
                cal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final Calendar Cal = Calendar.getInstance();
                        mDate = Cal.get(Calendar.DATE);
                        mMonth = Cal.get(Calendar.MONTH);
                        mYear = Cal.get(Calendar.YEAR);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(Bookingdr.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datepicker, int year, int month, int date) {
                                dateTXT.setText(date+"-"+(month+1)+"-"+year);
                            }


                        }, mYear, mMonth, mDate);
                        datePickerDialog.getDatePicker().setMinDate(Cal.getTimeInMillis());
                        datePickerDialog.show();
                    }
                });

    }

//timepicker
    EditText timeTXT;
    ImageView timepicker;
    int hour, minute;

    public void popTimePicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {

            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                timeTXT.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
            }
        };



        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }


    //click button generate report and send the data
    public void buttonsenderpressed(View v){

        Intent senderIntent = new Intent(this, com.example.medicare.Report.class);
        senderIntent.putExtra("date", dateTXT.getText().toString());
        senderIntent.putExtra("time", timeTXT.getText().toString());
        senderIntent.putExtra("name", nameTXT.getText().toString());
        senderIntent.putExtra("phonenumber", phonenumberTXT.getText().toString());
        senderIntent.putExtra("reason", reasonTXT.getText().toString());

        startActivity(senderIntent);

    }



}
