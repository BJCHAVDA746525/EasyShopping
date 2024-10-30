package com.example.easyshopping.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.easyshopping.Activities.RegisterActivity;
import com.example.easyshopping.Data.Room_Helper;
import com.example.easyshopping.Data.UserDataModel;
import com.example.easyshopping.R;
import com.example.easyshopping.Utils.Constants;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class ProfileFragment extends Fragment implements View.OnClickListener {


    TextView UserNameTxt, UserEmailTxt;
    Room_Helper helper;

    ArrayList<UserDataModel> userDataList;

    RelativeLayout SettingLayout, NotificationLayout, OrderLayout, PPLayout, TCLayout, LogOutLayout;

    FirebaseAuth auth;
    ImageView userdp;
    FirebaseUser user;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        InitView(view);
        UserLoad();


        return view;
    }

    private void UserLoad() {
        userDataList.addAll(helper.room_dao().getalluserdetails());

        if (!userDataList.isEmpty()) {
            UserNameTxt.setText(userDataList.get(0).getUserName());
            UserEmailTxt.setText(userDataList.get(0).getUseremail());
        }
        String photoUrl = Constants.USER_PHOTO;

        Glide.with(getActivity()).load(photoUrl).into(userdp);


    }

    private void InitView(View view) {
        UserNameTxt = view.findViewById(R.id.user_name_txt);
        UserEmailTxt = view.findViewById(R.id.user_email_txt);

        SettingLayout = view.findViewById(R.id.setting_layout_click);
        NotificationLayout = view.findViewById(R.id.noti_layout_click);
        OrderLayout = view.findViewById(R.id.order_layout_click);
        PPLayout = view.findViewById(R.id.pp_layout_click);
        TCLayout = view.findViewById(R.id.tc_layout_click);
        LogOutLayout = view.findViewById(R.id.logout_layout_click);
        userdp = view.findViewById(R.id.userdp);


        LogOutLayout.setOnClickListener(this);
        SettingLayout.setOnClickListener(this);
        NotificationLayout.setOnClickListener(this);
        TCLayout.setOnClickListener(this);
        PPLayout.setOnClickListener(this);
        OrderLayout.setOnClickListener(this);


        helper = Room_Helper.GetDB(getActivity());
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();


        userDataList = new ArrayList<>();

    }

    @Override
    public void onClick(View view) {
        int ID = view.getId();

        if (ID == R.id.setting_layout_click) {
            SettingsFragment settingsFragment = new SettingsFragment();
            FragmentLoad(settingsFragment);
        } else if (ID == R.id.noti_layout_click) {
            NotificationFragment notificationFragment = new NotificationFragment();
            FragmentLoad(notificationFragment);
        } else if (ID == R.id.order_layout_click) {

            OrderHistoryFragment orderFragment = new OrderHistoryFragment();
            FragmentLoad(orderFragment);

        } else if (ID == R.id.pp_layout_click) {

            TermsAndPrivacyDialog("Privacy");

        } else if (ID == R.id.tc_layout_click) {

            TermsAndPrivacyDialog("Terms");

        } else if (ID == R.id.logout_layout_click) {
            Logout();
        }

    }

    private void TermsAndPrivacyDialog(String privacy) {

        BottomSheetDialog TermsDialog = new BottomSheetDialog(getActivity());
        TermsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        TermsDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        TermsDialog.getWindow().setWindowAnimations(R.style.SlideAnimation);
        TermsDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        TermsDialog.setContentView(R.layout.terms_layout);

        NestedScrollView nestedScrollView = TermsDialog.findViewById(R.id.nestedscroll);
        TermsDialog.getBehavior().setDraggable(false);
        nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Check if the user is at the top of the NestedScrollView
                if (!nestedScrollView.canScrollVertically(-1)) {
                    // Re-enable dragging when the user is at the top
                    TermsDialog.getBehavior().setDraggable(true);
                } else {
                    // Disable dragging while scrolling
                    TermsDialog.getBehavior().setDraggable(false);
                }
            }
        });

        TextView TermsTitle, TermsDetails;
        TermsTitle = TermsDialog.findViewById(R.id.terms_title_dialog);
        TermsDetails = TermsDialog.findViewById(R.id.terms_intro);


        if (privacy.equals("Terms")) {

            TermsTitle.setText(R.string.terms_conditions);
            TermsDetails.setText(R.string.terms_details);

        } else {
            TermsTitle.setText(R.string.privacy_policy);
            TermsDetails.setText(R.string.privacy_details);
        }


        TermsDialog.show();

    }


    private void Logout() {
        auth.signOut();
        helper.room_dao().ClearCart();
        helper.room_dao().ClearProducts();
        helper.room_dao().ClearUserData();

        Intent ilogin = new Intent(getActivity(), RegisterActivity.class);
        startActivity(ilogin);

    }

    private void FragmentLoad(Fragment fragment) {
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.frag_container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}