package com.example.easyshopping.orderModels;

public class AddressModelFire {
    private String addLine1;
    private String addLine2;
    private String city;
    private String fireID;
    private String isSelected;
    private String pinCode;
    private int roomID;
    private int stability;
    private String uid;

    // Constructors
    public AddressModelFire() {
    }

    public AddressModelFire(String addLine1, String addLine2, String city, String fireID, String isSelected, String pinCode, int roomID, int stability, String uid) {
        this.addLine1 = addLine1;
        this.addLine2 = addLine2;
        this.city = city;
        this.fireID = fireID;
        this.isSelected = isSelected;
        this.pinCode = pinCode;
        this.roomID = roomID;
        this.stability = stability;
        this.uid = uid;
    }

    // Getters and Setters
    public String getAddLine1() {
        return addLine1;
    }

    public void setAddLine1(String addLine1) {
        this.addLine1 = addLine1;
    }

    public String getAddLine2() {
        return addLine2;
    }

    public void setAddLine2(String addLine2) {
        this.addLine2 = addLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFireID() {
        return fireID;
    }

    public void setFireID(String fireID) {
        this.fireID = fireID;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public int getStability() {
        return stability;
    }

    public void setStability(int stability) {
        this.stability = stability;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
