package com.asianliftbd.staff.db;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asianliftbd.staff.CallbackProject;
import com.asianliftbd.staff.R;

public class HCallback extends RecyclerView.ViewHolder {
    private View v;
    public HCallback(@NonNull View iv) {
        super(iv);
        v =iv;
    }
    public void setDetails(final Context ctx, final String name, final int value){
        TextView Site= v.findViewById(R.id.site);
        TextView count= v.findViewById(R.id.count);
        LinearLayout m=v.findViewById(R.id.m);
        Site.setText(name);
        count.setText(String.valueOf(value));
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, CallbackProject.class);
                i.putExtra("key",name);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(i);
            }
        });
    }
}
