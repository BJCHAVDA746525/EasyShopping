package com.example.easyshopping.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
import com.example.easyshopping.Utils.Constants;

import java.util.ArrayList;

public class NewCollectionAdapter extends RecyclerView.Adapter<NewCollectionAdapter.viewholder> {

    ArrayList<Room_Product_Model> arrayList;
    Context context;
    Room_Helper helper;
    String Uid;

    public NewCollectionAdapter(ArrayList<Room_Product_Model> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;

    }

    @NonNull
    @Override
    public NewCollectionAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_collection_item_layout,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewCollectionAdapter.viewholder holder, int position) {

        int pos = position;

        Glide.with(context).load(arrayList.get(position).getImg()).into(holder.home_img);
        helper = Room_Helper.GetDB(context);

        int id = (arrayList.get(pos).getId());
        String name = (arrayList.get(pos).getName());
        String Price = (arrayList.get(pos).getPrice());
        String Rating = (arrayList.get(pos).getRating());
        String cat = (arrayList.get(pos).getCat());
        String img = (arrayList.get(pos).getImg());
        String img1 = (arrayList.get(pos).getImg1());
        String img2 = (arrayList.get(pos).getImg2());
        String img3 = (arrayList.get(pos).getImg3());
        String img4 = (arrayList.get(pos).getImg4());
        String img5 = (arrayList.get(pos).getImg5());
        Uid = Constants.USER_ID;



        ArrayList<Room_Product_Model> list = new ArrayList<>();
        list.add(new Room_Product_Model(id, name, Price, Rating, cat, img, img1, img2, img3, img4, img5));
        holder.LayoutClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("productList", list);

                ProductDetailsFragment fragment = new ProductDetailsFragment();
                fragment.setArguments(bundle);

                FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                ft.replace(R.id.frag_container, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }




    public class viewholder extends RecyclerView.ViewHolder {
        ImageView home_img;
        RelativeLayout LayoutClick;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            home_img = itemView.findViewById(R.id.viewpager_imgview2);
            LayoutClick = itemView.findViewById(R.id.NewCollectionLayout);
        }
    }


}
