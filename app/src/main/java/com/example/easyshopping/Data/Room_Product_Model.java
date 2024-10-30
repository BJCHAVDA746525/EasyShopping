package com.example.easyshopping.Data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "product_table")
public class Room_Product_Model implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int Id ;

    @ColumnInfo
    private String Name;

    @ColumnInfo
    private String Price;

    @ColumnInfo
    private String Rating;

    @ColumnInfo
    private String cat;

    @ColumnInfo
    private String img;

    @ColumnInfo
    private String img1;

    @ColumnInfo
    private String img2;

    @ColumnInfo
    private String img3;

    @ColumnInfo
    private String img4;

    @ColumnInfo
    private String img5;



    public Room_Product_Model(){

    }

    @Ignore
    public Room_Product_Model(int id, String name, String price, String rating, String cat, String img, String img1, String img2, String img3, String img4, String img5) {
        this.Id = id;
        this.Name = name;
        this.Price = price;
        this.Rating = rating;
        this.cat = cat;
        this.img = img;
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
        this.img4 = img4;
        this.img5 = img5;
    }

    protected Room_Product_Model(Parcel in) {
        Id = in.readInt();
        Name = in.readString();
        Price = in.readString();
        Rating = in.readString();
        cat = in.readString();
        img = in.readString();
        img1 = in.readString();
        img2 = in.readString();
        img3 = in.readString();
        img4 = in.readString();
        img5 = in.readString();
    }

    public static final Creator<Room_Product_Model> CREATOR = new Creator<Room_Product_Model>() {
        @Override
        public Room_Product_Model createFromParcel(Parcel in) {
            return new Room_Product_Model(in);
        }

        @Override
        public Room_Product_Model[] newArray(int size) {
            return new Room_Product_Model[size];
        }
    };

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getImg4() {
        return img4;
    }

    public void setImg4(String img4) {
        this.img4 = img4;
    }

    public String getImg5() {
        return img5;
    }

    public void setImg5(String img5) {
        this.img5 = img5;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Name);
        dest.writeString(Price);
        dest.writeString(Rating);
        dest.writeString(cat);
        dest.writeString(img);
        dest.writeString(img1);
        dest.writeString(img2);
        dest.writeString(img3);
        dest.writeString(img4);
        dest.writeString(img5);
    }
}
