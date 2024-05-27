package com.asianliftbd.staff.db;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asianliftbd.staff.R;

public class HContact extends RecyclerView.ViewHolder {
    private View v;
    public HContact(@NonNull View iv) {
        super(iv);
        v=iv;
    }
    public void setDetails(final Context ctx, String name, String title, final String phone,String email) {
        TextView Name=v.findViewById(R.id.name);
        TextView Title=v.findViewById(R.id.title);
        TextView Phone=v.findViewById(R.id.phone);
        TextView Mail=v.findViewById(R.id.mail);
        ImageView Call=v.findViewById(R.id.call);
        Name.setText(name);
        Title.setText(title);
        Phone.setText(phone);
        Mail.setText(email);
        Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Uri u = Uri.parse("tel:"+phone);
                Intent i = new Intent(Intent.ACTION_DIAL, u);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(i);}
        });
    }
}