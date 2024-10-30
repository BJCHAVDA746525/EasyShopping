package com.example.easyshopping.WorkManager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.easyshopping.Data.Room_Helper;
import com.example.easyshopping.Data.Room_Product_Model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseSyncWorker extends Worker {

    private Room_Helper roomHelper;         // Your Room Database instance

    public FirebaseSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        roomHelper = Room_Helper.GetDB(getApplicationContext());

    }


    @NonNull
    @Override
    public Result doWork() {
        fetchDataFromFirebase();
        return Result.success();
    }

    private void fetchDataFromFirebase() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("/Products");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Room_Product_Model> list = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Room_Product_Model model = dataSnapshot.getValue(Room_Product_Model.class);
                    list.add(model);
                }



                insertDataRoom(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.d("Firebase Product Error"," ERROR : " + error.toString());

            }
        });

    }



    private void insertDataRoom(ArrayList<Room_Product_Model> list) {

        ArrayList<Room_Product_Model> roomList = new ArrayList<>();
        roomList = (ArrayList<Room_Product_Model>) roomHelper.room_dao().GetAllData();
        if (roomList.size() < list.size()){
            roomHelper.room_dao().ClearProducts();
            new Thread(()-> {
                roomHelper.room_dao().AddProducts(list);
            }).start();
        }



    }
}
