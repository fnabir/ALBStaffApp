package com.asianliftbd.staff;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.asianliftbd.staff.user.UApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AppInfo extends AppCompatActivity {
    private String date,name,newAppDate,NewFeature,url;
    private int code;
    private LinearLayout lur,lnf,Download;
    private long C;
    private TextView updateResult,versionN,lastCheck,newupdate,newdate,newFeature;
    private DatabaseReference ref=FirebaseDatabase.getInstance().getReference(),dbref=ref.child("app/staff");
    private int versionC,WRITE_PERMISSION=1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.about_app);
        versionN=findViewById(R.id.version_n);
        versionN.setText(BuildConfig.VERSION_NAME);
        lastCheck=findViewById(R.id.last_check);
        Download=findViewById(R.id.download);
        newupdate=findViewById(R.id.newupdate);
        newdate=findViewById(R.id.newdate);
        newFeature=findViewById(R.id.newfeatures);
        updateResult=findViewById(R.id.updateResult);
        lnf=findViewById(R.id.lnf);
        lur=findViewById(R.id.lur);
        versionC=BuildConfig.VERSION_CODE;
    }
    public void Back(View v){finish();}
    public void Update(View v){
        lur.setVisibility(View.VISIBLE);
        DateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm aa", Locale.getDefault());
        date = df.format(Calendar.getInstance().getTime());
        if (isNetworkConnected()){
            lastCheck.setText(String.format("সর্বশেষ চেক করা হয়েছে\n%s", date));
            dbref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot s) {
                    UApp u=s.getValue(UApp.class);
                    name=u.getName();
                    code = u.getCode();
                    url=u.getLink();
                    newAppDate = u.getDate();
                    NewFeature = u.getFeature();
                    newupdate.setText(name);
                    newdate.setText(newAppDate);
                    newFeature.setText(NewFeature);
                    lnf.setVisibility(View.VISIBLE);
                    if (code<=versionC) {
                        updateResult.setText("আপনার অ্যাপ আপডেট আছে");
                        Download.setVisibility(View.GONE);
                    } else {
                        updateResult.setText("নতুন আপডেট ডাউনলোড করুন");
                        Download.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });}
    }
    public void Download(View v){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            String FileName= String.format("ALB Staff-%s.apk",BuildConfig.VERSION_NAME);
            if (isNetworkConnected()){downloadFile(FileName,url);
            }else{Toast.makeText(this, "ইন্টারনেট কানেকশন নেই!", Toast.LENGTH_SHORT).show();}
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_PERMISSION);
            }
        }
    }
    private boolean isNetworkConnected(){ConnectivityManager cm=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);return cm.getActiveNetworkInfo()!=null&&cm.getActiveNetworkInfo().isConnected();}
    private void downloadFile(String fileName, String url) {
        Download.setVisibility(View.GONE);
        Uri uri=Uri.parse(url);
        DownloadManager manager=(DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        try {
            if (manager != null) {
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setTitle(fileName);
                request.setDescription("App Update");
                request.setNotificationVisibility(1);
                request.allowScanningByMediaScanner();
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                manager.enqueue(request);
                Toast.makeText(this, "ডাউনলোড শুরু হয়েছে!", Toast.LENGTH_SHORT).show();
                Download.setVisibility(View.VISIBLE);
            }else{
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
            }
        }catch(Exception e){
            Toast.makeText(this, "সমস্যা হয়েছে! আবার চেষ্টা করুন", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==WRITE_PERMISSION){
            if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                String FileName= String.format("ALB Staff-%s.apk",BuildConfig.VERSION_NAME);
                downloadFile(FileName,url);
            }else{
                Toast.makeText(this,"অনুমতি বাতিল করা হয়েছে!",Toast.LENGTH_SHORT).show();
            }
        }
    }
}