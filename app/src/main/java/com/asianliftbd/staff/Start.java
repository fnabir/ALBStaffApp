package com.asianliftbd.staff;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Start extends AppCompatActivity {
    private long C;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.start);
    }
    public void Login(View v) {
        if (SystemClock.elapsedRealtime()-C<1000){return;}
        C=SystemClock.elapsedRealtime();
        startActivity(new Intent(Start.this, Login.class));
    }
    public void Terms(View v){
        if (SystemClock.elapsedRealtime()-C<1000){return;}
        C=SystemClock.elapsedRealtime();
        String url="https://asianliftbd.com/terms-of-use";
        Intent i=new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    public void Right(View v) {
        if (SystemClock.elapsedRealtime()-C<1000){return;}
        C=SystemClock.elapsedRealtime();
        String url = "https://asianliftbd.com";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    @Override
    public void onStart() {
        super.onStart();
        if (user != null) {
            startActivity(new Intent(Start.this, Login.class));
            finish();
        }
    }
}