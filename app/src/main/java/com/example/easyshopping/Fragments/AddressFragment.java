package com.example.easyshopping.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.easyshopping.Activities.PaymentActivity;
import com.example.easyshopping.Adapters.AddressAdapter;
import com.example.easyshopping.Data.Room_Helper;
import com.example.easyshopping.Data.Room_Product_Model;
import com.example.easyshopping.Model.AddressModel;
import com.example.easyshopping.Model.OrderModel;
import com.example.easyshopping.Model.OrderProductModel;
import com.example.easyshopping.R;
import com.example.easyshopping.Utils.Dialogs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AddressFragment extends Fragment implements View.OnClickListener {
    RelativeLayout AddressTab;
    RelativeLayout newAddress;

    RecyclerView AddressRV;
    AddressAdapter adapter;
    Dialogs dialogs;
    AppCompatButton NextBtn;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Room_Helper helper;
    String Uid;

    ArrayList<AddressModel> AddressBookList;
    ArrayList<OrderModel> OrderList;
    ArrayList<OrderModel> FinalOrderList;




    public AddressFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address, container, false);
        InitView(view);
        AddreshBookLoad();
        FetchFireAddressBook();

        if (getArguments() != null) {
            OrderList = getArguments().getParcelableArrayList("OrderList");
        }

        return view;
    }

    private void AddreshBookLoad() {

        adapter = new AddressAdapter(AddressBookList);
        AddressRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        AddressRV.setAdapter(adapter);
    }



    private void InitView(View view) {
        AddressTab = view.findViewById(R.id.address_tab);
        AddressRV = view.findViewById(R.id.address_rv);
        newAddress = view.findViewById(R.id.add_address_btn);
        NextBtn = view.findViewById(R.id.address_next_btn);

        newAddress.setOnClickListener(this);
        NextBtn.setOnClickListener(this);

        dialogs = new Dialogs();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("/AddressBook");
        helper = Room_Helper.GetDB(getActivity());
        AddressBookList = new ArrayList<>();
        FinalOrderList = new ArrayList<>();


    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();
        if (Id == R.id.add_address_btn) {
            dialogs.NewAddressADD(getActivity());
        } else if (Id == R.id.address_next_btn) {
           LoadPaymentFragment();

        }
    }

    private void LoadPaymentFragment() {

        ArrayList<AddressModel> addressModels = new ArrayList<>();

        for (int i = 0; i < AddressBookList.size(); i++) {

           String selected =  AddressBookList.get(i).getIsSelected();

           if (selected.equals("check")){
               String addLine1 = AddressBookList.get(i).getAddLine1();
               String addLine2 = AddressBookList.get(i).getAddLine2();
               String City = AddressBookList.get(i).getCity();
               String Pincode = AddressBookList.get(i).getPinCode();
               String Uid = AddressBookList.get(i).getUid();
               String FireID = AddressBookList.get(i).getFireID();
               String IsSelected = AddressBookList.get(i).getIsSelected();

               addressModels.add(new AddressModel(addLine1,addLine2,City,Pincode,Uid,FireID,IsSelected));
           }

        }

        if (addressModels.isEmpty()) {
            Toast.makeText(getActivity(), "Please Select Address or Add New Address !!", Toast.LENGTH_SHORT).show();
        }else {
             OrderList.get(0).setAddress(addressModels.get(0));

            Bundle bundle = new Bundle();
            bundle.putSerializable("FinalOrderList", OrderList);

            Intent ipayment = new Intent(getActivity(),PaymentActivity.class);
            ipayment.putExtras(bundle);
            startActivity(ipayment);


            /*RazorPayFragment Pfragment = new RazorPayFragment();
            Pfragment.setArguments(bundle);


            FragmentManager fm = requireActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            ft.replace(R.id.frag_container, Pfragment);
            ft.addToBackStack(null);
            ft.commit();*/


        }












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

               // CompareData(AddBook);

                helper.room_dao().ClearAddressBook();

                AddressBookList.addAll(AddBook);
                // helper.room_dao().AddAddressList(AddBook);
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void CompareData(ArrayList<AddressModel> FireList) {

        AddressBookList.clear();
        ArrayList<AddressModel> RoomList = new ArrayList<>(helper.room_dao().GetAllAddress());

        int FireListSize = FireList.size();
        int RoomListSize = RoomList.size();

        if (FireListSize >= RoomListSize) {

            for (int i = RoomListSize ; i <= FireListSize-1; i++) {

                String AddLine1 = FireList.get(i).getAddLine1();
                String AddLine2 = FireList.get(i).getAddLine2();
                String City = FireList.get(i).getCity();
                String PinCode = FireList.get(i).getPinCode();
                String Uid = FireList.get(i).getUid();
                String FireID = FireList.get(i).getFireID();
                AddressModel model = new AddressModel(AddLine1,AddLine2,City,PinCode, Uid,FireID,"uncheck");

                RoomList.add(new AddressModel(AddLine1,AddLine2,City,PinCode, Uid,FireID,"uncheck"));

                //helper.room_dao().AddAddress(model);

            }


        }




    }


}