package com.asianliftbd.staff.user;
import com.google.firebase.database.IgnoreExtraProperties;
@IgnoreExtraProperties public class UContact {
    private String name, title, phone, email;
    private int roll;
    public UContact(){}
    public UContact(String name, String title, String phone, String email,int roll) {
        this.name = name;
        this.title = title;
        this.phone = phone;
        this.email = email;
        this.roll = roll;
    }
    public String getName(){return name;}
    public String getTitle(){return title;}
    public String getPhone(){return phone;}
    public String getEmail(){return email;}
    public int getRoll(){return roll;}
}

