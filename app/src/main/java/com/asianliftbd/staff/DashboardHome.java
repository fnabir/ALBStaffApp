package com.asianliftbd.staff;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.asianliftbd.staff.user.UBal;
import com.asianliftbd.staff.user.UProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
public class DashboardHome extends Fragment {
    private TextView Name,B,Cb,Tb,Tc,Rc,Oc;
    public static long C;
    public String uid= FirebaseAuth.getInstance().getUid();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dashboard_home, container, false);
        Name=root.findViewById(R.id.name);
        B=root.findViewById(R.id.b);
        Cb=root.findViewById(R.id.cb);
        Tb=root.findViewById(R.id.tb);
        Tc=root.findViewById(R.id.tcb);
        ref.child("user/"+uid+"/profile").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot d) {
                UProfile u=d.getValue(UProfile.class);
                if (u != null) {
                    Name.setText(u.getName());
                    UserProfileChangeRequest updt = new UserProfileChangeRequest.Builder()
                            .setDisplayName(u.getName()).build();
                    user.updateProfile(updt);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError e) {}
        });
        ref.child("balance/staff").child(uid).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot s){
                UBal u=s.getValue(UBal.class);
                if (u != null){
                    amountFormat(B,u.getValue());
                    Tb.setText(u.getDate());
                }else{
                    B.setText("৳ 0");
                    Tb.setText("-");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError e){}
        });
        ref.child("balance/conveyance/"+uid).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot s){
                UBal u=s.getValue(UBal.class);
                if (u != null) {
                    amountFormat(Cb,u.getValue());
                    Tc.setText(u.getDate());
                }else{
                    Cb.setText("৳ 0");
                    Tc.setText("-");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError e){}
        });
        Oc=root.findViewById(R.id.oc);
        DatabaseReference oc = ref.child("-offer");
        oc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot s) {
                int c= (int) s.getChildrenCount();
                Oc.setText(String.valueOf(c));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError e) {}
        });
        TextView wish = root.findViewById(R.id.wish);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.getDefault());
        int temp = Integer.parseInt(sdf.format(calendar.getTimeInMillis()));
        if (temp<6)wish.setText("শুভ রাত্রি");
        else if (temp>=6&&temp<12)wish.setText("শুভ সকাল");
        else if(temp>18 && temp <24) wish.setText("শুভ সন্ধ্যা");
        else wish.setText("শুভ বিকাল");
        LinearLayout lb=root.findViewById(R.id.lb);
        lb.setOnClickListener(v -> {singleClick();startActivity(new Intent(getActivity(),Transaction.class));});
        ImageView conv=root.findViewById(R.id.add_conveyance);
        conv.setOnClickListener(v -> {singleClick();startActivity(new Intent(getActivity(),Conveyance.class));});
        LinearLayout lc=root.findViewById(R.id.lc);
        lc.setOnClickListener(v -> {singleClick();startActivity(new Intent(getActivity(), ConvList.class));});
        LinearLayout lr=root.findViewById(R.id.lr);
        lr.setOnClickListener(v -> {singleClick();startActivity(new Intent(getActivity(), Callback.class));});
        LinearLayout lo=root.findViewById(R.id.lo);
        lo.setOnClickListener(v -> {singleClick();startActivity(new Intent(getActivity(), OfferList.class));});
        LinearLayout le=root.findViewById(R.id.le);
        le.setOnClickListener(v -> {singleClick();startActivity(new Intent(getActivity(),Error.class));});
        return root;
    }
    public static void amountFormat(TextView t,int number) {
        NumberFormat amount=NumberFormat.getInstance();
        if (amount instanceof DecimalFormat) {
            DecimalFormat d = (DecimalFormat) amount;
            d.applyPattern("##,##,###");
            d.setGroupingUsed(true);
            int absNumber=Math.abs(number);
            if (number<0) {
                t.setText(String.format("- ৳ %s", amount.format(absNumber)));
            }else t.setText(String.format("৳ %s", amount.format(absNumber)));
        }
    }
    public static void singleClick(){
        if(SystemClock.elapsedRealtime()-C<1000){return;}
        C=SystemClock.elapsedRealtime();
    }
}