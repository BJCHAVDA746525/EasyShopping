package com.example.easyshopping.orderModels;

import java.util.List;

public class OrderModelFire {
    private String orderTime;
    private String paymentDone;
    private String totalCartPrice;
    private String userID;
    private String userName;
    private AddressModelFire address;
    private List<ProductModelFire> productList;

    // Constructors
    public OrderModelFire() {
    }

    public OrderModelFire(String orderTime, String paymentDone, String totalCartPrice, String userID, String userName, AddressModelFire address, List<ProductModelFire> productList) {
        this.orderTime = orderTime;
        this.paymentDone = paymentDone;
        this.totalCartPrice = totalCartPrice;
        this.userID = userID;
        this.userName = userName;
        this.address = address;
        this.productList = productList;
    }

    // Getters and Setters
    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getPaymentDone() {
        return paymentDone;
    }

    public void setPaymentDone(String paymentDone) {
        this.paymentDone = paymentDone;
    }

    public String getTotalCartPrice() {
        return totalCartPrice;
    }

    public void setTotalCartPrice(String totalCartPrice) {
        this.totalCartPrice = totalCartPrice;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public AddressModelFire getAddress() {
        return address;
    }

    public void setAddress(AddressModelFire address) {
        this.address = address;
    }

    public List<ProductModelFire> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductModelFire> productList) {
        this.productList = productList;
    }
}
