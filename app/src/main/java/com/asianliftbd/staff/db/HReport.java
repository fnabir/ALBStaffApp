package com.asianliftbd.staff.db;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asianliftbd.staff.R;

public class HReport extends RecyclerView.ViewHolder {
    private View v;
    public HReport(@NonNull View iv) {
        super(iv);
        v =iv;
    }
    public void setDetails(String name, String details, String date){
        TextView nameText= v.findViewById(R.id.name);
        TextView detailsText= v.findViewById(R.id.details);
        TextView dateText= v.findViewById(R.id.date);
        nameText.setText(name);
        detailsText.setText(details);
        dateText.setText(date);
    }
}
