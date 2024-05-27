package com.asianliftbd.staff;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class Error extends AppCompatActivity {
    private TextView code,description,cause,level, solution;
    private EditText error;
    private LinearLayout l;
    private Button view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.error);
        code=findViewById(R.id.code);
        description=findViewById(R.id.description);
        cause=findViewById(R.id.cause);
        level=findViewById(R.id.level);
        solution=findViewById(R.id.solution);
        error=findViewById(R.id.no);
        view=findViewById(R.id.view);
        l=findViewById(R.id.le);
        error.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int st, int ct, int a) {}
            @Override
            public void onTextChanged(CharSequence s, int st, int b, int ct){l.setVisibility(View.GONE);view.setVisibility(View.VISIBLE);}
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    public void View(View v){
        String no = error.getText().toString();
        if (no.isEmpty()){
            Snack(v, "সমস্যার কোড লিখুন");
        }else{
            switch (no){
                case "1":
                    code.setText(R.string.e1);
                    description.setText(R.string.e1d);
                    cause.setText(R.string.e1c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e1s);
                    break;
                case "2":
                    code.setText(R.string.e2);
                    description.setText(R.string.e2d);
                    cause.setText(R.string.e2c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e234s);
                    break;
                case "3":
                    code.setText(R.string.e3);
                    description.setText(R.string.e3d);
                    cause.setText(R.string.e3c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e234s);
                    break;
                case "4":
                    code.setText(R.string.e4);
                    description.setText(R.string.e4d);
                    cause.setText(R.string.e4c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e234s);
                    break;
                case "5":
                    code.setText(R.string.e5);
                    description.setText(R.string.e5d);
                    cause.setText(R.string.e5c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e567s);
                    break;
                case "6":
                    code.setText(R.string.e6);
                    description.setText(R.string.e6d);
                    cause.setText(R.string.e6c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e567s);
                    break;
                case "7":
                    code.setText(R.string.e7);
                    description.setText(R.string.e7d);
                    cause.setText(R.string.e7c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e567s);
                    break;
                case "8":
                    code.setText(R.string.e8);
                    description.setText(R.string.e8d);
                    cause.setText(R.string.e8c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e8s);
                    break;
                case "9":
                    code.setText(R.string.e9);
                    description.setText(R.string.e9d);
                    cause.setText(R.string.e9c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e9s);
                    break;
                case "10":
                    code.setText(R.string.e10);
                    description.setText(R.string.e10d);
                    cause.setText(R.string.e10c);
                    level.setText(R.string.el41);
                    solution.setText(R.string.e10s);
                    break;
                case "11":
                    code.setText(R.string.e11);
                    description.setText(R.string.e11d);
                    cause.setText(R.string.e11c);
                    level.setText(R.string.el31);
                    solution.setText(R.string.e11s);
                    break;
                case "12":
                    code.setText(R.string.e12);
                    description.setText(R.string.e12d);
                    cause.setText(R.string.e12c);
                    level.setText(R.string.el41);
                    solution.setText(R.string.e12s);
                    break;
                case "13":
                    code.setText(R.string.e13);
                    description.setText(R.string.e13d);
                    cause.setText(R.string.e13c);
                    level.setText(R.string.el41);
                    solution.setText(R.string.e13s);
                    break;
                case "14":
                    code.setText(R.string.e14);
                    description.setText(R.string.e14d);
                    cause.setText(R.string.e14c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e14s);
                    break;
                case "15":
                    code.setText(R.string.e15);
                    description.setText(R.string.e15d);
                    cause.setText(R.string.e15c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e15s);
                    break;
                case "16":
                    code.setText(R.string.e16);
                    description.setText(R.string.e16d);
                    cause.setText(R.string.e16c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e16s);
                    break;
                case "17":
                    code.setText(R.string.e17);
                    description.setText(R.string.e17d);
                    cause.setText(R.string.e17c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e17s);
                    break;
                case "18":
                    code.setText(R.string.e18);
                    description.setText(R.string.e18d);
                    cause.setText(R.string.e18c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e18s);
                    break;
                case "19":
                    code.setText(R.string.e19);
                    description.setText(R.string.e19d);
                    cause.setText(R.string.e19c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e19s);
                    break;
                case "20":
                    code.setText(R.string.e20);
                    description.setText(R.string.e20d);
                    cause.setText(R.string.e20c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e20s);
                    break;
                case "22":
                    code.setText(R.string.e22);
                    description.setText(R.string.e22d);
                    cause.setText(R.string.e22c);
                    level.setText(R.string.el1);
                    solution.setText(R.string.e22s);
                    break;
                case "23":
                    code.setText(R.string.e23);
                    description.setText(R.string.e23d);
                    cause.setText(R.string.e23c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e23s);
                    break;
                case "24":
                    code.setText(R.string.e24);
                    description.setText(R.string.e24d);
                    cause.setText(R.string.e24c);
                    level.setText(R.string.el32);
                    solution.setText(R.string.e24s);
                    break;
                case "25":
                    code.setText(R.string.e25);
                    description.setText(R.string.e25d);
                    cause.setText(R.string.e25c);
                    level.setText(R.string.el41);
                    solution.setText(R.string.e25s);
                    break;
                case "26":
                    code.setText(R.string.e26);
                    description.setText(R.string.e26d);
                    cause.setText(R.string.e26c);
                    level.setText(R.string.el32);
                    solution.setText(R.string.e26s);
                    break;
                case "29":
                    code.setText(R.string.e29);
                    description.setText(R.string.e29d);
                    cause.setText(R.string.e29c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e29s);
                    break;
                case "30":
                    code.setText(R.string.e30);
                    description.setText(R.string.e30d);
                    cause.setText(R.string.e30c);
                    level.setText(R.string.el41);
                    solution.setText(R.string.e30s);
                    break;
                case "33":
                    code.setText(R.string.e33);
                    description.setText(R.string.e33d);
                    cause.setText(R.string.e33c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e33s);
                    break;
                case "34":
                    code.setText(R.string.e34);
                    description.setText(R.string.e34d);
                    cause.setText(R.string.e34c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e34s);
                    break;
                case "35":
                    code.setText(R.string.e35);
                    description.setText(R.string.e35d);
                    cause.setText(R.string.e35c);
                    level.setText(R.string.el43);
                    solution.setText(R.string.e35s);
                    break;
                case "36":
                    code.setText(R.string.e36);
                    description.setText(R.string.e36d);
                    cause.setText(R.string.e36c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e36s);
                    break;
                case "37":
                    code.setText(R.string.e37);
                    description.setText(R.string.e37d);
                    cause.setText(R.string.e37c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e37s);
                    break;
                case "38":
                    code.setText(R.string.e38);
                    description.setText(R.string.e38d);
                    cause.setText(R.string.e38c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e38s);
                    break;
                case "39":
                    code.setText(R.string.e39);
                    description.setText(R.string.e39d);
                    cause.setText(R.string.e39c);
                    level.setText(R.string.el31);
                    solution.setText(R.string.e39s);
                    break;
                case "41":
                    code.setText(R.string.e41);
                    description.setText(R.string.e41d);
                    cause.setText(R.string.e41c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e41s);
                    break;
                case "42":
                    code.setText(R.string.e42);
                    description.setText(R.string.e42d);
                    cause.setText(R.string.e42c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e42s);
                    break;
                case "43":
                    code.setText(R.string.e43);
                    description.setText(R.string.e43d);
                    cause.setText(R.string.e43c);
                    level.setText(R.string.el43);
                    solution.setText(R.string.e43s);
                    break;
                case "44":
                    code.setText(R.string.e44);
                    description.setText(R.string.e44d);
                    cause.setText(R.string.e44c);
                    level.setText(R.string.el43);
                    solution.setText(R.string.e44s);
                    break;
                case "45":
                    code.setText(R.string.e45);
                    description.setText(R.string.e45d);
                    cause.setText(R.string.e45c);
                    level.setText(R.string.el42);
                    solution.setText(R.string.e45s);
                    break;
                case "46":
                    code.setText(R.string.e46);
                    description.setText(R.string.e46d);
                    cause.setText(R.string.e46c);
                    level.setText(R.string.el22);
                    solution.setText(R.string.e46s);
                    break;
                case "47":
                    code.setText(R.string.e47);
                    description.setText(R.string.e47d);
                    cause.setText(R.string.e47c);
                    level.setText(R.string.el22);
                    solution.setText(R.string.e47s);
                    break;
                case "48":
                    code.setText(R.string.e48);
                    description.setText(R.string.e48d);
                    cause.setText(R.string.e48c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e48s);
                    break;
                case "49":
                    code.setText(R.string.e49);
                    description.setText(R.string.e49d);
                    cause.setText(R.string.e49c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e49s);
                    break;
                case "50":
                    code.setText(R.string.e50);
                    description.setText(R.string.e50d);
                    cause.setText(R.string.e50c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e50s);
                    break;
                case "51":
                    code.setText(R.string.e51);
                    description.setText(R.string.e51d);
                    cause.setText(R.string.e51c);
                    level.setText(R.string.el1);
                    solution.setText(R.string.e51s);
                    break;
                case "52":
                    code.setText(R.string.e52);
                    description.setText(R.string.e52d);
                    cause.setText(R.string.e52c);
                    level.setText(R.string.el1);
                    solution.setText(R.string.e52s);
                    break;
                case "53":
                    code.setText(R.string.e53);
                    description.setText(R.string.e53d);
                    cause.setText(R.string.e53c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e53s);
                    break;
                case "54":
                    code.setText(R.string.e54);
                    description.setText(R.string.e54d);
                    cause.setText(R.string.e54c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e54s);
                    break;
                case "55":
                    code.setText(R.string.e55);
                    description.setText(R.string.e55d);
                    cause.setText(R.string.e55c);
                    level.setText(R.string.el1);
                    solution.setText(R.string.e55s);
                    break;
                case "57":
                    code.setText(R.string.e57);
                    description.setText(R.string.e57d);
                    cause.setText(R.string.e57c);
                    level.setText(R.string.el51);
                    solution.setText(R.string.e57s);
                    break;
                case "58":
                    code.setText(R.string.e58);
                    description.setText(R.string.e58d);
                    cause.setText(R.string.e58c);
                    level.setText(R.string.el42);
                    solution.setText(R.string.e58s);
                    break;
                case "62":
                    code.setText(R.string.e62);
                    description.setText(R.string.e62d);
                    cause.setText(R.string.e62c);
                    level.setText(R.string.el1);
                    solution.setText(R.string.e62s);
                    break;
                default:
                    code.setText("কোড পাওয়া যায়নি");
                    Snack(v, "কোড পাওয়া যায়নি!");
                    break;
            }
            String Level=level.getText().toString();
            if (Level.equals(getString(R.string.el51))||Level.equals(getString(R.string.el52))){level.setTextColor(Color.parseColor("#dc3545"));}
            else if (Level.equals(getString(R.string.el21))||Level.equals(getString(R.string.el22))){level.setTextColor(Color.parseColor("#2699fb"));}
            else if (Level.equals(getString(R.string.el1))){level.setTextColor(Color.parseColor("#28a745"));}
            else {level.setTextColor(Color.parseColor("#b88a00"));}
            if (code.getText().toString().equals("কোড পাওয়া যায়নি")){
                l.setVisibility(View.GONE);
            }else{l.setVisibility(View.VISIBLE);}
        }
    }
    public void Back(View v){finish();}
    public void Snack(View v, String t){
        final Snackbar s = Snackbar.make(v,t,Snackbar.LENGTH_SHORT);
        @SuppressLint("InflateParams")View custom=LayoutInflater.from(this).inflate(R.layout.m_snack,null );
        s.getView().setPadding(0,0,0,0);
        s.setBackgroundTint(Color.TRANSPARENT);
        ((ViewGroup) s.getView()).removeAllViews();
        ((ViewGroup) s.getView()).addView(custom);
        TextView tv = custom.findViewById(R.id.text);
        tv.setText(t);tv.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { s.dismiss(); }});
        s.show();
    }
}
