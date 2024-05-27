package com.asianliftbd.staff.user;
public class UConv {
    private String name,site,type,ref,amount,date,dbdate;
    public UConv(){}
    public UConv(String name, String site, String type, String ref, String amount, String date, String dbdate) {
        this.name = name;
        this.site = site;
        this.type = type;
        this.ref = ref;
        this.amount = amount;
        this.date = date;
        this.dbdate = dbdate;
    }
    public String getName(){return name;}
    public String getSite() {return site;}
    public String getType() {return type;}
    public String getRef() {return ref;}
    public String getAmount() {return amount;}
    public String getDate() {return date;}
    public String getDbdate() {return dbdate;}
}
