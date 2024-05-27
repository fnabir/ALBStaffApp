package com.asianliftbd.staff;

import static com.asianliftbd.staff.DashboardHome.amountFormat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.asianliftbd.staff.db.HTransaction;
import com.asianliftbd.staff.user.UBal;
import com.asianliftbd.staff.user.UInfo;
import com.asianliftbd.staff.user.UTransaction;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class Transaction extends AppCompatActivity {
    private String uid= FirebaseAuth.getInstance().getUid(),name=FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),date;
    private TextView Empty,Total,Conveyance;
    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(),
            rvref=ref.child("transaction/staff/"+uid),
            bref=ref.child("balance/staff/"+uid),
            cref=ref.child("balance/conveyance/"+uid),
            rollref=ref.child("user/"+uid+"/info/info");
    private ScrollView l;
    private int conveyance,sum,roll;
    private LinearLayout lt,lc;
    private RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.transaction);
        l=findViewById(R.id.l);
        Empty=findViewById(R.id.empty);
        Total=findViewById(R.id.amount);
        Conveyance=findViewById(R.id.conveyance);
        rv=findViewById(R.id.rv);
        lt=findViewById(R.id.lt);
        lc=findViewById(R.id.lc);
        LinearLayoutManager LM = new LinearLayoutManager(Transaction.this);
        LM.setReverseLayout(true);
        LM.setStackFromEnd(true);
        rv.setLayoutManager(LM);
        rv.setNestedScrollingEnabled(false);
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
        cref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot d) {
                if (!d.exists()) {
                    conveyance=0;
                    Conveyance.setText(String.format("৳ %s", conveyance));
                }else{
                    UBal u=d.getValue(UBal.class);
                    if (u != null) {
                        conveyance=u.getValue();
                    }else conveyance=0;
                    amountFormat(Conveyance,conveyance);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError e) {}
        });
        bref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot d) {
                UBal u=d.getValue(UBal.class);
                if (u != null) {
                    date=u.getDate();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError e) {}
        });
        lc.setOnClickListener(v -> startActivity(new Intent(Transaction.this, ConvList.class)));
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
                    Empty.setPadding(0,0,0,99);
                    if (conveyance>0){Empty.setText("শুধু কনভেন্স পাওয়া গেছে।");
                        lt.setVisibility(View.VISIBLE);
                        lc.setVisibility(View.VISIBLE);
                    }else{
                    l.setVisibility(View.GONE);
                    lt.setVisibility(View.GONE);
                    lc.setVisibility(View.GONE);}
                    sum=conveyance+t[0];
                    Total.setText(String.format(Locale.getDefault(),"৳ %d",sum));
                    UBal u=new UBal(name,sum,date,uid,roll);
                    bref.setValue(u);
                }else {
                    l.setVisibility(View.VISIBLE);
                    lt.setVisibility(View.VISIBLE);
                    lc.setVisibility(View.VISIBLE);
                    Empty.setVisibility(View.GONE);
                    FirebaseRecyclerOptions<UTransaction> options =
                            new FirebaseRecyclerOptions.Builder<UTransaction>()
                                    .setQuery(rvref, UTransaction.class)
                                    .build();
                    FirebaseRecyclerAdapter<UTransaction, HTransaction> adapter = new FirebaseRecyclerAdapter<UTransaction, HTransaction>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull HTransaction vh, int position, @NonNull UTransaction ut) {
                            t[0] +=ut.getAmount();
                            sum=conveyance+t[0];
                            UBal u=new UBal(name,sum,date,uid,roll);
                            bref.setValue(u);
                            amountFormat(Total,sum);
                            vh.setDetails(ut.getDate(),ut.getTitle(),ut.getDetails(),ut.getAmount());
                        }
                        @NonNull
                        @Override
                        public HTransaction onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.m_transaction, parent, false);
                            return new HTransaction(view);
                        }
                    };
                    rv.setAdapter(adapter);
                    adapter.startListening();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError e) {
            }
        });
    }
    public void Back(View v){finish();}
}