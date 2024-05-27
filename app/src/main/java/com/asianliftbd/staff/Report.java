package com.asianliftbd.staff;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.asianliftbd.staff.user.UCallback;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Report extends AppCompatActivity {
    private TextView Name,Date,Date2;
    private String uid= FirebaseAuth.getInstance().getUid(),name=FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),site,details,date,date2;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference(),
            rref=ref.child("-callback").push();
    private EditText Details;
    private Spinner Site;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.report);
        Name=findViewById(R.id.name);
        Name.setText(name);
        Details=findViewById(R.id.details);
        Date=findViewById(R.id.date);
        Date2=findViewById(R.id.date2);
        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate(Report.this, Date, Date2);
            }
        });
        Site=findViewById(R.id.site);
        ArrayAdapter<CharSequence> a1 = ArrayAdapter.createFromResource(Report.this, R.array.site, R.layout.spinner);
        a1.setDropDownViewResource(android.R.layout.select_dialog_item);Site.setAdapter(a1);
        Site.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int p, long id){
                site= String.valueOf(parent.getItemAtPosition(p));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    public void Submit(View v){
        details=Details.getText().toString();
        date = Date.getText().toString();
        date2 = Date2.getText().toString();
        if(name.isEmpty()){
            Snack(v,"ইন্টারনেট কানেকশন নেই!");
        }else if (Site.getSelectedItemPosition()==0){
            Snack(v,"প্রজেক্টের নাম বাছাই করুন");
        }else if(details.isEmpty()){
            Snack(v,"কলবেকের বিবরণ লিখুন");
        }else if(date.isEmpty()){
            Snack(v,"তারিখ নির্বাচন করুন");
        }else{
            UCallback u = new UCallback(name,site,details,date,date2,uid,rref.getKey());
            rref.setValue(u);
            Toast.makeText(Report.this, "সফলভাবে জমা হয়েছে", Toast.LENGTH_LONG).show();
            finish();
        }
    }
    public void Back(View v){finish();}
    public static void showDate(final Context c, final TextView t, final TextView t2) {
        final Calendar cal = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dt = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker v, int y, int m, int d) {
                cal.set(Calendar.YEAR, y);
                cal.set(Calendar.MONTH, m);
                cal.set(Calendar.DAY_OF_MONTH, d);
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyMMdd", Locale.getDefault());
                t.setText(sdf.format(cal.getTime()));
                t2.setText(sdf2.format(cal.getTime()));
            }
        };
        new DatePickerDialog(c, dt, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }
    public void Snack(View v, String t){final Snackbar s=Snackbar.make(v,t,Snackbar.LENGTH_SHORT);@SuppressLint("InflateParams")View c= LayoutInflater.from(this).inflate(R.layout.m_snack,null );s.setBackgroundTint(Color.TRANSPARENT);((ViewGroup) s.getView()).removeAllViews();((ViewGroup)s.getView()).addView(c);TextView tv=c.findViewById(R.id.text);tv.setText(t);tv.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {s.dismiss(); }});s.show();}
}