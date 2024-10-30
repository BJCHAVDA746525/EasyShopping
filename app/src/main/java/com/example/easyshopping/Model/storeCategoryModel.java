package com.example.easyshopping.Model;

public class storeCategoryModel {
    int home_swipeimg ;
    String Cat_name;

    public storeCategoryModel(int home_swipeimg, String Cat_name) {
        this.home_swipeimg = home_swipeimg;
        this.Cat_name = Cat_name;
    }

    public String getCat_name() {
        return Cat_name;
    }

    public void setCat_name(String cat_name) {
        Cat_name = cat_name;
    }

    public int getHome_swipeimg() {
        return home_swipeimg;
    }

    public void setHome_swipeimg(int home_swipeimg) {
        this.home_swipeimg = home_swipeimg;
    }
}
