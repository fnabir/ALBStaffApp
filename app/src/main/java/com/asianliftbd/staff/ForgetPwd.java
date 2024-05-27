package com.asianliftbd.staff;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
public class ForgetPwd extends AppCompatActivity {
    private TextInputEditText Mail;
    private TextInputLayout Lmail;
    private Button submit;
    private ProgressBar Load;
    private long C;
    private TextView Text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.forget_pwd);
        Mail=findViewById(R.id.mail);
        Lmail=findViewById(R.id.lmail);
        submit=findViewById(R.id.submit);
        Load=findViewById(R.id.load);
        Text=findViewById(R.id.text);
    }
    public void Back(View v){finish();}
    public static boolean isValidEmail(CharSequence mail){return (!TextUtils.isEmpty(mail) && Patterns.EMAIL_ADDRESS.matcher(mail).matches());}
    private boolean isNetworkConnected(){ConnectivityManager cm=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);return cm.getActiveNetworkInfo()!=null&&cm.getActiveNetworkInfo().isConnected();}
    public void Snack(View v, String t){final Snackbar s=Snackbar.make(v,t,Snackbar.LENGTH_SHORT);@SuppressLint("InflateParams")View c= LayoutInflater.from(this).inflate(R.layout.m_snack,null );s.setBackgroundTint(Color.TRANSPARENT);((ViewGroup) s.getView()).removeAllViews();((ViewGroup)s.getView()).addView(c);TextView tv=c.findViewById(R.id.text);tv.setText(t);tv.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {s.dismiss(); }});s.show();}
    public void Submit(View v){
        final String mail=Mail.getText().toString();
        if (mail.isEmpty()){Lmail.setErrorEnabled(true);Lmail.setError("ইমেইল প্রয়োজন!");
        }else if (!isValidEmail(mail)){Lmail.setErrorEnabled(true);Lmail.setError("অকার্যকর ইমেইল!");
        }else if(!(isNetworkConnected())){Snack(v, "ইন্টারনেট কানেকশন নাই!");
        }else{
            submit.setVisibility(View.GONE);
            Load.setVisibility(View.VISIBLE);
            FirebaseAuth.getInstance().sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Lmail.setErrorEnabled(false);
                        Text.setText("নতুন পাসওয়ার্ডের লিংক আপনার ইমেইলে পাঠানো হয়েছে।");
                        Text.setTextColor(Color.parseColor("#28a745"));
                    }else{
                        Lmail.setErrorEnabled(true);Lmail.setError("ইমেইল পাওয়া যায়নি!");
                    }
                    submit.setVisibility(View.VISIBLE);
                    Load.setVisibility(View.GONE);
                }
            });
        }
    }
}