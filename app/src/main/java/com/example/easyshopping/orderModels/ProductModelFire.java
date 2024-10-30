package com.example.easyshopping.orderModels;

public class ProductModelFire {
    private String productName;
    private String productPrice;
    private String productQty;
    private int stability;

    // Constructors
    public ProductModelFire() {
    }

    public ProductModelFire(String productName, String productPrice, String productQty, int stability) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQty = productQty;
        this.stability = stability;
    }

    // Getters and Setters
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductQty() {
        return productQty;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }

    public int getStability() {
        return stability;
    }

    public void setStability(int stability) {
        this.stability = stability;
    }
}
