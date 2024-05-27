package com.asianliftbd.staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asianliftbd.staff.db.HOffer;
import com.asianliftbd.staff.user.UOffer;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OfferList extends AppCompatActivity {
    private TextView Empty;
    private RelativeLayout l;
    private RecyclerView rv;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("-offer");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.offer_list);
        l=findViewById(R.id.l);
        Empty=findViewById(R.id.empty);
        rv=findViewById(R.id.rv);
        LinearLayoutManager LM = new LinearLayoutManager(OfferList.this);
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
                    FirebaseRecyclerOptions<UOffer> options =
                            new FirebaseRecyclerOptions.Builder<UOffer>()
                                    .setQuery(ref, UOffer.class)
                                    .build();
                    FirebaseRecyclerAdapter<UOffer, HOffer> a = new FirebaseRecyclerAdapter<UOffer, HOffer>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull HOffer vh, int position, @NonNull UOffer ut) {
                            vh.setDetails(getApplicationContext(),ut.getName(),ut.getAddress(),ut.getPtype(),ut.getWtype(),ut.getUnit(),ut.getFloor(),ut.getShaft(),ut.getPerson(),ut.getNote(),ut.getRefer(),ut.getDate(),ut.getKey(),ut.getUid());
                        }
                        @NonNull
                        @Override
                        public HOffer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.m_offer, parent, false);
                            return new HOffer(view);
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
    public void NewOffer(View v){startActivity(new Intent(this,Offer.class));}
}