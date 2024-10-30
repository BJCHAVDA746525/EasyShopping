package com.example.easyshopping.WorkManager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.easyshopping.Data.Room_Helper;
import com.example.easyshopping.Data.UserDataModel;
import com.example.easyshopping.Model.FireUserDetailsModel;
import com.example.easyshopping.Utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FireUserSync extends Worker {

    private Room_Helper roomHelper;         // Your Room Database instance

    public FireUserSync(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        roomHelper = Room_Helper.GetDB(getApplicationContext());
    }

    @NonNull
    @Override
    public Result doWork() {
        storeUserDataToRoom();
        setUsersDataToConstants();
        return Result.success();
    }


    private void storeUserDataToRoom() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("/users").child(auth.getUid());


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<UserDataModel> Userlist = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FireUserDetailsModel model = dataSnapshot.getValue(FireUserDetailsModel.class);
                    String email = model.getEmail();
                    String name = model.getName();
                    String phone = model.getPhone();
                    String photo = model.getProfileImageUrl();
                    String userId = model.getUserId();
                    UserDataModel user = new UserDataModel(name, email, phone, photo, userId);
                    Userlist.add(user);

                }

                if (!Userlist.isEmpty()) {
                    roomHelper.room_dao().ClearUserData();
                    roomHelper.room_dao().AddUser(Userlist.get(0));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase Product Error", " ERROR : " + error.toString());
            }
        });


    }

    private boolean setUsersDataToConstants() {

        try {
            ArrayList<UserDataModel> user = new ArrayList<>(roomHelper.room_dao().getalluserdetails());

            if ((!user.isEmpty())) {
                Constants.USER_ID = user.get(0).getUserID();
                Constants.USER_NAME = user.get(0).getUserName();
                Constants.USER_EMAIL = user.get(0).getUseremail();
                Constants.USER_PHONE = user.get(0).getUserphone();
                Constants.USER_PHOTO = user.get(0).getUserphotoURL();

                return true;
            }

        } catch (Exception e) {

            Log.e("SetUserDataToConstant Error", "ERROR: " + e);
            return false;
        }

        return false;
    }
}
