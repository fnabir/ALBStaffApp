package com.asianliftbd.staff.user;
public class UOffer {
    private String name,address,ptype,wtype,unit,floor,shaft, person,note,refer,date,key,uid;
    public UOffer(){}
    public UOffer(String name, String address, String ptype, String wtype, String unit, String floor, String shaft, String person, String note, String refer, String date, String key,String uid){
        this.name = name;
        this.address = address;
        this.ptype = ptype;
        this.wtype = wtype;
        this.unit = unit;
        this.floor = floor;
        this.shaft = shaft;
        this.person = person;
        this.note = note;
        this.refer = refer;
        this.date = date;
        this.key = key;
        this.uid = uid;
    }
    public String getKey(){return key;}
    public String getUnit(){return unit;}
    public String getDate(){return date;}
    public String getName() {return name;}
    public String getAddress() {return address;}
    public String getPtype(){return ptype;}
    public String getWtype(){return wtype;}
    public String getFloor() {return floor;}
    public String getShaft() {return shaft;}
    public String getPerson() {return person;}
    public String getNote() {return note;}
    public String getRefer() {return refer;}
    public String getUid(){return uid;}
}