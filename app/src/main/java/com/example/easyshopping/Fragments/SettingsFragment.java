package com.example.easyshopping.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.easyshopping.Activities.LoginActivity;
import com.example.easyshopping.Activities.RegisterActivity;
import com.example.easyshopping.Data.Room_Helper;
import com.example.easyshopping.Data.UserDataModel;
import com.example.easyshopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;
import java.util.Objects;


public class SettingsFragment extends Fragment implements View.OnClickListener {

    RelativeLayout EditprofileLayout,AddressLayout;

    FirebaseUser user;
    FirebaseAuth auth;
    Room_Helper helper;



    public SettingsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);
        Initview(view);



        return view;
    }

    private void Initview(View view) {
        /*  Ids  */
        EditprofileLayout = view.findViewById(R.id.edit_profile_layout_click);
        AddressLayout = view.findViewById(R.id.address_management_layout_click);


        /*  OnClicks  */
        EditprofileLayout.setOnClickListener(this);
        AddressLayout.setOnClickListener(this);


        user = FirebaseAuth.getInstance().getCurrentUser();
        helper = Room_Helper.GetDB(getActivity());
        auth = FirebaseAuth.getInstance();



    }

    @Override
    public void onClick(View view) {

        int Id = view.getId();

        if (Id == R.id.edit_profile_layout_click) {
            EditProfileFragment editProfileFragment = new EditProfileFragment();
            fragmentLoad(editProfileFragment);
        } else if (Id == R.id.address_management_layout_click) {
            AddressEditFragment addressEditFragment = new AddressEditFragment();
            fragmentLoad(addressEditFragment);
        }


    }


    private void fragmentLoad(Fragment fragment) {

        FragmentManager fm = requireActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.frag_container,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void Logout() {
        auth.signOut();
        helper.room_dao().ClearCart();
        helper.room_dao().ClearProducts();
        helper.room_dao().ClearUserData();

        Intent ilogin = new Intent(getActivity(), LoginActivity.class);
        startActivity(ilogin);

    }
}