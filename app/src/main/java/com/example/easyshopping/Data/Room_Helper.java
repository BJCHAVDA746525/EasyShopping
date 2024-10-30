package com.example.easyshopping.Data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.easyshopping.Model.AddressModel;
import com.example.easyshopping.Model.CartModel;

@Database(entities = {Room_Product_Model.class,UserDataModel.class,CartModel.class, AddressModel.class},exportSchema = false,version = 6)
public abstract class Room_Helper extends RoomDatabase {
    private static final String DB_NAME = "easyshopDB";
    private static Room_Helper instance;

    public static synchronized Room_Helper GetDB (Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context,Room_Helper.class,DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;

    }

    public abstract Room_DAO room_dao();
}
