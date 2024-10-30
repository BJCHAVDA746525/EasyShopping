package com.example.easyshopping.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.easyshopping.Fragments.CartFragment;
import com.example.easyshopping.Model.OrderModel;
import com.example.easyshopping.R;
import com.example.easyshopping.Utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener, View.OnClickListener {

    //Views
    private AppCompatButton payBtn;
    private TextView TotalPrice, DeliveryCharge, FinalPrice, waitOrderTxt;
    private ProgressBar progressBar;

    //Variables
    private static final String RazorPayKey = "rzp_test_LpOBCFlSDHrhcU";
    String finalStr, samount;
    boolean IsListOk = false;

    //Lists
    ArrayList<OrderModel> FinalOrderList;

    //Objects
    FirebaseDatabase database;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        InitView();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            FinalOrderList = (ArrayList<OrderModel>) bundle.getSerializable("FinalOrderList");

            if (FinalOrderList != null && !FinalOrderList.isEmpty()) {
                SetFinalPrice(FinalOrderList);
                IsListOk = true;

            } else {
                // Handle the case where the finalOrderList is null or empty
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                GotoCart();
            }
        } else {
            // Handle the case where the bundle is null
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            GotoCart();
        }


    }

    private void InitView() {

        TotalPrice = findViewById(R.id.price_totalprice_txt);
        DeliveryCharge = findViewById(R.id.delivery_totalprice_txt);
        FinalPrice = findViewById(R.id.cart_totalprice_txt);
        payBtn = findViewById(R.id.place_order_btn);
        waitOrderTxt = findViewById(R.id.wait_for_order);
        progressBar = findViewById(R.id.payment_progress);

        payBtn.setOnClickListener(this);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("/OrderList");
    }

    private void PaymentFun(String samount) {

        // rounding off the amount.
        int amount = Math.round(Float.parseFloat(samount) * 100);

        // initialize Razorpay account.
        Checkout checkout = new Checkout();

        // set your id as below
        checkout.setKeyID(RazorPayKey);

        // set image
        checkout.setImage(R.drawable.notification_logo);

        // initialize json object
        JSONObject object = new JSONObject();

        if (!Constants.USER_EMAIL.isEmpty() || !Constants.USER_PHONE.isEmpty()) {
            try {
                object.put("name", "FurniFind Payment");
                object.put("description", "Order Payment");
                object.put("theme.color", "FurniFind Payment");
                object.put("currency", "INR");
                object.put("amount", amount);
                object.put("prefill.contact", Constants.USER_PHONE);
                object.put("prefill.email", Constants.USER_EMAIL);

                checkout.open(PaymentActivity.this, object);

            } catch (JSONException e) {
                Log.e("JSON", "Error in starting Razorpay Checkout :: "+ e.getMessage());
                Toast.makeText(this, "something went wrong, Please try again", Toast.LENGTH_SHORT).show();
                GotoCart();
            }
        } else {
            try {
                object.put("name", "FurniFind Payment");
                object.put("description", "Order Payment");
                object.put("currency", "INR");
                object.put("amount", amount);
                object.put("prefill.contact", "9284064503");
                object.put("prefill.email", "chaitanyamunje@gmail.com");

                checkout.open(PaymentActivity.this, object);

            } catch (JSONException e) {
                Log.e("JSON", "Error in starting Razorpay Checkout :: " + e.getMessage());
                Toast.makeText(this, "something went wrong, Please try again", Toast.LENGTH_SHORT).show();
                GotoCart();
            }


        }

    }

    private void SetFinalPrice(ArrayList<OrderModel> finalOrderList) {
        String ShowPrice = finalOrderList.get(0).getTotalCartPrice();

        Locale indiaLocale = new Locale("en", "IN");

        float Deli = Float.parseFloat(ShowPrice);
        float deliveryCharge = (Deli * 2) / 100;
        String Delistr = String.format(indiaLocale,"%.2f", deliveryCharge);

        Float FinalValue = Deli + deliveryCharge;
        finalStr = String.format(indiaLocale,"%.2f", FinalValue);


        TotalPrice.setText(ShowPrice);
        DeliveryCharge.setText("Rs. " + Delistr);
        FinalPrice.setText("Rs. " + finalStr);

    }

    private void SaveToFirebase(ArrayList<OrderModel> finalOrderList) {
        payBtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        String paymentdetials = finalOrderList.get(0).getPaymentDone();
        String UserID = finalOrderList.get(0).getUserID();


        if (paymentdetials == null || paymentdetials.isEmpty() || paymentdetials.equals("")) {
            Toast.makeText(this, "Please make the payment first !! ", Toast.LENGTH_SHORT).show();

        } else {

            OrderModel model = FinalOrderList.get(0);

            Map<String, Object> OrderObj = new HashMap<>();
            OrderObj.put("userName", FinalOrderList.get(0).getUserName());
            OrderObj.put("userID", FinalOrderList.get(0).getUserID());
            OrderObj.put("productList", FinalOrderList.get(0).getProductList());
            OrderObj.put("address", FinalOrderList.get(0).getAddress());
            OrderObj.put("totalCartPrice", FinalOrderList.get(0).getTotalCartPrice());
            OrderObj.put("paymentDone", FinalOrderList.get(0).getPaymentDone());
            OrderObj.put("orderTime", FinalOrderList.get(0).getOrderTime());

            if (model != null){
                reference.child(UserID).push().setValue(OrderObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            DatabaseReference databaseReference2 = database.getReference("/Cart");
                            databaseReference2.child(UserID).removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                                    payBtn.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);

                                    GoToHome();

                                }
                            });
                        }



                    }
                });
            }else
                Log.d("Order Error" , "Order is empty");


        }


    }

    @Override
    public void onPaymentSuccess(String s) {
        waitOrderTxt.setText("Please Wait for Order To Submit");
        FinalOrderList.get(0).setPaymentDone("Done");
        FinalOrderList.get(0).setOrderTime(String.valueOf(System.currentTimeMillis()));
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();
        SaveToFirebase(FinalOrderList);
    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(this, "Oops something went wrong", Toast.LENGTH_SHORT).show();
        GotoCart();
    }

    @Override
    public void onClick(View v) {

        int Id = v.getId();

        if (Id == R.id.place_order_btn) {

            PayBtnFun();

        }

    }

    private void PayBtnFun() {
        if (IsListOk) {
            samount = FinalOrderList.get(0).getTotalCartPrice();
            if (samount != null && !samount.trim().isEmpty()) {

                PaymentFun(samount);

            } else {
                // Handle the case where samount is null or empty
                Toast.makeText(this, "something went wrong ", Toast.LENGTH_SHORT).show();
                GotoCart();
            }
        } else {

            Toast.makeText(this, "something went wrong ", Toast.LENGTH_SHORT).show();
            GotoCart();

        }
    }

    private void GotoCart() {

        CartFragment fragment = new CartFragment();
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.frag_container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void GoToHome(){

        Intent ihome = new Intent(PaymentActivity.this, ShoppingActivity.class);
        startActivity(ihome);
        finish();

    }
}