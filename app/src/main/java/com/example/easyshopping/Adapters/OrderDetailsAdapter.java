package com.example.easyshopping.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.easyshopping.Data.Room_Product_Model;
import com.example.easyshopping.R;

import java.util.ArrayList;


public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {

    ArrayList<Room_Product_Model> FinalOrderList;
    Context context;


    public OrderDetailsAdapter(ArrayList<Room_Product_Model> finalOrderList,Context context) {
        this.context = context;
        FinalOrderList = finalOrderList;
    }

    @NonNull
    @Override
    public OrderDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details_recylcler_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsAdapter.ViewHolder holder, int position) {
        int positionId = position;

        holder.Product_name.setText(FinalOrderList.get(position).getName());
        holder.Product_catagory.setText(FinalOrderList.get(position).getCat());
        holder.Product_price.setText(FinalOrderList.get(position).getPrice());
        holder.Product_rating.setText(FinalOrderList.get(position).getRating());

        Glide.with(context).load(FinalOrderList.get(position).getImg()).into(holder.Product_img);


    }

    @Override
    public int getItemCount() {
        return FinalOrderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView Product_img;
        TextView Product_name, Product_catagory, Product_price, Product_rating;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Product_img = itemView.findViewById(R.id.order_rv_imgview);
            Product_name = itemView.findViewById(R.id.order_product_rv_txt);
            Product_catagory = itemView.findViewById(R.id.order_catagory_rv_txt);
            Product_price = itemView.findViewById(R.id.order_Price_rv_txt);
            Product_rating = itemView.findViewById(R.id.order_rating_rv_txt);
        }
    }
}
