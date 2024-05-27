package com.asianliftbd.staff.db;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asianliftbd.staff.Offer;
import com.asianliftbd.staff.R;
import com.google.firebase.auth.FirebaseAuth;

public class HOffer extends RecyclerView.ViewHolder {
    private View v;
    public HOffer(@NonNull View iv) {
        super(iv);
        v=iv;
    }
    public void setDetails(final Context ctx, String name, String address, String ptype, String wtype, String unit, String floor, String shaft, String person, String note, String refer, String date, final String key,final String uid){
        TextView Name=v.findViewById(R.id.name);
        TextView Address=v.findViewById(R.id.address);
        TextView Type=v.findViewById(R.id.type);
        TextView  Floor=v.findViewById(R.id.floor);
        TextView Shaft=v.findViewById(R.id.shaft);
        TextView  Note=v.findViewById(R.id.note);
        TextView  Refer=v.findViewById(R.id.refer);
        RelativeLayout m=v.findViewById(R.id.m);
        Name.setText(name);
        Shaft.setText(shaft);
        Note.setText(note);
        Refer.setText(String.format("%s | %s",refer,date));
        if(address.isEmpty())Address.setVisibility(View.GONE);
        else Address.setText(address);
        if(wtype.isEmpty()&&unit.isEmpty()){Type.setText(ptype);}
        else if(wtype.isEmpty()&&!unit.isEmpty()){Type.setText(String.format("%s > %s Unit(s)",ptype,unit));}
        else if(!wtype.isEmpty()&&unit.isEmpty()){Type.setText(String.format("%s > %s",ptype,wtype));}
        else Type.setText(String.format("%s > %s > %s Unit(s)",ptype,wtype,unit));
        if(person.isEmpty()&&floor.isEmpty()){Floor.setVisibility(View.GONE);}
        else if(floor.isEmpty()&&!person.isEmpty()){Floor.setText(String.format("Person/Load: %s ",person));}
        else if(person.isEmpty()&&!floor.isEmpty()){Floor.setText(String.format("%s Floor",floor));}
        else{Floor.setText(String.format("%s Floor | Person/Load: %s",floor,person));}
        if(shaft.isEmpty()){Shaft.setVisibility(View.GONE);}
        if(note.isEmpty()){Note.setVisibility(View.GONE);}
        m.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (FirebaseAuth.getInstance().getUid().equals(uid)) {
                    Intent i = new Intent(ctx, Offer.class);
                    i.putExtra("key", key);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(i);
                } else {
                    Toast.makeText(ctx, "অফারটি আপনার নয়", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }
}