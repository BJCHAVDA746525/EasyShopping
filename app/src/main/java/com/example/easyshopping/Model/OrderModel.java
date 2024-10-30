package com.example.easyshopping.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Ignore;

import java.util.ArrayList;

public class OrderModel extends ArrayList<OrderModel> implements Parcelable {

    String UserName;
    String UserID;

    ArrayList<OrderProductModel> ProductList;

    AddressModel address;

    String TotalCartPrice;
    String PaymentDone;

    String OrderTime;


    public OrderModel() {

    }

    protected OrderModel(Parcel in) {
        UserName = in.readString();
        UserID = in.readString();
        TotalCartPrice = in.readString();
        PaymentDone = in.readString();
        OrderTime = in.readString();
        address = in.readParcelable(AddressModel.class.getClassLoader());
        ProductList = in.createTypedArrayList(OrderProductModel.CREATOR);
    }

    public static final Creator<OrderModel> CREATOR = new Creator<OrderModel>() {
        @Override
        public OrderModel createFromParcel(Parcel in) {
            return new OrderModel(in);
        }

        @Override
        public OrderModel[] newArray(int size) {
            return new OrderModel[size];
        }
    };

    public String getPaymentDone() {
        return PaymentDone;
    }

    public void setPaymentDone(String paymentDone) {
        PaymentDone = paymentDone;
    }

    @Ignore
    public OrderModel(String userName, String userID, ArrayList<OrderProductModel> productList, AddressModel address) {
        this.UserName = userName;
        this.UserID = userID;
        this.ProductList = productList;
        this.address = address;
    }

    @Ignore
    public OrderModel(String userName, String userID, ArrayList<OrderProductModel> productList,String TotalCartPrice) {
        this.UserName = userName;
        this.UserID = userID;
        this.ProductList = productList;
        this.TotalCartPrice = TotalCartPrice;
    }

    public String getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(String orderTime) {
        OrderTime = orderTime;
    }

    public String getTotalCartPrice() {
        return TotalCartPrice;
    }

    public void setTotalCartPrice(String totalCartPrice) {
        TotalCartPrice = totalCartPrice;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public ArrayList<OrderProductModel> getProductList() {
        return ProductList;
    }

    public void setProductList(ArrayList<OrderProductModel> productList) {
        ProductList = productList;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(UserName);
        dest.writeString(UserID);
        dest.writeString(TotalCartPrice);
        dest.writeString(PaymentDone);
        dest.writeString(OrderTime);
        dest.writeParcelable(address, flags);
        dest.writeTypedList(ProductList);
    }
}
