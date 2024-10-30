package com.example.easyshopping.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyshopping.Model.NotificationModel;
import com.example.easyshopping.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    ArrayList<NotificationModel> NotificationList;


    public NotificationAdapter(ArrayList<NotificationModel> notificationList) {
        NotificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_recycler,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {

        int positionId = position;

        holder.Title.setText(NotificationList.get(positionId).getNotification_Title());
        holder.Msg.setText(NotificationList.get(positionId).getNotification_Body());
        holder.NotificationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullNotificationShow(holder,
                        NotificationList.get(positionId).getNotification_Title(),
                        NotificationList.get(positionId).getNotification_Body());
            }
        });

    }

    private void FullNotificationShow(ViewHolder holder, String notificationTitle, String notificationBody) {
        BottomSheetDialog NotificationDialog = new BottomSheetDialog(holder.itemView.getContext());
        NotificationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        NotificationDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        NotificationDialog.getWindow().setWindowAnimations(R.style.SlideAnimation);
        NotificationDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        NotificationDialog.setContentView(R.layout.notification_dialog_layout);

        TextView NTitle,NMsg;
        NTitle = NotificationDialog.findViewById(R.id.notification_title_dialog);
        NMsg = NotificationDialog.findViewById(R.id.notification_body_dialog);

        NTitle.setText(notificationTitle);
        NMsg.setText(notificationBody);

        NotificationDialog.show();
    }

    @Override
    public int getItemCount() {
        return NotificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Title,Msg;
        RelativeLayout NotificationLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Title = itemView.findViewById(R.id.notifi_title);
            Msg = itemView.findViewById(R.id.notifi_message);
            NotificationLayout = itemView.findViewById(R.id.notification_layout);
        }
    }
}
