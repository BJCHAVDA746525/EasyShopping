package com.example.easyshopping.Model;

import java.util.ArrayList;

public class ProductModel {

    ArrayList<homeRVModel> product_cat_list;

    public ProductModel() {

    }

    public ProductModel(ArrayList<homeRVModel> product_cat_list) {
        this.product_cat_list = product_cat_list;
    }

    public ArrayList<homeRVModel> getProduct_cat_list() {
        return product_cat_list;
    }

    public void setProduct_cat_list(ArrayList<homeRVModel> product_cat_list) {
        this.product_cat_list = product_cat_list;
    }
}
