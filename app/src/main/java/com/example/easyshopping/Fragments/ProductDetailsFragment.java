package com.example.easyshopping.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyshopping.Adapters.ProductDetailsAdapter;
import com.example.easyshopping.Data.Room_Helper;
import com.example.easyshopping.Data.Room_Product_Model;
import com.example.easyshopping.Model.OrderModel;
import com.example.easyshopping.Model.OrderProductModel;
import com.example.easyshopping.Model.newCollectionModel;
import com.example.easyshopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;


public class ProductDetailsFragment extends Fragment implements View.OnClickListener   {

    ViewPager2 ProductImg_Viewpager;
    ProductDetailsAdapter viewpager_adapter;
    ArrayList<newCollectionModel> arrayList;
    ScrollingPagerIndicator scrollingPagerIndicator;
    ArrayList<Room_Product_Model> list;

    TextView ProductName,ProductPrice,ProductCat,ProductDescription;

    ImageView backbtn;
    AppCompatButton add_to_cart,buy_now_btn;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Room_Helper helper;
    String Uid,UserName;
    ImageView PlusQty,MinusQty;
    TextView QtyTxt;

    String FinalValueQty;



    public ProductDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);

       getActivity().findViewById(R.id.bottomNV).setVisibility(View.GONE);

        InitView(view);




        list= (ArrayList<Room_Product_Model>)getArguments().getSerializable("productList");
        ProductDetailsLoad(list);
        ProductImages();


        return view;
    }

    private void ProductDetailsLoad(ArrayList<Room_Product_Model> list) {
        ProductName.setText(list.get(0).getName());
        ProductPrice.setText(list.get(0).getPrice());
        ProductCat.setText(list.get(0).getCat());
    }

    private void ProductImages() {

        int imgcount = list.size();


         arrayList.add(new newCollectionModel(list.get(0).getImg()));
         arrayList.add(new newCollectionModel(list.get(0).getImg1()));
         arrayList.add(new newCollectionModel(list.get(0).getImg2()));
         arrayList.add(new newCollectionModel(list.get(0).getImg3()));
         arrayList.add(new newCollectionModel(list.get(0).getImg4()));
         arrayList.add(new newCollectionModel(list.get(0).getImg5()));

        viewpager_adapter = new ProductDetailsAdapter(arrayList,getActivity());
        ProductImg_Viewpager.setAdapter(viewpager_adapter);
        scrollingPagerIndicator.attachToPager(ProductImg_Viewpager);



    }

    private void InitView(View view) {
        ProductImg_Viewpager = view.findViewById(R.id.Product_viewpager_img);
        scrollingPagerIndicator = view.findViewById(R.id.pager_indicator);
        ProductName = view.findViewById(R.id.product_name_prdetails);
        ProductPrice = view.findViewById(R.id.product_price_prdetails);
        ProductCat = view.findViewById(R.id.product_cat_prdetails);
        ProductDescription = view.findViewById(R.id.product_description);
        backbtn = view.findViewById(R.id.Pr_dt_imgview_frag);
        add_to_cart = view.findViewById(R.id.add_to_cart);
        buy_now_btn = view.findViewById(R.id.buy_now_btn);

        PlusQty = view.findViewById(R.id.qty_minus);
        MinusQty = view.findViewById(R.id.qty_plus);
        QtyTxt = view.findViewById(R.id.qty_txt);

        arrayList = new ArrayList<>();
        list = new ArrayList<>();

        helper = Room_Helper.GetDB(getActivity());

        backbtn.setOnClickListener(this);
        add_to_cart.setOnClickListener(this);
        buy_now_btn.setOnClickListener(this);

        PlusQty.setOnClickListener(this);
        MinusQty.setOnClickListener(this);


        FinalValueQty = "1";
        QtyTxt.setText(FinalValueQty);

        Uid = helper.room_dao().getalluserdetails().get(0).getUserID();
        UserName = helper.room_dao().getalluserdetails().get(0).getUserName();



    }


    @Override
    public void onClick(View view) {
        int ID = view.getId();

        if (ID == R.id.Pr_dt_imgview_frag) {
            Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();
        } else if (ID == R.id.add_to_cart) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("/Cart");


            addDatatoFirebase(list, FinalValueQty);
        } else if (ID == R.id.qty_minus) {
            int PlusQty = Integer.parseInt(FinalValueQty);
            int PlusFinalQty = PlusQty + 1;
            FinalValueQty = String.valueOf(PlusFinalQty);

            QtyTxt.setText(FinalValueQty);
        } else if (ID == R.id.qty_plus) {
            int minusQty = Integer.parseInt(FinalValueQty);
            int minusFinalQty = minusQty - 1;

            if (minusFinalQty <= 0) {
                minusFinalQty = 1;
                Toast.makeText(getActivity(), "Minimum Qty is 1", Toast.LENGTH_SHORT).show();
            }
            FinalValueQty = String.valueOf(minusFinalQty);

            QtyTxt.setText(FinalValueQty);
        } else if (ID == R.id.buy_now_btn) {
            ArrayList<OrderModel> OrderList = TotalCartValue();
            ArrayList<OrderProductModel> ProductListSize = OrderList.get(0).getProductList();


            if (!ProductListSize.isEmpty()) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("OrderList", OrderList);

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
        }





    }

    private ArrayList<OrderModel> TotalCartValue() {

        ArrayList<OrderModel> orderModel = new ArrayList<>();
        ArrayList<OrderProductModel> prlist = new ArrayList<>();
        Float PrQty = 0.0F;
        Float prValue = 0.0F;
        Float TotalCartPrice = 0.0F;



        for (int i = 0; i <= list.size()-1; i++) {
            Float ProductTotalPrice = 0.0F;

            String Price = list.get(i).getPrice();
            String Qty = FinalValueQty;

            try {
                prValue = Float.parseFloat(Price);
                PrQty = Float.parseFloat(Qty);

            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }


            ProductTotalPrice = prValue * PrQty;

            TotalCartPrice = TotalCartPrice + ProductTotalPrice;

            String ProductName = list.get(i).getName();
            String ProductPrice = list.get(i).getPrice();
            String ProductQty = FinalValueQty;

            prlist.add(new OrderProductModel(ProductName,ProductPrice,ProductQty));



        }

        orderModel.add(new OrderModel(UserName,Uid,prlist, String.valueOf(TotalCartPrice)));

        return orderModel;
    }


    private void addDatatoFirebase(ArrayList<Room_Product_Model> listcart, String finalValueQty) {

        String prname = listcart.get(0).getName();
        String Qty = finalValueQty;
        String Total = listcart.get(0).getPrice();

        Map<String, String> CartItem = new HashMap<>();
        CartItem.put("UserID", Uid);
        CartItem.put("Name", prname);
        CartItem.put("Cart_Item_Qty", Qty);
        CartItem.put("Price", Total);
        CartItem.put("Fire_CartID", "");

        databaseReference.child(Uid).push().setValue(CartItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getActivity(), "Item Added", Toast.LENGTH_SHORT).show();

                CartFragment fragment = new CartFragment();

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                ft.replace(R.id.frag_container, fragment);
                ft.addToBackStack(null);
                ft.commit();

            }
        });



    }










}