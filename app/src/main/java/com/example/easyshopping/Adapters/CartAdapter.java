package com.example.easyshopping.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.easyshopping.Model.CartModel;
import com.example.easyshopping.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewholder> {

    Context context;
    ArrayList<CartModel> arrayList;
    FirebaseDatabase database;
    DatabaseReference reference;



    public CartAdapter(Context context, ArrayList<CartModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;



    }

    @NonNull
    @Override
    public CartAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_layout,parent,false);
        return new CartAdapter.viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.viewholder holder, int position) {

        String name = arrayList.get(position).Cart_Name;
        int posi = position;
        String PrID = arrayList.get(position).Cart_Fire_PR_ID;
        String Uid = arrayList.get(position).Cart_UserID;

        holder.Product_name.setText(arrayList.get(position).Cart_Name);
        holder.Product_catagory.setText(arrayList.get(position).Cart_cat);
        holder.Product_price.setText(arrayList.get(position).Cart_Price);
        holder.Product_rating.setText(arrayList.get(position).Cart_Rating);
        holder.qty_txt.setText(arrayList.get(position).Cart_Item_Qty);

        Glide.with(context).load(arrayList.get(position).Cart_img).into(holder.Product_img);

        holder.cancel_cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase.getInstance().getReference("Cart/").child(Uid).child(PrID).removeValue();
                arrayList.remove(posi);
                notifyItemRemoved(posi);

               // Refreshfragment();


            }
        });

        holder.qty_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String QtyFire = arrayList.get(posi).Cart_Item_Qty;
                int PlusQty = Integer.parseInt(QtyFire);
                int PlusFinalQty = PlusQty + 1 ;
                String FinalValueQty = String.valueOf(PlusFinalQty);

                FirebaseDatabase.getInstance().getReference("Cart/").child(Uid).child(PrID).child("Cart_Item_Qty").setValue(FinalValueQty);

                holder.qty_txt.setText(FinalValueQty);


               // Refreshfragment();
            }
        });

        holder.qty_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String QtyFire = arrayList.get(posi).Cart_Item_Qty;
                int PlusQty = Integer.parseInt(QtyFire);
                int PlusFinalQty = PlusQty - 1 ;

                if (PlusFinalQty <= 0){
                     PlusFinalQty = 1 ;
                    Toast.makeText(context, "Minimum Qty is 1", Toast.LENGTH_SHORT).show();
                }
                String FinalValueQty = String.valueOf(PlusFinalQty);

                FirebaseDatabase.getInstance().getReference("Cart/").child(Uid).child(PrID).child("Cart_Item_Qty").setValue(FinalValueQty);

                holder.qty_txt.setText(FinalValueQty);



            }
        });

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView Product_img,cancel_cart_btn,qty_plus,qty_minus;
        TextView Product_name,Product_catagory,Product_price,Product_rating,qty_txt;



        public viewholder(@NonNull View itemView) {
            super(itemView);
            Product_img = itemView.findViewById(R.id.cart_rv_imgview);
            Product_name = itemView.findViewById(R.id.cart_product_rv_txt);
            Product_catagory = itemView.findViewById(R.id.cart_catagory_rv_txt);
            Product_price = itemView.findViewById(R.id.cart_Price_rv_txt);
            Product_rating = itemView.findViewById(R.id.cart_rating_rv_txt);
            cancel_cart_btn = itemView.findViewById(R.id.cancel_cart_btn);

            qty_plus = itemView.findViewById(R.id.qty_plus);
            qty_minus = itemView.findViewById(R.id.qty_minus);
            qty_txt = itemView.findViewById(R.id.qty_txt);
        }
    }
}
