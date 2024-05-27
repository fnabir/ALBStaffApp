package com.asianliftbd.staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asianliftbd.staff.db.HReport;
import com.asianliftbd.staff.user.UCallback;
import com.asianliftbd.staff.user.UTC;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CallbackProject extends AppCompatActivity {
    private String name;
    private TextView Empty,Project;
    private ScrollView l;
    private RecyclerView rv;
    private DatabaseReference ref,tref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.callback_project);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name=extras.getString("key");
        }
        ref = FirebaseDatabase.getInstance().getReference().child("callback/details/"+name);
        tref = FirebaseDatabase.getInstance().getReference().child("callback/total/"+name);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot s) {
                int count= (int) s.getChildrenCount();
                UTC u=new UTC(name,count);
                tref.setValue(u);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError e) {}
        });
        l=findViewById(R.id.l);
        Empty=findViewById(R.id.empty);
        Project=findViewById(R.id.project);
        Project.setText(name);
        rv=findViewById(R.id.rv);
        LinearLayoutManager LM = new LinearLayoutManager(CallbackProject.this);
        LM.setReverseLayout(true);
        LM.setStackFromEnd(true);
        rv.setLayoutManager(LM);
    }
    @Override
    public void onStart() {
        super.onStart();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot d) {
                if (!d.exists()) {
                    Empty.setVisibility(View.VISIBLE);
                    l.setVisibility(View.GONE);
                } else {
                    l.setVisibility(View.VISIBLE);
                    Empty.setVisibility(View.GONE);
                    FirebaseRecyclerOptions<UCallback> options =
                            new FirebaseRecyclerOptions.Builder<UCallback>()
                                    .setQuery(ref, UCallback.class)
                                    .build();
                    FirebaseRecyclerAdapter<UCallback, HReport> a = new FirebaseRecyclerAdapter<UCallback, HReport>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull HReport vh, int position, @NonNull UCallback ut) {
                            vh.setDetails(ut.getName(),ut.getDetails(),ut.getDate());
                        }
                        @NonNull
                        @Override
                        public HReport onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.m_report, parent, false);
                            return new HReport(view);
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
    public void Report(View v){startActivity(new Intent(this,Report.class));}
}