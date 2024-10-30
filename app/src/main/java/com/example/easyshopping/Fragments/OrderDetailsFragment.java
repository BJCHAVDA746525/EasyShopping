package com.example.easyshopping.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyshopping.Adapters.HomeProductAdapter;
import com.example.easyshopping.Adapters.OrderDetailsAdapter;
import com.example.easyshopping.Data.Room_Helper;
import com.example.easyshopping.Data.Room_Product_Model;
import com.example.easyshopping.Model.OrderModel;
import com.example.easyshopping.R;
import com.example.easyshopping.orderModels.AddressModelFire;
import com.example.easyshopping.orderModels.OrderModelFire;

import java.util.ArrayList;


public class OrderDetailsFragment extends Fragment {

    RecyclerView orderItems;
    TextView order_add_line_1;
    TextView order_add_line_2;
    TextView order_add_pincode;
    TextView order_add_city;
    TextView order_total;

    ArrayList<OrderModelFire> orderModel;
    OrderDetailsAdapter adapter;
    ArrayList<Room_Product_Model> list;
    Room_Helper helper;

    public OrderDetailsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);
        Initview(view);

        if (getArguments() != null) {

            orderModel= (ArrayList<OrderModelFire>) getArguments().getSerializable("orderdetails");
        }

        if (orderModel != null) {
            ProductDetailsLoad(orderModel);
            OrderDetailsLoad(orderModel);
        }


        return view;
    }

    private void OrderDetailsLoad(ArrayList<OrderModelFire> orderModel) {
        if (orderModel.get(0).getAddress() != null || orderModel.get(0).getTotalCartPrice() != null){
            AddressModelFire addressModelFire = orderModel.get(0).getAddress();
            order_add_line_1.setText(addressModelFire.getAddLine1());
            order_add_line_2.setText(addressModelFire.getAddLine2());
            order_add_pincode.setText(addressModelFire.getPinCode());
            order_add_city.setText(addressModelFire.getCity());

            order_total.setText(orderModel.get(0).getTotalCartPrice());
        }

    }

    private void ProductDetailsLoad(ArrayList<OrderModelFire> orderModel) {
        orderItems.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        adapter = new OrderDetailsAdapter(list, getActivity());
        orderItems.setAdapter(adapter);
        FindProducts(orderModel);




    }

    private void FindProducts(ArrayList<OrderModelFire> orderModelFires) {
        int productsSize = orderModelFires.get(0).getProductList().size();

        for (int i = 0; i < productsSize; i++) {

            Room_Product_Model model = helper.room_dao().GetOrderDetailsProducts(orderModelFires.get(0).getProductList().get(i).getProductName());
            list.add(model);
        }

        adapter.notifyDataSetChanged();


    }


    private void Initview(View view) {

        orderItems = view.findViewById(R.id.order_rv_details_recycler);

        order_add_line_1 = view.findViewById(R.id.order_add_line_1);
        order_add_line_2 = view.findViewById(R.id.order_add_line_2);
        order_add_pincode = view.findViewById(R.id.order_add_pincode);
        order_add_city = view.findViewById(R.id.order_add_city);
        order_total = view.findViewById(R.id.order_total);

        list = new ArrayList<>();
        orderModel = new ArrayList<>();

        helper = Room_Helper.GetDB(getActivity());


    }


}