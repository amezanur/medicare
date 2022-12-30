package com.example.medicare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    ArrayList<com.example.medicare.Medicine> list = new ArrayList<>();

    public Adapter(Context context) {
        this.context = context;
    }

    public void setItems(ArrayList<com.example.medicare.Medicine> med) {
        list.addAll(med);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view, parent, false);
        return new com.example.medicare.MedicineView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        com.example.medicare.MedicineView mv = (com.example.medicare.MedicineView) holder;
        com.example.medicare.Medicine med = list.get(position);

        mv.medName.setText(med.getMedName());
        mv.medType.setText(med.getMedType());
        mv.medUse.setText(med.getMedUse());
        mv.medDose.setText(med.getMedDose());
        mv.medIntake.setText(med.getMedIntake());
        mv.medInfo.setText(med.getMedInfo());
        mv.option.setOnClickListener(op ->
        {
            PopupMenu popupMenu = new PopupMenu(context, mv.option);
            popupMenu.inflate(R.menu.option_menu);
            popupMenu.setOnMenuItemClickListener(item ->
            {
                switch (item.getItemId()) {

                    case R.id.menu_remove:
                        com.example.medicare.Action act = new com.example.medicare.Action();
                        act.remove(med.getKey()).addOnSuccessListener(suc ->
                        {
                            Toast.makeText(context, "REMOVED!", Toast.LENGTH_LONG).show();
                            notifyItemRemoved(position);
                        }).addOnFailureListener(e ->
                        {
                            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                        break;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}