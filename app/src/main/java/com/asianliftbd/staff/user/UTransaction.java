package com.asianliftbd.staff.user;
import com.google.firebase.database.IgnoreExtraProperties;
@IgnoreExtraProperties public class UTransaction {
    private String date,title,details;private int amount;
    public UTransaction(){}
    public String getTitle(){return title;}
    public String getDate(){return date;}
    public String getDetails(){return details;}
    public int getAmount(){return amount;}
}