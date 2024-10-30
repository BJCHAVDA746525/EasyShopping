package com.example.easyshopping.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easyshopping.Adapters.OrdersAdapter;
import com.example.easyshopping.Model.OrderModel;
import com.example.easyshopping.R;
import com.example.easyshopping.Utils.Constants;
import com.example.easyshopping.orderModels.OrderModelFire;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class OrderHistoryFragment extends Fragment {
    RecyclerView OrdersRecyclerview;
    ArrayList<OrderModelFire> OrdersList;
    OrdersAdapter adapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    public OrderHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_order_history_frament, container, false);
        InitView(view);
        orderRecycler();
        FetchOrders();

        return view;
    }



    private void InitView(View view) {
        OrdersRecyclerview = view.findViewById(R.id.Order_recycler);
        OrdersList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("/OrderList");
    }

    private void orderRecycler() {
        adapter = new OrdersAdapter(OrdersList,getActivity());
        OrdersRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        OrdersRecyclerview.setAdapter(adapter);
    }

    private void FetchOrders() {

        databaseReference.child(Constants.USER_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<OrderModelFire> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                    OrderModelFire model = dataSnapshot.getValue(OrderModelFire.class);
                    if (model != null) {
                        list.add(model);
                    }

                }
                OrdersList.addAll(list);  // Add all the fetched orders to the list
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase Order Error", "ERROR: " + error.toString());
            }
        });

    }
}