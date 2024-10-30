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

import com.example.easyshopping.Adapters.AddressEditAdapter;
import com.example.easyshopping.Data.Room_Helper;
import com.example.easyshopping.Model.AddressModel;
import com.example.easyshopping.R;
import com.example.easyshopping.Utils.Dialogs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AddressEditFragment extends Fragment implements View.OnClickListener {

    RecyclerView FireAddressRv;
    FloatingActionButton addAddress;
    AddressEditAdapter adapter;
    ArrayList<AddressModel> AddressBookList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Dialogs dialogs;

    Room_Helper helper;
    String Uid;


    public AddressEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_address_edit, container, false);
        Initview(view);
        AddressLoad();
        FetchFireAddressBook();
        adapter.notifyDataSetChanged();



        return view;
    }

    private void AddressLoad() {
        adapter = new AddressEditAdapter(AddressBookList,getActivity());
        FireAddressRv.setLayoutManager(new LinearLayoutManager(getContext()));
        FireAddressRv.setAdapter(adapter);
    }

    private void Initview(View view) {
        FireAddressRv  = view.findViewById(R.id.address_edit_recyclerview);
        addAddress  = view.findViewById(R.id.AddNewAddress);
        AddressBookList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("/AddressBook");
        helper = Room_Helper.GetDB(getActivity());
        dialogs = new Dialogs();


        /* OnClicks */

        addAddress.setOnClickListener(this);

    }

    private void FetchFireAddressBook() {

        Uid = helper.room_dao().getalluserdetails().get(0).getUserID();

        if (!Uid.isEmpty())
            databaseReference = firebaseDatabase.getReference().child("/AddressBook/" + Uid);
        else
            Uid = helper.room_dao().getalluserdetails().get(0).getUserID();


        ArrayList<AddressModel> AddBook = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                AddBook.clear();
                AddressBookList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String FireID = dataSnapshot.getKey();
                    AddressModel Model = dataSnapshot.getValue(AddressModel.class);

                    Model.setFireID(FireID);
                    Model.setIsSelected("uncheck");

                    AddBook.add(Model);

                }

                AddressBookList.addAll(AddBook);
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });




    }

    @Override
    public void onClick(View view) {

        int ID = view.getId();

        if (ID == R.id.AddNewAddress) {
            dialogs.NewAddressADD(getActivity());
        }

    }
}