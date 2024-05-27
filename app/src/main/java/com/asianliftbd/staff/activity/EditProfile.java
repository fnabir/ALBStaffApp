package com.asianliftbd.staff.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.asianliftbd.staff.R;
import com.asianliftbd.staff.user.UContact;
import com.asianliftbd.staff.user.UInfo;
import com.asianliftbd.staff.user.UProfile;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfile extends AppCompatActivity {
    private TextInputEditText name,phone;
    private TextInputLayout Lname,Lphone;
    private FirebaseAuth auth=FirebaseAuth.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid=auth.getUid(),mail=user.getEmail(),title;
    private int roll;
    DatabaseReference ref= FirebaseDatabase.getInstance().getReference(),pref=ref.child("user/"+uid+"/profile"),
            cref=ref.child("contact/alb/"+uid),iref=ref.child("user/"+uid+"/info/info");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.edit_profile);
        name=findViewById(R.id.name);
        Lname=findViewById(R.id.lname);
        phone=findViewById(R.id.phone);
        Lphone=findViewById(R.id.lphone);
        TextView Mail=findViewById(R.id.mail);
        Mail.setText(mail);
        pref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot d) {
                UProfile u=d.getValue(UProfile.class);
                name.setText(u.getName());
                phone.setText(u.getPhone());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError e){}
        });
        iref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot d) {
                UInfo u=d.getValue(UInfo.class);
                if (u != null) {
                    title=u.getTitle();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError e) {}
        });
        cref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot d) {
                UContact u=d.getValue(UContact.class);
                if (u != null) {
                    roll=u.getRoll();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError e) {}
        });
    }
    public void Back(View v){finish();}
    public void Save(View v) {
        String Name=name.getText().toString();
        String No=phone.getText().toString();
        if (Name.isEmpty()){Lname.setErrorEnabled(true);Lname.setError("নাম লিখুন");}
        else if (Name.length()<6){Lname.setError("সম্পূর্ণ নাম লিখুন");}
        else if (No.isEmpty()) {Lname.setErrorEnabled(false);Lphone.setErrorEnabled(true);Lphone.setError("ফোন নাম্বার লিখুন");}
        else if (No.length()!=11) {Lphone.setError("১১-সংখ্যার নাম্বার লিখুন");}
        else {
            Lphone.setErrorEnabled(false);
            UProfile u = new UProfile(Name, No);
            UContact uc=new UContact(Name,title,FirebaseAuth.getInstance().getCurrentUser().getEmail(),No,roll);
            cref.setValue(uc);
            pref.setValue(u);
            UserProfileChangeRequest updt = new UserProfileChangeRequest.Builder()
                    .setDisplayName(Name).build();
            user.updateProfile(updt);
            Toast.makeText(EditProfile.this, "সফলভাবে জমা হয়েছে", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}