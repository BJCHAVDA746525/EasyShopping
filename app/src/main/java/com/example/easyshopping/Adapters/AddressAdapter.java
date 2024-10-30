package com.example.easyshopping.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyshopping.Model.AddressModel;
import com.example.easyshopping.R;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    ArrayList<AddressModel> arrayList;

    public AddressAdapter(ArrayList<AddressModel> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_rv_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder holder, int position) {

        int pos = position;

        holder.AddLine1.setText(arrayList.get(position).getAddLine1());
        holder.AddLine2.setText(arrayList.get(position).getAddLine2());
        holder.City.setText(arrayList.get(position).getCity());
        holder.PinCode.setText(arrayList.get(position).getPinCode());





        if (arrayList.get(position).getIsSelected().equals("check")){
            holder.selectionbtn.setImageResource(R.drawable.check_select);
        }else {
            holder.selectionbtn.setImageResource(R.drawable.check_unselect);
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < arrayList.size(); i++) {

                    arrayList.get(i).setIsSelected("uncheck");

                }



                holder.selectionbtn.setImageResource(R.drawable.check_select);
                arrayList.get(pos).setIsSelected("check");
                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView AddLine1,AddLine2,City,PinCode;
        ImageView selectionbtn;
        RelativeLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            AddLine1 = itemView.findViewById(R.id.add_rv_add_line1);
            AddLine2 = itemView.findViewById(R.id.add_rv_add_line2);
            City = itemView.findViewById(R.id.add_rv_city);
            PinCode = itemView.findViewById(R.id.add_rv_pincode);
            selectionbtn = itemView.findViewById(R.id.add_select_rv);
            layout = itemView.findViewById(R.id.Address_layout);

        }
    }
}
