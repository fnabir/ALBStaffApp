package com.asianliftbd.staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asianliftbd.staff.db.HCallback;
import com.asianliftbd.staff.user.UTC;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Callback extends AppCompatActivity {
    private TextView Empty;
    private ScrollView l;
    private RecyclerView rv;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("callback/total");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.callback);
        l=findViewById(R.id.l);
        Empty=findViewById(R.id.empty);
        rv=findViewById(R.id.rv);
        LinearLayoutManager LM = new LinearLayoutManager(Callback.this);
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
                    FirebaseRecyclerOptions<UTC> options =
                            new FirebaseRecyclerOptions.Builder<UTC>()
                                    .setQuery(ref.orderByChild("name"), UTC.class)
                                    .build();
                    FirebaseRecyclerAdapter<UTC, HCallback> a = new FirebaseRecyclerAdapter<UTC, HCallback>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull HCallback vh, int position, @NonNull UTC ut) {
                            vh.setDetails(getApplicationContext(),ut.getName(),ut.getValue());
                        }
                        @NonNull
                        @Override
                        public HCallback onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.m_callback, parent, false);
                            return new HCallback(view);
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