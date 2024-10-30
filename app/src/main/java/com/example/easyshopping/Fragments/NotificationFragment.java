package com.example.easyshopping.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.easyshopping.Adapters.NotificationAdapter;
import com.example.easyshopping.Data.Room_Helper;
import com.example.easyshopping.Model.NotificationModel;
import com.example.easyshopping.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class NotificationFragment extends Fragment {
    RecyclerView NotificationRecyclerview;
    NotificationAdapter Adapter;
    ArrayList<NotificationModel> NotificationList;
    Room_Helper database;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    public NotificationFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_notification, container, false);
        Initview(view);
        FetchNotification();


        return view;
    }

    private void Initview(View view) {
        NotificationRecyclerview = view.findViewById(R.id.notification_recycler);
        NotificationList = new ArrayList<>();
        database = Room_Helper.GetDB(getActivity());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("/Notification");

        Adapter = new NotificationAdapter(NotificationList);
        NotificationRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        NotificationRecyclerview.setAdapter(Adapter);

    }

    private void FetchNotification() {

        String Uid = database.room_dao().getalluserdetails().get(0).getUserID();

        if (!Uid.isEmpty()){

            databaseReference.child(Uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    NotificationList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        NotificationModel Model = dataSnapshot.getValue(NotificationModel.class);
                        NotificationList.add(Model);
                    }

                    Adapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }



    }
}