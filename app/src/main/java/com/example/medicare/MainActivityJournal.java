package com.example.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivityJournal extends AppCompatActivity {
    DatabaseReference database;
    Journal journal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_main);

        EditText date = (EditText) findViewById(R.id.date);
        EditText title = (EditText) findViewById(R.id.title);
        EditText notes = (EditText) findViewById(R.id.notes);
        Button button =(Button) findViewById(R.id.adddata);
        Button view = (Button) findViewById(R.id.viewJournal);

        journal= new Journal();
        database = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Journal");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (date.getText().toString().equals("")) {
                    Toast.makeText(MainActivityJournal.this, "Please fill in the date ", Toast.LENGTH_SHORT).show();
                } else if (title.getText().toString().equals("")) {
                    Toast.makeText(MainActivityJournal.this, "Please fill in the title. ", Toast.LENGTH_SHORT).show();
                } else if (notes.getText().toString().equals("")) {
                    Toast.makeText(MainActivityJournal.this, "Please fill in the notes. ", Toast.LENGTH_SHORT).show();
                } else {
                    journal.setTitle(title.getText().toString().trim());
                    journal.setDate(date.getText().toString().trim());
                    journal.setNotes(notes.getText().toString().trim());
                    database.push().setValue(journal);
                    Toast.makeText(MainActivityJournal.this, "Journal Added Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivityJournal.this, viewJournal.class));
                }
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityJournal.this, viewJournal.class);
                startActivity(intent);
            }
        });
    }
}