package com.asianliftbd.staff.user;

public class UDevice {
    private String model,device,manufacturer,brand,android,time,app;
    public UDevice(){}

    public UDevice(String model, String device, String manufacturer, String brand, String android, String time, String app) {
        this.model = model;
        this.device = device;
        this.manufacturer = manufacturer;
        this.brand = brand;
        this.android = android;
        this.time = time;
        this.app = app;
    }
    public String getModel() { return model;}
    public String getDevice() {return device;}
    public String getManufacturer() {return manufacturer;}
    public String getBrand() {return brand;}
    public String getAndroid() {return android;}
    public String getTime() {return time;}
    public String getApp() {return app;}
}
