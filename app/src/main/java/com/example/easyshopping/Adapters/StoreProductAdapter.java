package com.example.easyshopping.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.easyshopping.Data.Room_Helper;
import com.example.easyshopping.Data.Room_Product_Model;
import com.example.easyshopping.Fragments.ProductDetailsFragment;
import com.example.easyshopping.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StoreProductAdapter extends RecyclerView.Adapter<StoreProductAdapter.viewholder> {

    ArrayList<Room_Product_Model> Product_list;
    Context context;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Room_Helper helper;
    String Uid;

    public StoreProductAdapter(ArrayList<Room_Product_Model> product_list, Context context) {
        Product_list = product_list;
        this.context = context;
    }

    @NonNull
    @Override
    public StoreProductAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_recycler_layout,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreProductAdapter.viewholder holder, int position) {


        int pos = position;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("/Cart");
        helper = Room_Helper.GetDB(context);

        holder.Product_name.setText(Product_list.get(position).getName());
        holder.Product_catagory.setText(Product_list.get(position).getCat());
        holder.Product_price.setText(Product_list.get(position).getPrice());
        holder.Product_rating.setText(Product_list.get(position).getRating());

        Glide.with(context).load(Product_list.get(position).getImg()).into(holder.Product_img);

        int id = (Product_list.get(pos).getId());
        String name = (Product_list.get(pos).getName());
        String Price = (Product_list.get(pos).getPrice());
        String Rating = (Product_list.get(pos).getRating());
        String cat = (Product_list.get(pos).getCat());
        String img = (Product_list.get(pos).getImg());
        String img1 = (Product_list.get(pos).getImg1());
        String img2 = (Product_list.get(pos).getImg2());
        String img3 = (Product_list.get(pos).getImg3());
        String img4 = (Product_list.get(pos).getImg4());
        String img5 = (Product_list.get(pos).getImg5());
        Uid = helper.room_dao().getalluserdetails().get(0).getUserID();

        ArrayList<Room_Product_Model> list = new ArrayList<>();
        list.add(new Room_Product_Model(id,name,Price,Rating,cat,img,img1,img2,img3,img4,img5));

        holder.ProductLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("productList", list);

                ProductDetailsFragment fragment = new ProductDetailsFragment();
                fragment.setArguments(bundle);

                FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                ft.replace(R.id.frag_container,fragment);
                ft.addToBackStack(null);
                ft.commit();

            }
        });

        holder.Cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addDatatoFirebase(list);
            }
        });

    }

    private void addDatatoFirebase(ArrayList<Room_Product_Model> listcart) {

        String prname = listcart.get(0).getName();
        String Qty = "1";
        String Total = listcart.get(0).getPrice();

        Map<String, String> CartItem = new HashMap<>();
        CartItem.put("UserID", Uid);
        CartItem.put("Name", prname);
        CartItem.put("Cart_Item_Qty", Qty);
        CartItem.put("Price", Total);
        CartItem.put("Fire_CartID", "");

        databaseReference.child(Uid).push().setValue(CartItem);
        Toast.makeText(context, "Item Added", Toast.LENGTH_SHORT).show();

    }

    @Override
    public int getItemCount() {
        return Product_list.size();
    }

    public void setProductList(ArrayList<Room_Product_Model> productList) {
        this.Product_list = productList;
        notifyDataSetChanged();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView Product_img,Cart_btn;
        TextView Product_name,Product_catagory,Product_price,Product_rating;

        RelativeLayout ProductLayout;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            Product_img = itemView.findViewById(R.id.home_rv_imgview);
            Product_name = itemView.findViewById(R.id.product_rv_txt);
            Product_catagory = itemView.findViewById(R.id.catagory_rv_txt);
            Product_price = itemView.findViewById(R.id.Price_rv_txt);
            Product_rating = itemView.findViewById(R.id.rating_rv_txt);
            ProductLayout = itemView.findViewById(R.id.product_rv_layout);
            Cart_btn = itemView.findViewById(R.id.shopping_bag_home);
        }
    }
}
