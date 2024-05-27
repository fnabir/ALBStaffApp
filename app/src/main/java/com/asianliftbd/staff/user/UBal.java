package com.asianliftbd.staff.user;

public class UBal {private String name,date,uid;private int value,position;
public UBal(){}
public UBal(String name, int value, String date, String uid,int position){this.name = name;this.value = value;this.date = date;this.uid=uid;this.position=position;}
    public String getName(){return name;}public void setName(String name){this.name = name;}
    public int getValue(){return value;}public void setValue(int value){this.value=value;}
    public String getDate(){return date;}public void setDate(String date) {this.date = date;}
    public String getUid(){return uid;}public void setUid(String uid){this.uid = uid;}
    public int getPosition() {return position;}public void setPosition(int position) {this.position = position;}
}