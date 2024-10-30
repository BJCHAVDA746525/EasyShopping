package com.example.easyshopping.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.easyshopping.Model.newCollectionModel;
import com.example.easyshopping.R;

import java.util.ArrayList;

public class ProductDetailsAdapter extends RecyclerView.Adapter<ProductDetailsAdapter.viewholder> {

    ArrayList<newCollectionModel> arrayList;
    Context context;

    public ProductDetailsAdapter(ArrayList<newCollectionModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductDetailsAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_viewpage_layout,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductDetailsAdapter.viewholder holder, int position) {

        int listimgsize = arrayList.size();

        if (arrayList.get(position).getImgCatagory() != null
                && !arrayList.get(position).getImgCatagory().isEmpty()
                &&  !arrayList.get(position).getImgCatagory().equals("")){

            Glide.with(context)
                    .load(arrayList.get(position).getImgCatagory())
                    .into(holder.home_img);
        }



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView home_img;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            home_img = itemView.findViewById(R.id.product_viewpager_imgview);
        }
    }
}
