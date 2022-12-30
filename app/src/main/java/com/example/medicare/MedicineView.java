package com.example.medicare;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MedicineView extends RecyclerView.ViewHolder
{
    public TextView medName,medType,medUse,medDose,medIntake,medInfo,option;

    public MedicineView(@NonNull View itemView)
    {
        super(itemView);

        medName = itemView.findViewById(R.id.medName);
        medType = itemView.findViewById(R.id.medType);
        medUse = itemView.findViewById(R.id.medUse);
        medDose = itemView.findViewById(R.id.medDose);
        medIntake = itemView.findViewById(R.id.medIntake);
        medInfo = itemView.findViewById(R.id.medInfo);
        option = itemView.findViewById(R.id.option);
    }
}
