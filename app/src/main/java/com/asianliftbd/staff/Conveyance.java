package com.asianliftbd.staff;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.asianliftbd.staff.user.UBTotal;
import com.asianliftbd.staff.user.UBal;
import com.asianliftbd.staff.user.UConv;
import com.asianliftbd.staff.user.UInfo;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Conveyance extends AppCompatActivity {
    private TextView Name,Date,Date2;
    private Button submit;
    private String uid= FirebaseAuth.getInstance().getUid(),name=FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),site1,site2,site3,type,mode,date,date2,amount,refer,refo,bdate;
    private int bval,ctval,stval,roll;
    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(),
            cref=ref.child("transaction/conveyance/"+uid),
            ctref=ref.child("balance/total/conveyance"),
            stref=ref.child("balance/total/staff"),
            bref=ref.child("balance/conveyance/"+uid),
            rollref=ref.child("user/"+uid+"/info/info");
    private TextInputEditText Amount,RefO;
    private LinearLayout lr,lr1,ls2,ls3;
    private TextInputLayout lr2;
    private Spinner Site1,Site2,Site3,Type,Mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.conveyance);
        Name=findViewById(R.id.name);
        Name.setText(name);
        RefO=findViewById(R.id.refo);
        submit=findViewById(R.id.submit);
        lr=findViewById(R.id.lr);
        lr1=findViewById(R.id.lr1);
        lr2=findViewById(R.id.lr2);
        ls2=findViewById(R.id.ls2);
        ls3=findViewById(R.id.ls3);
        Site1=findViewById(R.id.site1);
        Site2=findViewById(R.id.site2);
        Site3=findViewById(R.id.site3);
        ArrayAdapter<CharSequence> a1 = ArrayAdapter.createFromResource(Conveyance.this,R.array.site, R.layout.spinner);
        a1.setDropDownViewResource(android.R.layout.select_dialog_item);
        Site1.setAdapter(a1);
        Site2.setAdapter(a1);
        Site3.setAdapter(a1);
        Site1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int p, long id){
                if(p!=0){ls2.setVisibility(View.VISIBLE);site1=String.valueOf(parent.getItemAtPosition(p));}
                else{site2="";ls2.setVisibility(View.GONE);}
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        Site2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int p, long id){
                if(p!=0){ls3.setVisibility(View.VISIBLE);site2= ", "+String.valueOf(parent.getItemAtPosition(p));}
                else{site2="";site3="";ls3.setVisibility(View.GONE);}
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        Site3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int p, long id){
                if(p!=0)site3=", "+String.valueOf(parent.getItemAtPosition(p));
                else site3="";
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        Type=findViewById(R.id.type);
        ArrayAdapter<CharSequence> a2 = ArrayAdapter.createFromResource(Conveyance.this,R.array.conveyance, R.layout.spinner);
        a2.setDropDownViewResource(android.R.layout.select_dialog_item);
        Type.setAdapter(a2);Type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> pr, View v, int p, long id){
                type= String.valueOf(pr.getItemAtPosition(p));
                switch (p){
                    case 1:lr.setVisibility(View.VISIBLE);lr1.setVisibility(View.VISIBLE);lr2.setVisibility(View.GONE);break;
                    case 3:lr.setVisibility(View.VISIBLE);lr2.setVisibility(View.VISIBLE);lr1.setVisibility(View.GONE);break;
                    default:lr.setVisibility(View.GONE);lr1.setVisibility(View.GONE);lr2.setVisibility(View.GONE);break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        Mode=findViewById(R.id.reft);
        ArrayAdapter<CharSequence> a3=ArrayAdapter.createFromResource(Conveyance.this,R.array.transportation, R.layout.spinner);
        a3.setDropDownViewResource(android.R.layout.select_dialog_item);
        Mode.setAdapter(a3);Mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int p, long id){
                mode=String.valueOf(parent.getItemAtPosition(p));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        Date=findViewById(R.id.date);
        Date2=findViewById(R.id.date2);
        Date.setOnClickListener(v -> showDate(Conveyance.this,Date,Date2));
        Amount=findViewById(R.id.amount);
        bref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot d) {
                DateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                bdate = df.format(Calendar.getInstance().getTime());
                UBal u=d.getValue(UBal.class);
                bval=u.getValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError e){}
        });
        ctref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot d) {
                UBTotal u=d.getValue(UBTotal.class);
                if (u != null) {
                    ctval=u.getValue();
                }else ctval=0;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError e){}
        });
        stref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot d) {
                UBTotal u=d.getValue(UBTotal.class);
                stval=u.getValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError e){}
        });
        rollref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot d) {
                UInfo u=d.getValue(UInfo.class);
                if (u!=null) {
                    roll=u.getRoll();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError e) {}
        });
    }
    public void Back(View v){finish();}
    public void Snack(View v, String t){
        final Snackbar s = Snackbar.make(v,t,Snackbar.LENGTH_SHORT);
        @SuppressLint("InflateParams")View custom=LayoutInflater.from(this).inflate(R.layout.m_snack,null );
        s.getView().setPadding(0,0,0,0);
        s.setBackgroundTint(Color.TRANSPARENT);
        ((ViewGroup) s.getView()).removeAllViews();
        ((ViewGroup) s.getView()).addView(custom);
        TextView tv = custom.findViewById(R.id.text);
        tv.setText(t);tv.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { s.dismiss(); }});
        s.show();
    }
    public static void showDate(final Context c, final TextView t, final TextView t2) {
        final Calendar cal = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dt = (v, y, m, d) -> {
            cal.set(Calendar.YEAR, y);
            cal.set(Calendar.MONTH, m);
            cal.set(Calendar.DAY_OF_MONTH, d);
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyMMdd", Locale.getDefault());
            t.setText(sdf.format(cal.getTime()));
            t2.setText(sdf2.format(cal.getTime()));
        };
        new DatePickerDialog(c, dt, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }
    public void Submit(View v){
        amount=Amount.getText().toString();
        refo=RefO.getText().toString();
        date=Date.getText().toString();
        date2=Date2.getText().toString();
        if (name.isEmpty()){Snack(v,"ইন্টারনেট কানেকশন নেই!");}
        else if (Site1.getSelectedItemPosition()==0){Snack(v,"প্রজেক্টের নাম বাছাই করুন");}
        else if (Type.getSelectedItemPosition()==0){Snack(v,"কনভেন্সের ধরণ বাছাই করুন");}
        else if (Type.getSelectedItemPosition()==1&&Mode.getSelectedItemPosition()==0){Snack(v,"যাতায়াতের মাধ্যম বাছাই করুন");}
        else if (Type.getSelectedItemPosition()==3&&refo.isEmpty()){Snack(v,"বিবরণ লিখুন");}
        else if (amount.isEmpty()){Snack(v,"টাকার পরিমাণ লিখুন");}
        else if (date.isEmpty()){Snack(v,"তারিখ বাছাই করুন");}
        else{
            if (Type.getSelectedItemPosition()==1&&Mode.getSelectedItemPosition()!=0){refer=mode;}
            else if (Type.getSelectedItemPosition()==3&&!(refo.isEmpty())){refer=refo;}
            else{refer="";}
            if ((site1.equals(site2)||site1.equals(site3))){Snack(v,"একই প্রজেক্ট বাছাই করা হয়েছে!");
            }else if(Site2.getSelectedItemPosition()!=0&&site2.equals(site3)){Snack(v,"একই প্রজেক্ট বাছাই করা হয়েছে!");
            }else{
                UConv u = new UConv(name, site1+site2+site3, type, refer, amount, date, date2);
                UBal ub=new UBal(name,bval,bdate,uid,roll);
                UBTotal uct=new UBTotal(ctval,bdate);
                UBTotal ust=new UBTotal(stval,bdate);
                submit.setVisibility(View.INVISIBLE);
                cref.push().setValue(u);
                bref.setValue(ub);
                ctref.setValue(uct);
                stref.setValue(ust);
                finish();
                startActivity(new Intent(Conveyance.this,ConvList.class));
                Toast.makeText(Conveyance.this, "সফলভাবে জমা হয়েছে", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
