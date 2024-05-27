package com.asianliftbd.staff;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asianliftbd.staff.db.HContact;
import com.asianliftbd.staff.user.UContact;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Contact extends AppCompatActivity {
    private ProgressBar Empty;
    private RecyclerView rv;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("contact/alb");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.contact);
        Empty=findViewById(R.id.empty);
        rv=findViewById(R.id.rv);
        LinearLayoutManager LM = new LinearLayoutManager(Contact.this);
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
                    rv.setVisibility(View.GONE);
                } else {
                    rv.setVisibility(View.VISIBLE);
                    Empty.setVisibility(View.GONE);
                    FirebaseRecyclerOptions<UContact> options =
                            new FirebaseRecyclerOptions.Builder<UContact>()
                                    .setQuery(ref.orderByChild("roll"), UContact.class)
                                    .build();
                    FirebaseRecyclerAdapter<UContact, HContact> a = new FirebaseRecyclerAdapter<UContact, HContact>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull HContact vh, int p, @NonNull UContact u) {
                            vh.setDetails(getApplicationContext(),u.getName(),u.getTitle(),u.getPhone(),u.getEmail());
                        }
                        @NonNull
                        @Override
                        public HContact onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.m_contact, parent, false);
                            return new HContact(view);
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