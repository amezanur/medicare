package com.example.medicare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListRV extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter adapter;
    Action act;
    String key = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_rv);

        swipeRefreshLayout = findViewById(R.id.swip);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        act = new Action();
        loadData();

        FirebaseRecyclerOptions<Medicine> option =
                new FirebaseRecyclerOptions.Builder<Medicine>()
                        .setQuery(act.get(), new SnapshotParser<Medicine>() {
                            @NonNull
                            @Override
                            public Medicine parseSnapshot(@NonNull DataSnapshot snapshot) {

                                Medicine med = snapshot.getValue(Medicine.class);
                                med.setKey(snapshot.getKey());
                                return med;
                            }
                        }).build();

        adapter = new FirebaseRecyclerAdapter(option)
        {

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(ListRV.this).inflate(R.layout.view, parent, false);
                return new MedicineView(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i, @NonNull Object o) {
                MedicineView mv = (MedicineView) viewHolder;
                Medicine med = (Medicine) o;

                mv.medName.setText(med.getMedName());
                mv.medType.setText(med.getMedType());
                mv.medUse.setText(med.getMedUse());
                mv.medDose.setText(med.getMedDose());
                mv.medIntake.setText(med.getMedIntake());
                mv.medInfo.setText(med.getMedInfo());
                mv.option.setOnClickListener(op ->
                {
                    PopupMenu popupMenu = new PopupMenu(ListRV.this, mv.option);
                    popupMenu.inflate(R.menu.option_menu);
                    popupMenu.setOnMenuItemClickListener(item ->
                    {
                        switch (item.getItemId()) {

                            case R.id.menu_remove:
                                Action act = new Action();
                                act.remove(med.getKey()).addOnSuccessListener(suc ->
                                {
                                    Toast.makeText(ListRV.this, "REMOVED!", Toast.LENGTH_LONG).show();

                                }).addOnFailureListener(e ->
                                {
                                    Toast.makeText(ListRV.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                                break;
                        }
                        return false;
                    });
                    popupMenu.show();
                });
            }

            @Override
            public void onDataChanged()
            {
                Toast.makeText(ListRV.this, "DATA CHANGED!", Toast.LENGTH_SHORT).show();
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        act.get(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Medicine> meds = new ArrayList<>();

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Medicine med = dataSnapshot.getValue(Medicine.class);
                    med.setKey(dataSnapshot.getKey());
                    meds.add(med);
                    key = dataSnapshot.getKey();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}