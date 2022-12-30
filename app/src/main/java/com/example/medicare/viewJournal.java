package com.example.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class viewJournal extends AppCompatActivity {
    DatabaseReference database;
    ListView listView;
    ImageButton btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_journal);

        listView=findViewById(R.id.listview);
        final ArrayList<String> list=new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter<String>(viewJournal.this,R.layout.list_item,list);
        listView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Journal");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Journal j= dataSnapshot.getValue(Journal.class);
                    String txt = " Title :" + j.getTitle() + " Date :" + j.getDate() +" Notes :" + j.getNotes();
                    list.add(txt);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(viewJournal.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        btn=findViewById(R.id.addjournal);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(viewJournal.this, com.example.medicare.MainActivityJournal.class));

            }
        });
    }
}