package com.asianliftbd.staff;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asianliftbd.staff.db.HConv;
import com.asianliftbd.staff.user.UBal;
import com.asianliftbd.staff.user.UConv;
import com.asianliftbd.staff.user.UInfo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class ConvList extends AppCompatActivity {
    private final String uid= FirebaseAuth.getInstance().getUid(),name=FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    private String date;
    private int roll;
    private TextView Empty,Total;
    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(),
            rvref=ref.child("transaction/conveyance/"+uid),
            bref=ref.child("balance/conveyance/"+uid),
            rollref=ref.child("user/"+uid+"/info/info");;
    private ScrollView l;
    private LinearLayout lt;
    private RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.conv_list);
        l=findViewById(R.id.l);
        Empty=findViewById(R.id.empty);
        Total=findViewById(R.id.amount);
        rv=findViewById(R.id.rv);
        lt=findViewById(R.id.lt);
        LinearLayoutManager LM = new LinearLayoutManager(ConvList.this);
        LM.setReverseLayout(true);
        LM.setStackFromEnd(true);
        rv.setLayoutManager(LM);
        rv.setNestedScrollingEnabled(false);
        bref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot d) {
                UBal u=d.getValue(UBal.class);
                if (u != null) {
                    date=u.getDate();
                }
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
    @Override
    public void onStart() {
        super.onStart();
        rvref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot d) {
                final int[] t = {0};
                if (!d.exists()) {
                    Empty.setVisibility(View.VISIBLE);
                    l.setVisibility(View.GONE);
                    lt.setVisibility(View.GONE);
                    UBal u=new UBal(name,0,date,uid,roll);
                    bref.setValue(u);
                } else {
                    l.setVisibility(View.VISIBLE);
                    lt.setVisibility(View.VISIBLE);
                    Empty.setVisibility(View.GONE);
                    FirebaseRecyclerOptions<UConv> options =
                            new FirebaseRecyclerOptions.Builder<UConv>()
                                    .setQuery(rvref.orderByChild("dbdate"), UConv.class)
                                    .build();
                    FirebaseRecyclerAdapter<UConv, HConv> a = new FirebaseRecyclerAdapter<UConv, HConv>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull HConv vh, int position, @NonNull UConv ut) {
                            t[0] += Integer.parseInt(ut.getAmount());
                            Total.setText(String.format(Locale.getDefault(),"৳ %d",t[0]));
                            UBal u=new UBal(name,t[0],date,uid,roll);
                            bref.setValue(u);
                            vh.setDetails(ut.getDate(),ut.getSite(),ut.getType(),ut.getRef(),ut.getAmount());
                        }
                        @NonNull
                        @Override
                        public HConv onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.m_transaction, parent, false);
                            return new HConv(view);
                        }
                    };
                    rv.setAdapter(a);
                    a.startListening();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError e) {
            }
        });
    }
    public void Back(View v){finish();}
}