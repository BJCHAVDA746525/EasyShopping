package com.example.easyshopping.Model;

import androidx.room.Ignore;

public class NotificationModel {
    String Notification_Title, Notification_Body;

    public NotificationModel() {

    }
    @Ignore
    public NotificationModel(String notificationMsg, String notificationTitle) {
        Notification_Body = notificationMsg;
        Notification_Title = notificationTitle;
    }


    public String getNotification_Body() {
        return Notification_Body;
    }

    public void setNotification_Body(String notification_Body) {
        Notification_Body = notification_Body;
    }

    public String getNotification_Title() {
        return Notification_Title;
    }

    public void setNotification_Title(String notification_Title) {
        Notification_Title = notification_Title;
    }
}
