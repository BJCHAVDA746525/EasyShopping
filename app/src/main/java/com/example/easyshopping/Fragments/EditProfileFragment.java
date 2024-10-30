package com.example.easyshopping.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.easyshopping.Data.Room_Helper;
import com.example.easyshopping.Data.UserDataModel;
import com.example.easyshopping.R;
import com.example.easyshopping.Utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;


public class EditProfileFragment extends Fragment implements View.OnClickListener {

    ImageView UserDP;

    String FireName, uid, RoomName, FireEmail;

    String roomProfileUrl;

    FirebaseUser user;

    private static final int CAM_REQ_CODE = 100;
    private static final int GALL_REQ_CODE = 1000;

    Room_Helper helper;

    ArrayList<UserDataModel> userdataRoom;

    int roomid;


    RelativeLayout ChangeNameLayout,ChangePassLayout,EmailLayout,DpChangeLayout;



    public EditProfileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        Initview(view);
        SettextEdittext();


        return view;
    }

    private void SettextEdittext() {

        userdataRoom.addAll(helper.room_dao().getalluserdetails());

        if (userdataRoom != null){
            try {

                RoomName = userdataRoom.get(0).getUserName();
                roomid = userdataRoom.get(0).getRoomId();
                roomProfileUrl = userdataRoom.get(0).getUserphotoURL();


                Glide.with(getContext()).load(roomProfileUrl).into(UserDP);

            } catch (Exception e) {
                Log.e("Room data fetch Error",e.getMessage());
            }
        }



        if (user != null) {
            // Name, email address, and profile photo Url
            FireName = user.getDisplayName();
            FireEmail = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            uid = user.getUid();


        }





    }

    private void Initview(View view) {

        UserDP = view.findViewById(R.id.changedp_img);

        user = FirebaseAuth.getInstance().getCurrentUser();
        helper = Room_Helper.GetDB(getContext());
        userdataRoom = new ArrayList<>();

        DpChangeLayout = view.findViewById(R.id.user_profile_photo_layout);
        ChangeNameLayout = view.findViewById(R.id.edit_profile_layout_click);
        ChangePassLayout = view.findViewById(R.id.change_pass_layout_click);
        EmailLayout = view.findViewById(R.id.change_email_layout_click);


        //  OnClicks
        ChangeNameLayout.setOnClickListener(this);
        ChangePassLayout.setOnClickListener(this);
        EmailLayout.setOnClickListener(this);
        DpChangeLayout.setOnClickListener(this);




    }

    @Override
    public void onClick(View view) {
        int ID = view.getId();

        if (ID == R.id.edit_profile_layout_click) {
            UserNameEdit();
        } else if (ID == R.id.change_pass_layout_click) {
            ChangePassword();
        } else if (ID == R.id.change_email_layout_click) {
            ChangeEmail();
        } else if (ID == R.id.user_profile_photo_layout) {
            UpdateDP();
        }

    }

    private void UpdateDP() {

        final BottomSheetDialog dialog = new BottomSheetDialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.updatedp_layout);

        LinearLayout Camera = dialog.findViewById(R.id.CameraDP_layout);
        LinearLayout Gallery = dialog.findViewById(R.id.GallaryDP_layout);


        assert Camera != null;
        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent icam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(icam, CAM_REQ_CODE);

                dialog.dismiss();

            }
        });

        assert Gallery != null;
        Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent igallery = new Intent(Intent.ACTION_PICK);
                igallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(igallery, GALL_REQ_CODE);

                dialog.dismiss();

            }
        });


        dialog.show();


    }

    private void UserNameEdit() {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(R.layout.email_pass_dailog_layout);


        EditText ChangeName = dialog.findViewById(R.id.email_pass_edt);
        TextView TitleUpdate = dialog.findViewById(R.id.email_pass_title);
        AppCompatButton SaveBtn = dialog.findViewById(R.id.email_pass_save_btn);
        AppCompatButton CancelBtn = dialog.findViewById(R.id.email_pass_cancel_btn);


        TitleUpdate.setText("Edit Name");
        ChangeName.setHint(Constants.USER_NAME);


        if (SaveBtn != null) {
            SaveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String UpdateName = ChangeName.getText().toString().trim();

                    if (!UpdateName.isEmpty()){

                        if (!UpdateName.equals(FireName)){

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(UpdateName)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                helper.room_dao().Updateusername(UpdateName,roomid);
                                                Toast.makeText(getActivity(), "Username changed successfully", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();

                                            }
                                        }
                                    });
                        }else
                            dialog.dismiss();

                    }else {
                        Toast.makeText(getContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        assert CancelBtn != null;
        CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    private void ChangeEmail() {

        final BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setWindowAnimations(R.style.SlideAnimation);
        dialog.setContentView(R.layout.email_pass_dailog_layout);

        EditText UpdateEmail;
        AppCompatButton SavePass, CancelPass;
        LinearLayout progPass, BtnLayout;
        TextView TitleUpdate, EmailNote;

        UpdateEmail = dialog.findViewById(R.id.email_pass_edt);
        SavePass = dialog.findViewById(R.id.email_pass_save_btn);
        CancelPass = dialog.findViewById(R.id.email_pass_cancel_btn);
        progPass = dialog.findViewById(R.id.email_pass_progressBarEDT);
        BtnLayout = dialog.findViewById(R.id.email_pass_save_layout);
        TitleUpdate = dialog.findViewById(R.id.email_pass_title);
        EmailNote = dialog.findViewById(R.id.email_note_txt);

        EmailNote.setVisibility(View.VISIBLE);

        UpdateEmail.setHint(Constants.USER_EMAIL);
        TitleUpdate.setText("Email Update");

        SavePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BtnLayout.setVisibility(View.GONE);
                progPass.setVisibility(View.VISIBLE);

                String UserEmail = UpdateEmail.getText().toString().trim();

                if (!UserEmail.isEmpty()) {

                    if (UserEmail.length() > 3) {

                        user.verifyBeforeUpdateEmail(UserEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                BtnLayout.setVisibility(View.VISIBLE);
                                progPass.setVisibility(View.GONE);

                                Toast.makeText(getContext(), "Email sent successfully ", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

                            }
                        });


                    } else {
                        Toast.makeText(getContext(), "Please set password minimum 3 character", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(), "Please fill the new Password", Toast.LENGTH_SHORT).show();
                }

            }


        });

        CancelPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void ChangePassword() {

        final BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setWindowAnimations(R.style.SlideAnimation);
        dialog.setContentView(R.layout.email_pass_dailog_layout);

        EditText updatedPassword;
        AppCompatButton SavePass, CancelPass;
        LinearLayout progPass, BtnLayout;
        TextView TitleUpdate;

        updatedPassword = dialog.findViewById(R.id.email_pass_edt);
        SavePass = dialog.findViewById(R.id.email_pass_save_btn);
        CancelPass = dialog.findViewById(R.id.email_pass_cancel_btn);
        progPass = dialog.findViewById(R.id.email_pass_progressBarEDT);
        BtnLayout = dialog.findViewById(R.id.email_pass_save_layout);
        TitleUpdate = dialog.findViewById(R.id.email_pass_title);


        updatedPassword.setHint("Please enter new password");

        TitleUpdate.setText("Change Password");

        SavePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BtnLayout.setVisibility(View.GONE);
                progPass.setVisibility(View.VISIBLE);

                String UserPass = updatedPassword.getText().toString().trim();

                if (!UserPass.isEmpty()) {

                    if (UserPass.length() > 3) {

                        user.updatePassword(UserPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                BtnLayout.setVisibility(View.VISIBLE);
                                progPass.setVisibility(View.GONE);

                                Toast.makeText(getContext(), "Password updated successfully ", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                    } else {
                        Toast.makeText(getContext(), "Please set password minimum 3 character", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(), "Please fill the new Password", Toast.LENGTH_SHORT).show();
                }

            }


        });

        CancelPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            Uri fireDP;

            if (requestCode == CAM_REQ_CODE) {

                Bitmap imgbit = (Bitmap) (data.getExtras().get("data"));
                UserDP.setImageBitmap(imgbit);
                fireDP = data.getData();

                UpdateDPfirebase(fireDP);

            } else if (requestCode == GALL_REQ_CODE) {


                UserDP.setImageURI(data.getData());

                fireDP = data.getData();
                UpdateDPfirebase(fireDP);

            }
        }
    }

    private void UpdateDPfirebase(Uri fireDP) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(fireDP)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {


                            Toast.makeText(getActivity(), "Profile photo changed successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}