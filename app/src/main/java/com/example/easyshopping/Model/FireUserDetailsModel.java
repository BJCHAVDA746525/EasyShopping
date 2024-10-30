package com.example.easyshopping.Model;

public class FireUserDetailsModel {
    String email,name,phone,profileImageUrl,userId;

    public FireUserDetailsModel() {
    }

    public FireUserDetailsModel(String email, String name, String phone, String userId, String profileImageUrl) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.userId = userId;
        this.profileImageUrl = profileImageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
