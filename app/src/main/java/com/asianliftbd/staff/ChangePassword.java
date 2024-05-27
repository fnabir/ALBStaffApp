package com.asianliftbd.staff;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {
    private ProgressBar load;
    private TextInputEditText Old,New,Confirm;
    private TextInputLayout LOld,LNew,LConfirm;
    private Button Update;
    private String mail;
    private FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.change_password);
        Old=findViewById(R.id.pwd);
        LOld=findViewById(R.id.lpwd);
        New=findViewById(R.id.npwd);
        LNew=findViewById(R.id.lnpwd);
        Confirm=findViewById(R.id.copwd);
        LConfirm=findViewById(R.id.lcopwd);
        load=findViewById(R.id.load);
        Update=findViewById(R.id.update);
        TextView Email=findViewById(R.id.mail);
        mail=user.getEmail();
        Email.setText(mail);
    }
    public void Back(View v) {finish();}
    public void Save(final View v) {
        String OP=Old.getText().toString();
        final String NP=New.getText().toString();
        final String CP=Confirm.getText().toString();
        if(OP.isEmpty() && NP.isEmpty() && CP.isEmpty()){
            Snack(v,"Fields are empty!");
        }
        else if (OP.isEmpty()){
            LOld.setErrorEnabled(true);LOld.setError("Enter old password");
            Old.requestFocus();
        }
        else if(NP.isEmpty()){
            LOld.setErrorEnabled(false);
            LNew.setErrorEnabled(true);LNew.setError("Enter new password");
            New.requestFocus();
        }
        else if (NP.length()<6){
            LNew.setError("At least 6 characters long");
            New.requestFocus();
        }
        else  if(CP.isEmpty()){
            LNew.setErrorEnabled(false);
            LConfirm.setErrorEnabled(true);LConfirm.setError("Enter your new password again");
            Confirm.requestFocus();
        }else if (!(NP.equals(CP))){
            LConfirm.setError("New Password does not match with Confirm Password");
            Confirm.requestFocus();
        }
        else {
            LConfirm.setErrorEnabled(false);
            Update.setVisibility(View.GONE);
            load.setVisibility(View.VISIBLE);
            AuthCredential credential = EmailAuthProvider.getCredential(mail,OP);
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(NP).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(ChangePassword.this, "Password Changed Successfully.", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                            else {
                                Snack(v,"Authentication Failed!");
                                load.setVisibility(View.GONE);
                                Update.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }
    }
    public void Snack(View v, String t){final Snackbar s=Snackbar.make(v,t,Snackbar.LENGTH_SHORT);@SuppressLint("InflateParams")View c= LayoutInflater.from(this).inflate(R.layout.m_snack,null );s.setBackgroundTint(Color.TRANSPARENT);((ViewGroup) s.getView()).removeAllViews();((ViewGroup)s.getView()).addView(c);TextView tv=c.findViewById(R.id.text);tv.setText(t);tv.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {s.dismiss(); }});s.show();}
}
