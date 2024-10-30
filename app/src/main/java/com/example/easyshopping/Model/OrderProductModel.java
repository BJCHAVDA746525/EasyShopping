package com.example.easyshopping.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Ignore;

public class OrderProductModel implements Parcelable {

    String ProductName;
    String ProductPrice;
    String ProductQty;


    public OrderProductModel() {
    }

    @Ignore
    public OrderProductModel(String productName, String productPrice, String productQty) {
        ProductName = productName;
        ProductPrice = productPrice;
        ProductQty = productQty;
    }

    protected OrderProductModel(Parcel in) {
        ProductName = in.readString();
        ProductPrice = in.readString();
        ProductQty = in.readString();
    }

    public static final Creator<OrderProductModel> CREATOR = new Creator<OrderProductModel>() {
        @Override
        public OrderProductModel createFromParcel(Parcel in) {
            return new OrderProductModel(in);
        }

        @Override
        public OrderProductModel[] newArray(int size) {
            return new OrderProductModel[size];
        }
    };

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getProductQty() {
        return ProductQty;
    }

    public void setProductQty(String productQty) {
        ProductQty = productQty;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(ProductName);
        dest.writeString(ProductPrice);
        dest.writeString(ProductQty);
    }
}
