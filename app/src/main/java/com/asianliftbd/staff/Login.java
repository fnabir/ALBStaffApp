package com.asianliftbd.staff;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.asianliftbd.staff.user.UDevice;
import com.asianliftbd.staff.user.UValue;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class Login extends AppCompatActivity {
    private TextInputEditText Mail,Pwd;
    private TextInputLayout Lmail,Lpwd;
    private Button btn;
    private long C;
    private final FirebaseAuth auth=FirebaseAuth.getInstance();
    private final FirebaseUser user = auth.getCurrentUser();
    private final DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
    private DatabaseReference inforef;
    private ProgressBar Load;
    private String token,date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Mail=findViewById(R.id.mail);
        Pwd=findViewById(R.id.pwd);
        Lmail=findViewById(R.id.lmail);
        Lpwd=findViewById(R.id.lpwd);
        btn=findViewById(R.id.btn);
        Load=findViewById(R.id.load);
    }
    public static boolean isValidEmail(CharSequence mail){return (!TextUtils.isEmpty(mail) && Patterns.EMAIL_ADDRESS.matcher(mail).matches());}
    private boolean isNetworkConnected(){ConnectivityManager cm=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);return cm.getActiveNetworkInfo()!=null&&cm.getActiveNetworkInfo().isConnected();}
    public void Snack(View v, String t){final Snackbar s=Snackbar.make(v,t,Snackbar.LENGTH_SHORT);@SuppressLint("InflateParams")View c= LayoutInflater.from(this).inflate(R.layout.m_snack,null );s.setBackgroundTint(Color.TRANSPARENT);((ViewGroup) s.getView()).removeAllViews();((ViewGroup)s.getView()).addView(c);TextView tv=c.findViewById(R.id.text);tv.setText(t);tv.setOnClickListener(v1 -> s.dismiss());s.show();}
    public void SignIn(final View v){
        String mail= Objects.requireNonNull(Mail.getText()).toString();
        String pass= Objects.requireNonNull(Pwd.getText()).toString();
        if (mail.isEmpty()){Lmail.setErrorEnabled(true);Lmail.setError("Email Required");
        }else if (!isValidEmail(mail)){Lmail.setErrorEnabled(true);Lmail.setError("Invalid Email Address");
        }else if(pass.isEmpty()){Lmail.setErrorEnabled(false);Lpwd.setErrorEnabled(true);Lpwd.setError("Password Required");
        }else if(!(isNetworkConnected())){Snack(v,"No Internet Connection");
        }else{Lmail.setErrorEnabled(false);Lpwd.setErrorEnabled(false);btn.setVisibility(View.GONE);Load.setVisibility(View.VISIBLE);
            auth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(Login.this, task -> {Load.setVisibility(View.GONE);btn.setVisibility(View.VISIBLE);
                if (!task.isSuccessful()){Snack(v,"Wrong Credentials");
                }else{inforef= ref.child("user/"+auth.getUid()+"/info");
                    FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task1 -> {
                        token= task1.getResult();
                        DatabaseReference tokenref= inforef.child("token");
                        UValue userValue=new UValue(token);
                        tokenref.setValue(userValue);
                    });
                    String model= Build.MODEL,device=Build.DEVICE,manufacturer=Build.MANUFACTURER,brand=Build.BRAND,android=Build.VERSION.RELEASE,version= BuildConfig.VERSION_NAME;
                    DateFormat df = new SimpleDateFormat("dd.MM.yy hh:mm aa", Locale.getDefault());
                    date = df.format(Calendar.getInstance().getTime());
                    DatabaseReference mblref=inforef.child("login");
                    UDevice u=new UDevice(model,device,manufacturer,brand,android,date,version);
                    mblref.setValue(u);
                    Intent i=new Intent(Login.this, Dashboard.class);
                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
            });
        }
    }
    @Override public void onStart(){super.onStart();if(user != null){startActivity(new Intent(Login.this, Welcome.class));finish();}}
    public void Terms(View v){
        if (SystemClock.elapsedRealtime()-C<1000){return;}
        C=SystemClock.elapsedRealtime();
        String url="https://asianliftbd.com/terms-of-use";
        Intent i=new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    public void ForgetPwd(View v){
        if(SystemClock.elapsedRealtime()-C<1000){return;}
        C=SystemClock.elapsedRealtime();
        startActivity(new Intent(this, ForgetPwd.class));
    }
}