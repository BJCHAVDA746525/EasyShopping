package com.example.easyshopping.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.easyshopping.Data.Room_Helper;
import com.example.easyshopping.Data.UserDataModel;
import com.example.easyshopping.R;
import com.example.easyshopping.WorkManager.SyncScheduler;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    //Objects
    Room_Helper helper;
    LottieAnimationView SplashAnimation;

    //Lists
    ArrayList<UserDataModel> user;

    private static final int PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Initview();
        SyncScheduler syncScheduler = new SyncScheduler();
        syncScheduler.ScheduleFirebaseSync();

        // Check and request multiple permissions at once
        checkAndRequestPermissions();


    }

    private void Initview() {
        SplashAnimation = findViewById(R.id.splashAnimation);
        helper = Room_Helper.GetDB(this);
    }

    // Method to check and request multiple permissions
    private void checkAndRequestPermissions() {
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.POST_NOTIFICATIONS};

        List<String> listPermissionsNeeded = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), PERMISSION_REQUEST_CODE);
        } else {
            // All permissions are already granted
            LoginCheck();
        }
    }

    private void LoginCheck() {
        SplashAnimation.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> {

            FirebaseAuth auth = FirebaseAuth.getInstance();
            if (auth.getCurrentUser() != null) {

                SyncScheduler syncScheduler = new SyncScheduler();
                syncScheduler.UserSync();

                Intent intent = new Intent(SplashActivity.this, ShoppingActivity.class);
                startActivity(intent);
                finish();
                SplashAnimation.setVisibility(View.GONE);

            } else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                SplashAnimation.setVisibility(View.GONE);
            }


        }, 1000);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == PERMISSION_REQUEST_CODE) {
            Map<String, Integer> permissionResults = new HashMap<>();
            permissionResults.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
            permissionResults.put(Manifest.permission.POST_NOTIFICATIONS, PackageManager.PERMISSION_GRANTED);

            for (int i = 0; i < permissions.length; i++) {
                permissionResults.put(permissions[i], grantResults[i]);
            }

            // Check if any permission is denied
            boolean allPermissionsGranted = true;
            for (String permission : permissionResults.keySet()) {
                if (permissionResults.get(permission) != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (!allPermissionsGranted) {
                Toast.makeText(this, "Some permissions are denied!", Toast.LENGTH_SHORT).show();


            }

            LoginCheck();

        }
    }


}