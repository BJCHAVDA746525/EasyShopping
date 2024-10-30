package com.example.easyshopping.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "userdata")
public class UserDataModel {

    @PrimaryKey(autoGenerate = true)
    private int RoomId ;

    @ColumnInfo
    private String UserName;

    @ColumnInfo
    private String Useremail;

    @ColumnInfo
    private String Userphone;

    @ColumnInfo
    private String userphotoURL;

    @ColumnInfo
    private String UserID;

    public UserDataModel() {
    }

    @Ignore
    public UserDataModel(String userName, String useremail, String userphone, String userphotoURL, String userID) {
        this.UserName = userName;
        this.Useremail = useremail;
        this.Userphone = userphone;
        this.userphotoURL = userphotoURL;
        this.UserID = userID;
    }

    public int getRoomId() {
        return RoomId;
    }

    public void setRoomId(int roomId) {
        RoomId = roomId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUseremail() {
        return Useremail;
    }

    public void setUseremail(String useremail) {
        Useremail = useremail;
    }

    public String getUserphone() {
        return Userphone;
    }

    public void setUserphone(String userphone) {
        Userphone = userphone;
    }

    public String getUserphotoURL() {
        return userphotoURL;
    }

    public void setUserphotoURL(String userphotoURL) {
        this.userphotoURL = userphotoURL;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
}
