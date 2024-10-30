package com.example.easyshopping.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "cartlist")
public class CartModel {


    @PrimaryKey(autoGenerate = true)
    private int Cart_Id ;

    @ColumnInfo
    public String Cart_Name;

    @ColumnInfo
    public String Cart_Price;

    @ColumnInfo
    public String Cart_Rating;

    @ColumnInfo
    public String Cart_cat;

    @ColumnInfo
    public String Cart_img;
    @ColumnInfo
    public String Cart_img1;

    @ColumnInfo
    public String Cart_img2;

    @ColumnInfo
    public String Cart_img3;

    @ColumnInfo
    public String Cart_img4;

    @ColumnInfo
    public String Cart_img5;

    @ColumnInfo
    public String Cart_UserID;

    @ColumnInfo
    public String Cart_Fire_PR_ID;

    @ColumnInfo
    public String Cart_Item_Qty;


    public CartModel() {
    }

    public void setCart_Id(int cart_Id) {
        Cart_Id = cart_Id;
    }

    public String getCart_Item_Qty() {
        return Cart_Item_Qty;
    }

    public void setCart_Item_Qty(String cart_Item_Qty) {
        Cart_Item_Qty = cart_Item_Qty;
    }

    public int getCart_Id() {
        return Cart_Id;
    }

    public CartModel(String cart_Name, String cart_Price, String cart_Rating, String cart_cat, String cart_img,
                     String cart_img1, String cart_img2, String cart_img3, String cart_img4, String cart_img5,
                     String cart_UserID, String cart_Fire_PR_ID,String Cart_Item_Qty) {

        this.Cart_Name = cart_Name;
        this.Cart_Price = cart_Price;
        this.Cart_Rating = cart_Rating;
        this.Cart_cat = cart_cat;
        this.Cart_img = cart_img;
        this.Cart_img1 = cart_img1;
        this.Cart_img2 = cart_img2;
        this.Cart_img3 = cart_img3;
        this.Cart_img4 = cart_img4;
        this.Cart_img5 = cart_img5;
        this.Cart_UserID = cart_UserID;
        this.Cart_Fire_PR_ID = cart_Fire_PR_ID;
        this.Cart_Item_Qty = Cart_Item_Qty;
    }

}
