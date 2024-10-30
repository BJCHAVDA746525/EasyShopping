package com.example.easyshopping.Adapters;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyshopping.Fragments.OrderDetailsFragment;
import com.example.easyshopping.Fragments.ProductDetailsFragment;
import com.example.easyshopping.Model.OrderModel;
import com.example.easyshopping.R;
import com.example.easyshopping.orderModels.OrderModelFire;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    ArrayList<OrderModelFire> OrdersList;
    Context context;


    public OrdersAdapter(ArrayList<OrderModelFire> Orderlist, Context contexts) {
        OrdersList = Orderlist;
        context = contexts;
    }

    @NonNull
    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_recycler_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.ViewHolder holder, int position) {

        int positionId = position;

        if (OrdersList != null || !OrdersList.isEmpty()){

            try {
                String YearTxt = DateConverter(OrdersList.get(positionId).getOrderTime(),"Year");
                String MonthTxt = DateConverter(OrdersList.get(positionId).getOrderTime(),"Month");
                String DateTxt = DateConverter(OrdersList.get(positionId).getOrderTime(),"Day");
                String OrderNoTxt = OrdersList.get(positionId).getOrderTime().substring(6,10);
                String NameStr = OrdersList.get(positionId).getUserName();
                String TotalProductsDetailsStr = String.valueOf(OrdersList.get(positionId).getProductList().size());
                String OrderCostStr = String.valueOf(OrdersList.get(positionId).getTotalCartPrice());
                String StatusStr = OrdersList.get(positionId).getPaymentDone();

                holder.YearTxt.setText(YearTxt);
                holder.DateTxt.setText(DateTxt);
                holder.MonthTxt.setText(MonthTxt);
                holder.OrderNoTxt.setText(OrderNoTxt);
                holder.NameTxt.setText(NameStr);
                holder.TotalProductsDetailsTxt.setText(TotalProductsDetailsStr);
                holder.OrderCostTxt.setText(OrderCostStr);
                holder.StatusTxt.setText(StatusStr);

            } catch (Exception e) {
                Log.d("OrderAdapter Error", "Error: " + e.getMessage());
            }

            holder.OrderLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    GotoOrderDetails(OrdersList.get(positionId));
                }
            });

        }

       












    }

    private void GotoOrderDetails(OrderModelFire orderModel) {
        Bundle bundle = new Bundle();
        ArrayList<OrderModelFire> orderModels = new ArrayList<>();
        orderModels.add(orderModel);
        bundle.putSerializable("orderdetails", orderModels);

        OrderDetailsFragment fragment = new OrderDetailsFragment();
        fragment.setArguments(bundle);

        FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.frag_container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }


    @Override
    public int getItemCount() {
        return OrdersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView YearTxt,DateTxt,MonthTxt,OrderNoTxt,NameTxt,TotalProductsDetailsTxt,OrderCostTxt,StatusTxt;
        RelativeLayout OrderLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            YearTxt = itemView.findViewById(R.id.year_order_txt);
            DateTxt = itemView.findViewById(R.id.date_order_txt);
            MonthTxt = itemView.findViewById(R.id.month_order_txt);
            OrderNoTxt = itemView.findViewById(R.id.orderno_order_txt);
            NameTxt = itemView.findViewById(R.id.name_order_txt);
            TotalProductsDetailsTxt = itemView.findViewById(R.id.total_products_details);
            OrderCostTxt = itemView.findViewById(R.id.total_cost_txt);
            StatusTxt = itemView.findViewById(R.id.Status_txt);
            OrderLayout = itemView.findViewById(R.id.order_layout);


        }
    }

    private String DateConverter(String Stamp, String Type) {
        if (Stamp != null && !Stamp.isEmpty()) {
            // Parse the timestamp string into a long
            long timestamp = Long.parseLong(Stamp);

            String formattedDate;

            // For Year
            if (Type.equals("Year")) {
                // API 26 and above (using java.time)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    Instant instant = Instant.ofEpochMilli(timestamp);
                    ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
                    formattedDate = zonedDateTime.format(formatter);
                } else {
                    // For Android versions below API 26 (using java.util.Date and SimpleDateFormat)
                    Date date = new Date(timestamp);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.getDefault());
                    formattedDate = sdf.format(date);
                }
                return formattedDate;

                // For Month
            } else if (Type.equals("Month")) {
                // API 26 and above (using java.time)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    Instant instant = Instant.ofEpochMilli(timestamp);
                    ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM");
                    formattedDate = zonedDateTime.format(formatter);
                } else {
                    // For Android versions below API 26 (using java.util.Date and SimpleDateFormat)
                    Date date = new Date(timestamp);
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM", Locale.getDefault());
                    formattedDate = sdf.format(date);
                }
                return formattedDate;

                // For Day
            } else if (Type.equals("Day")) {
                // API 26 and above (using java.time)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    Instant instant = Instant.ofEpochMilli(timestamp);
                    ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
                    formattedDate = zonedDateTime.format(formatter);
                } else {
                    // For Android versions below API 26 (using java.util.Date and SimpleDateFormat)
                    Date date = new Date(timestamp);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.getDefault());
                    formattedDate = sdf.format(date);
                }
                return formattedDate;
            }
        }

        return null;
    }
}
