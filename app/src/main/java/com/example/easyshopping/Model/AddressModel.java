package com.example.easyshopping.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "AddressBook")
public class AddressModel implements Parcelable {


    @PrimaryKey(autoGenerate = true)
    private int RoomID ;

    @ColumnInfo
    private String AddLine1;

    @ColumnInfo
    private String AddLine2;

    @ColumnInfo
    private String City;

    @ColumnInfo
    private String PinCode;

    @ColumnInfo
    private String Uid;

    @ColumnInfo
    private String FireID;

    @ColumnInfo
    private String IsSelected;

    public AddressModel() {

    }




    @Ignore
    public AddressModel( String addLine1, String addLine2, String city, String pinCode, String uid, String fireID, String isSelected) {
        this.AddLine1 = addLine1;
        this.AddLine2 = addLine2;
        this.City = city;
        this.PinCode = pinCode;
        this.Uid = uid;
        this.FireID = fireID;
        this.IsSelected = isSelected;
    }

    protected AddressModel(Parcel in) {
        RoomID = in.readInt();
        AddLine1 = in.readString();
        AddLine2 = in.readString();
        City = in.readString();
        PinCode = in.readString();
        Uid = in.readString();
        FireID = in.readString();
        IsSelected = in.readString();
    }

    public static final Creator<AddressModel> CREATOR = new Creator<AddressModel>() {
        @Override
        public AddressModel createFromParcel(Parcel in) {
            return new AddressModel(in);
        }

        @Override
        public AddressModel[] newArray(int size) {
            return new AddressModel[size];
        }
    };

    public String getIsSelected() {
        return IsSelected;
    }

    public void setIsSelected(String isSelected) {
        IsSelected = isSelected;
    }

    public int getRoomID() {
        return RoomID;
    }

    public void setRoomID(int roomID) {
        RoomID = roomID;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getFireID() {
        return FireID;
    }

    public void setFireID(String fireID) {
        FireID = fireID;
    }

    public String getAddLine1() {
        return AddLine1;
    }

    public void setAddLine1(String addLine1) {
        AddLine1 = addLine1;
    }

    public String getAddLine2() {
        return AddLine2;
    }

    public void setAddLine2(String addLine2) {
        AddLine2 = addLine2;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        PinCode = pinCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(RoomID);
        dest.writeString(AddLine1);
        dest.writeString(AddLine2);
        dest.writeString(City);
        dest.writeString(PinCode);
        dest.writeString(Uid);
        dest.writeString(FireID);
        dest.writeString(IsSelected);
    }
}
