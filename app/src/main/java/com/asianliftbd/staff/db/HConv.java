package com.asianliftbd.staff.db;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asianliftbd.staff.R;
public class HConv extends RecyclerView.ViewHolder {
    private View v;
    public HConv(@NonNull View iv) {
        super(iv);
        v=iv;
    }
    public void setDetails(String date, String site, String type, String ref, String amount){
        TextView Date=v.findViewById(R.id.date);
        TextView Title=v.findViewById(R.id.title);
        TextView Details=v.findViewById(R.id.details);
        TextView Amount=v.findViewById(R.id.amount);
        Date.setText(date);
        Title.setText(site);
        Amount.setText(String.format("৳ %s",amount));
        if (!ref.isEmpty()) {
            Details.setText(String.format("%s-%s", type, ref));
        }else {
            Details.setText(type);
        }
    }
}
