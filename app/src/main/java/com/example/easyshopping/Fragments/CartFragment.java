package com.example.easyshopping.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyshopping.Adapters.CartAdapter;
import com.example.easyshopping.Data.Room_Helper;
import com.example.easyshopping.Data.Room_Product_Model;
import com.example.easyshopping.Model.CartListModel;
import com.example.easyshopping.Model.CartModel;
import com.example.easyshopping.Model.OrderModel;
import com.example.easyshopping.Model.OrderProductModel;
import com.example.easyshopping.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartFragment extends Fragment implements View.OnClickListener {

    ArrayList<Room_Product_Model> Product_list_home;
    Room_Helper helper;
    RecyclerView Cart_item_rv;
    CartAdapter adapter;

    AppCompatButton CheckOutBtn;

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    String Uid,UserName;
    ArrayList<CartListModel> cartListfire;
    ArrayList<CartModel> cartModels;
    ArrayList<CartModel> CartList;
    TextView cartContinueShopping;



    public CartFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        InitView(view);
        Cart_rv();


        if (CartList.isEmpty())
            FetchDataFromFireBase();
        else
            adapter.notifyDataSetChanged();




        return view;

    }

    private void InitView(View view) {

        Cart_item_rv = view.findViewById(R.id.cart_item_rv);
        CheckOutBtn = view.findViewById(R.id.checkout_btn_cart);
        cartContinueShopping = view.findViewById(R.id.cartContinueShopping);

        CheckOutBtn.setOnClickListener(this);
        cartContinueShopping.setOnClickListener(this);


        Product_list_home = new ArrayList<>();
        cartListfire = new ArrayList<>();
        cartModels = new ArrayList<>();
        CartList = new ArrayList<>();

        helper  = Room_Helper.GetDB(getActivity());

        Uid = helper.room_dao().getalluserdetails().get(0).getUserID();
        UserName = helper.room_dao().getalluserdetails().get(0).getUserName();
        database = FirebaseDatabase.getInstance();




    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.checkout_btn_cart) {
            ArrayList<OrderModel> OrderList = TotalCartValue();
            ArrayList<OrderProductModel> ProductListSize = OrderList.get(0).getProductList();


            if (!ProductListSize.isEmpty()) {

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("OrderList", OrderList);

                AddressFragment fragment = new AddressFragment();
                fragment.setArguments(bundle);

                FragmentManager fm = requireActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                ft.replace(R.id.frag_container, fragment);
                ft.addToBackStack(null);
                ft.commit();
            } else
                Toast.makeText(getActivity(), "Please add Items in CartList.", Toast.LENGTH_SHORT).show();
        } else if (Id == R.id.cartContinueShopping) {
            View view1 = getActivity().findViewById(R.id.bottomNV).findViewById(R.id.store_menu);
            view1.performClick();

            StoreFragment fragmentst = new StoreFragment();

            FragmentManager fms = (getActivity()).getSupportFragmentManager();
            FragmentTransaction fts = fms.beginTransaction();
            fts.replace(R.id.frag_container, fragmentst);
            fts.addToBackStack(null);
            fts.commit();
        }
    }

    private ArrayList<OrderModel> TotalCartValue() {

        ArrayList<OrderModel> orderModel = new ArrayList<>();
        ArrayList<OrderProductModel> prlist = new ArrayList<>();
        Float PrQty = 0.0F;
        Float prValue = 0.0F;
        Float TotalCartPrice = 0.0F;



        for (int i = 0; i <= CartList.size()-1; i++) {
            Float ProductTotalPrice = 0.0F;

            String Price = CartList.get(i).Cart_Price;
            String Qty = CartList.get(i).Cart_Item_Qty;

            try {
                 prValue = Float.parseFloat(Price);
                 PrQty = Float.parseFloat(Qty);

            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }


            ProductTotalPrice = prValue * PrQty;

            TotalCartPrice = TotalCartPrice + ProductTotalPrice;

            String ProductName = CartList.get(i).Cart_Name;
            String ProductPrice = CartList.get(i).Cart_Price;
            String ProductQty = CartList.get(i).Cart_Item_Qty;

            prlist.add(new OrderProductModel(ProductName,ProductPrice,ProductQty));



        }

        orderModel.add(new OrderModel(UserName,Uid,prlist, String.valueOf(TotalCartPrice)));

        return orderModel;
    }

    private void Cart_rv() {
        Cart_item_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        Cart_item_rv.setNestedScrollingEnabled(false);
        adapter = new CartAdapter(getActivity(),CartList);
        Cart_item_rv.setAdapter(adapter);

    }

    private void FetchDataFromFireBase() {


        if (!Uid.isEmpty())
            databaseReference = database.getReference().child("/Cart/"+Uid);
        else{
            Uid = helper.room_dao().getalluserdetails().get(0).getUserID();
            UserName = helper.room_dao().getalluserdetails().get(0).getUserName();
        }


        ArrayList<CartModel> arrayList = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String PrID = dataSnapshot.getKey();
                    CartListModel cartListModel = dataSnapshot.getValue(CartListModel.class);

                    String productName = cartListModel.Name;
                    Room_Product_Model model = helper.room_dao().GetCartProduct(productName);

                   // int Cart_Id = model.getId();
                    String cart_Name = model.getName();
                    String cart_Price = model.getPrice();
                    String cart_Rating = model.getPrice();
                    String cart_cat = model.getCat();
                    String cart_img = model.getImg();
                    String cart_img1 = model.getImg1();
                    String cart_img2 = model.getImg2();
                    String cart_img3 = model.getImg3();
                    String cart_img4 = model.getImg4();
                    String cart_img5 = model.getImg5();
                    String cart_UserID = Uid ;
                    String cart_Fire_PR_ID = PrID;
                    String Cart_Item_Qty = cartListModel.Cart_Item_Qty;


                    CartModel cartModel = new CartModel(cart_Name,cart_Price,cart_Rating,cart_cat,
                            cart_img,cart_img1,cart_img2,cart_img3,cart_img4,
                            cart_img5,cart_UserID,cart_Fire_PR_ID,Cart_Item_Qty);

                    helper.room_dao().AddCart(cartModel);

                    arrayList.add(cartModel);

                }

                CartList.clear();
                CartList.addAll(arrayList);
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getActivity(), "Data fetch Failed", Toast.LENGTH_SHORT).show();
            }
        });










    }




}