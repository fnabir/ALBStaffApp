package com.asianliftbd.staff.user;
public class UCallback {
    private String name,site,details,date,dbdate,uid,key;
    public UCallback(){}
    public UCallback(String name, String site, String details,String date,String dbdate,String uid,String key) {
        this.name = name;
        this.site = site;
        this.details = details;
        this.date = date;
        this.dbdate = dbdate;
        this.uid = uid;
        this.key = key;
    }
    public String getName() {return name;}
    public String getSite() {return site;}
    public String getDetails() {return details;}
    public String getDate() {return date;}
    public String getDbdate() {return dbdate;}
    public String getUid(){return uid;}
    public String getKey(){return key;}
}
