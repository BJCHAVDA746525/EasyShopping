package com.example.easyshopping.Model;

public class CartListModel {


    public String UserID;

    public String Name;

    public String Cart_Item_Qty;

    public String Price;







    public CartListModel() {
    }

    public CartListModel(String cart_UserID, String cart_Name, String cart_Qty, String cart_Price ) {
        this.UserID = cart_UserID;
        this.Name = cart_Name;
        this.Cart_Item_Qty = cart_Qty;
        this.Price = cart_Price;
    }




}
