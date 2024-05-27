package com.asianliftbd.staff.db;

import static com.asianliftbd.staff.DashboardHome.amountFormat;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asianliftbd.staff.R;

import java.util.Locale;

public class HTransaction extends RecyclerView.ViewHolder {
    View v;
    public HTransaction(@NonNull View iv) {
        super(iv);
        v=iv;
    }
    public void setDetails(String date, String title, String details, int value){
        TextView Date=v.findViewById(R.id.date);
        TextView Title=v.findViewById(R.id.title);
        TextView Details=v.findViewById(R.id.details);
        TextView Amount=v.findViewById(R.id.amount);
        Date.setText(date);
        amountFormat(Amount,value);
        if (value<0)Amount.setTextColor(Color.parseColor("#dc3545"));
        else Amount.setTextColor(Color.parseColor("#28a745"));
        switch (title) {
            case "Advance":Title.setText("অগ্রিম");break;
            case "For Conveyance":Title.setText("কনভেন্স বাবদ");break;
            case "Salary":Title.setText("বেতন");break;
            case "House Rent":Title.setText("ঘর ভাড়া");break;
            case "Payment":Title.setText("টাকা প্রদান");break;
            case "Bonus":Title.setText("বোনাস");break;
            case "Cashback":Title.setText("নগদ ফেরত");break;
            default:Title.setText(title);break;
        }
        if(details.isEmpty()){Details.setVisibility(View.GONE);}
        else{
            switch (details) {
                case "Cash":Details.setText("নগদ");break;
                case "bKash":Details.setText("বিকাশ");break;
                default:Details.setText(details);break;
            }
        }
    }
}
