package com.asianliftbd.staff;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.asianliftbd.staff.user.UInfo;
import com.asianliftbd.staff.user.UDevice;
import com.asianliftbd.staff.user.UProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DashboardMenu extends Fragment {
    private TextView Name,Phone,Title;
    private long C;
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private String uid=firebaseAuth.getUid();
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference(),
            nref=ref.child("user/"+uid+"/profile"),iref=ref.child("user/"+uid+"/info/info");
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dashboard_menu, container, false);
        Name=root.findViewById(R.id.name);
        Phone=root.findViewById(R.id.phone);
        Title=root.findViewById(R.id.title);
        TextView Mail=root.findViewById(R.id.mail);
        Mail.setText(user.getEmail());
        TextView fb=root.findViewById(R.id.fb);
        TextView web=root.findViewById(R.id.web);
        TextView version = root.findViewById(R.id.version_n);
        version.setText(String.format("%s v%s",getResources().getString(R.string.fullapp_name), BuildConfig.VERSION_NAME));
        nref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot d) {
                UProfile u=d.getValue(UProfile.class);
                Name.setText(u.getName());
                Phone.setText(u.getPhone());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError e) {}
        });
        iref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot d) {
                UInfo u=d.getValue(UInfo.class);
                Title.setText(u.getTitle());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError e) {}
        });
        TextView editProfile=root.findViewById(R.id.edit_profile);
        TextView change=root.findViewById(R.id.change_password);
        editProfile.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View v){if(SystemClock.elapsedRealtime()-C<1000){return;}C=SystemClock.elapsedRealtime();startActivity(new Intent(getActivity(),EditProfile.class));}});
        change.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View v){if(SystemClock.elapsedRealtime()-C<1000){return;}C=SystemClock.elapsedRealtime();startActivity(new Intent(getActivity(),ChangePassword.class));}});
        web.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View v){if(SystemClock.elapsedRealtime()-C<1000){return;}C=SystemClock.elapsedRealtime();String url="https://asianliftbd.com";Intent i=new Intent(Intent.ACTION_VIEW);i.setData(Uri.parse(url));startActivity(i); }});
        fb.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View v){if(SystemClock.elapsedRealtime()-C<1000){return;}C=SystemClock.elapsedRealtime();String url="https://www.facebook.com/asianliftbangladesh";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);}});
        TextView appInfo=root.findViewById(R.id.about_app);
        appInfo.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View v) {if(SystemClock.elapsedRealtime()-C<1000){return;}C=SystemClock.elapsedRealtime();startActivity(new Intent(getActivity(), AppInfo.class));}});
        TextView Logout=root.findViewById(R.id.logout);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String model = Build.MODEL;
                String device = Build.DEVICE;
                String manufacturer = Build.MANUFACTURER;
                String brand = Build.BRAND;
                String android = Build.VERSION.RELEASE;
                String version= BuildConfig.VERSION_NAME;
                DateFormat df = new SimpleDateFormat("dd.MM.yy hh:mm aa", Locale.getDefault());
                String date = df.format(Calendar.getInstance().getTime());
                DatabaseReference logoutref = ref.child("user").child(uid).child("info").child("logout");
                UDevice userDevice = new UDevice(model, device, manufacturer, brand, android, date,version);
                logoutref.setValue(userDevice);
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getActivity(), "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(), Start.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        return root;
    }
}