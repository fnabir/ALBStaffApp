package com.asianliftbd.staff;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.asianliftbd.staff.user.UOffer;
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

public class Offer extends AppCompatActivity{
    private TextInputLayout Lname;
    private TextInputEditText Name,Address,Unit,Floor,Shaft,Person,Note;
    private String uid= FirebaseAuth.getInstance().getUid(),ptype,wtype,refer=FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),date,key;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference(),oref,oeref;
    private Button submit;
    private Spinner PType,WType;
    private ArrayAdapter<CharSequence> s1,s2;
    private Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.offer);
        extras = getIntent().getExtras();
        Name=findViewById(R.id.name);
        Lname=findViewById(R.id.lname);
        Address=findViewById(R.id.address);
        Unit=findViewById(R.id.unit);
        Floor=findViewById(R.id.floor);
        Shaft=findViewById(R.id.shaft);
        Person=findViewById(R.id.person);
        Note=findViewById(R.id.note);
        submit=findViewById(R.id.submit);
        PType=findViewById(R.id.ptype);
        WType=findViewById(R.id.wtype);
        s1 = ArrayAdapter.createFromResource(Offer.this, R.array.lift, R.layout.spinner);
        s1.setDropDownViewResource(android.R.layout.select_dialog_item);
        PType.setAdapter(s1);PType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> pr, View v, int p, long id){
                ptype= String.valueOf(pr.getItemAtPosition(p));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        s2 = ArrayAdapter.createFromResource(Offer.this, R.array.wtype, R.layout.spinner);
        s2.setDropDownViewResource(android.R.layout.select_dialog_item);
        WType.setAdapter(s2);WType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> pr, View v, int p, long id){
                wtype= String.valueOf(pr.getItemAtPosition(p));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        DateFormat df = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        date = df.format(Calendar.getInstance().getTime());
        if (extras!=null) {
            key=extras.getString("key");
            oeref=ref.child("-offer").child(key);
            oeref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot s) {
                    if (s.exists()){
                        UOffer u=s.getValue(UOffer.class);
                        Name.setText(u.getName());
                        Address.setText(u.getAddress());
                        ptype=u.getPtype();
                        int sp1 = s1.getPosition(ptype);
                        PType.setSelection(sp1);
                        wtype=u.getWtype();
                        int sp2 = s2.getPosition(wtype);
                        WType.setSelection(sp2);
                        Unit.setText(u.getUnit());
                        Floor.setText(u.getFloor());
                        Shaft.setText(u.getShaft());
                        Person.setText(u.getPerson());
                        Note.setText(u.getNote());}
                }
                @Override
                public void onCancelled(@NonNull DatabaseError e){}
            });
        }
    }
    public void Back(View v){finish();}
    public void Submit(View v){
        String name=Name.getText().toString(),
                address=Address.getText().toString(),
                unit=Unit.getText().toString(),
                floor=Floor.getText().toString(),
                shaft=Shaft.getText().toString(),
                person=Person.getText().toString(),
                note=Note.getText().toString();
        if(name.isEmpty()){Name.requestFocus();Lname.setErrorEnabled(true);Lname.setError("বাক্তি বা প্রজেক্টের নাম লিখুন");}
        else if(PType.getSelectedItemPosition()==0){Lname.setErrorEnabled(false);Snack(v,"লিফটের ধরণ বাছাই করুন");}
        else{
            if(address.isEmpty()){address="";}
            if(WType.getSelectedItemPosition()==0){wtype="নতুন প্রজেক্ট";}
            if(unit.isEmpty()){unit="1";}
            if(floor.isEmpty()){floor="";}
            if(shaft.isEmpty()){shaft="";}
            if(person.isEmpty()){person="";}
            if(note.isEmpty()){note="";}
            if (extras!=null) {
                UOffer u = new UOffer(name,address,ptype,wtype,unit,floor,shaft,person,note,refer,date,key,uid);
                oeref.setValue(u);}
            else {
                oref=ref.child("-offer").push();
                UOffer u = new UOffer(name,address,ptype,wtype,unit,floor,shaft,person,note,refer,date,oref.getKey(),uid);
                oref.setValue(u);
            }
            submit.setVisibility(View.GONE);
            finish();
            Toast.makeText(Offer.this, "সফলভাবে জমা হয়েছে", Toast.LENGTH_SHORT).show();
        }
    }
    public void Snack(View v, String t){final Snackbar s=Snackbar.make(v,t,Snackbar.LENGTH_SHORT);@SuppressLint("InflateParams")View c= LayoutInflater.from(this).inflate(R.layout.m_snack,null );s.setBackgroundTint(Color.TRANSPARENT);((ViewGroup) s.getView()).removeAllViews();((ViewGroup)s.getView()).addView(c);TextView tv=c.findViewById(R.id.text);tv.setText(t);tv.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {s.dismiss(); }});s.show();}
}