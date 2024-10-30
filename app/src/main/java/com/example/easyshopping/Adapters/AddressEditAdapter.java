package com.example.easyshopping.Adapters;

import android.app.Activity;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyshopping.Data.Room_Helper;
import com.example.easyshopping.Model.AddressModel;
import com.example.easyshopping.R;
import com.example.easyshopping.Utils.Dialogs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddressEditAdapter extends RecyclerView.Adapter<AddressEditAdapter.viewholder> {

    ArrayList<AddressModel> arrayList;
    Activity activity;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Room_Helper helper;

    String Uid;


    public AddressEditAdapter(ArrayList<AddressModel> arrayList,Activity activity) {
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AddressEditAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_rv_layout,parent,false);
        return new AddressEditAdapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressEditAdapter.viewholder holder, int position) {

        int pos = position;

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("/AddressBook");
        helper = Room_Helper.GetDB(activity);

        String FireID = arrayList.get(position).getFireID();

        Uid = helper.room_dao().getalluserdetails().get(0).getUserID();

        holder.selectionbtn.setVisibility(View.GONE);

        holder.AddLine1.setText(arrayList.get(position).getAddLine1());
        holder.AddLine2.setText(arrayList.get(position).getAddLine2());
        holder.City.setText(arrayList.get(position).getCity());
        holder.PinCode.setText(arrayList.get(position).getPinCode());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!FireID.isEmpty()){


                    BottomSheetDialog MainDialog = new BottomSheetDialog(activity);
                    MainDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    MainDialog.setCancelable(false);
                    MainDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
                    MainDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    MainDialog.setContentView(R.layout.address_edit_layout);

                    AppCompatButton EditAdd,DeleteAdd,CancelBtn;

                    EditAdd = MainDialog.findViewById(R.id.edit_address);
                    DeleteAdd = MainDialog.findViewById(R.id.delete_address);
                    CancelBtn = MainDialog.findViewById(R.id.cancel_btn_edt_address);

                    EditAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AddressEditFun(pos,FireID);
                            MainDialog.dismiss();
                        }
                    });

                    DeleteAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DeleteAddress(view,pos,FireID);
                            MainDialog.dismiss();
                        }
                    });
                    CancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MainDialog.dismiss();
                        }
                    });

                    MainDialog.show();
                }

            }

        });




    }

    private void AddressEditFun(int pos, String fireID) {
        final BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(R.layout.add_edit_dailog);

        EditText AddLine1, AddLine2, City, Pincode;
        AddLine1 = dialog.findViewById(R.id.address_line_1_edt);
        AddLine2 = dialog.findViewById(R.id.address_line_2_edt);
        City = dialog.findViewById(R.id.city_edt);
        Pincode = dialog.findViewById(R.id.pincode_edt);

        AddLine1.setText(arrayList.get(pos).getAddLine1());
        AddLine2.setText(arrayList.get(pos).getAddLine2());
        City.setText(arrayList.get(pos).getCity());
        Pincode.setText(arrayList.get(pos).getPinCode());


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

                        databaseReference.child(Uid).child(fireID).setValue(CartItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                Toast.makeText(activity, "Address Updated Successfully", Toast.LENGTH_SHORT).show();
                                arrayList.set(pos,new AddressModel(AddL1,AddL2,CityEd,PincodeEd,Uid,fireID,"uncheck"));
                                notifyItemChanged(pos);

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

    private void DeleteAddress(View view, int pos, String fireID){

        final BottomSheetDialog Deletedialog = new BottomSheetDialog(activity);
        Deletedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Deletedialog.setCancelable(false);
        Deletedialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        Deletedialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Deletedialog.setContentView(R.layout.delete_add_layout);

        AppCompatButton YesBtn, NoBtn;
        YesBtn = Deletedialog.findViewById(R.id.delete_add_yes_btn);
        NoBtn = Deletedialog.findViewById(R.id.delete_add_no_btn);


        YesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseReference.child(Uid).child(fireID).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Deletedialog.dismiss();
                    }
                });

            }
        });

        NoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Deletedialog.dismiss();
            }
        });



        Deletedialog.show();


    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView AddLine1,AddLine2,City,PinCode;
       ImageView selectionbtn;
        RelativeLayout layout;

        public viewholder(@NonNull View itemView) {
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
