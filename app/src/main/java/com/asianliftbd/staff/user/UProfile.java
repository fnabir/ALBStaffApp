package com.asianliftbd.staff.user;

public class UProfile {
    public String name,phone;
    public UProfile(){}
    public UProfile(String name, String number){
        this.name =name;
        this.phone =number;
    }

    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}