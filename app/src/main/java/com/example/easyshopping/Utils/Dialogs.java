package com.example.easyshopping.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.easyshopping.Data.Room_Helper;
import com.example.easyshopping.Model.OrderModel;
import com.example.easyshopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Dialogs {

    public void NewAddressADD(Activity activity) {

        final BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(R.layout.add_address_dailog);

        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;

        Room_Helper helper;


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("/AddressBook");
        helper = Room_Helper.GetDB(activity);

        EditText AddLine1, AddLine2, City, Pincode;
        AddLine1 = dialog.findViewById(R.id.address_line_1_edt);
        AddLine2 = dialog.findViewById(R.id.address_line_2_edt);
        City = dialog.findViewById(R.id.city_edt);
        Pincode = dialog.findViewById(R.id.pincode_edt);


        LinearLayout saveAddress = dialog.findViewById(R.id.save_address_layout);
        LinearLayout cancel_address_layout = dialog.findViewById(R.id.cancel_address_layout);

        saveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String AddL1, AddL2, CityEd, PincodeEd;
                String Uid;

                AddL1 = AddLine1.getText().toString().trim();
                AddL2 = AddLine2.getText().toString().trim();
                CityEd = City.getText().toString().trim();
                PincodeEd = Pincode.getText().toString().trim();
                Uid = helper.room_dao().getalluserdetails().get(0).getUserID();

                if (AddL1.isEmpty() || AddL2.isEmpty() || CityEd.isEmpty() || PincodeEd.isEmpty()) {

                    Toast.makeText(activity, "Something is empty ", Toast.LENGTH_SHORT).show();

                } else {

                    if (Uid.isEmpty()) {

                        Log.d("Uid", "UserId Missing");

                    } else {


                        Map<String, String> CartItem = new HashMap<>();
                        CartItem.put("AddLine1", AddL1);
                        CartItem.put("AddLine2", AddL2);
                        CartItem.put("City", CityEd);
                        CartItem.put("PinCode", PincodeEd);
                        CartItem.put("Uid", Uid);
                        CartItem.put("FireAddID", "");

                        databaseReference.child(Uid).push().setValue(CartItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(activity, "Address Added Successfully", Toast.LENGTH_SHORT).show();

                                dialog.dismiss();
                            }
                        });


                    }
                }

            }
        });

        cancel_address_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }



}






