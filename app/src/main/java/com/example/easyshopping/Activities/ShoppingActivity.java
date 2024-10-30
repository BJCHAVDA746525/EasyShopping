package com.example.easyshopping.Activities;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.easyshopping.Fragments.CartFragment;
import com.example.easyshopping.Fragments.HomeFragment;
import com.example.easyshopping.Fragments.ProfileFragment;
import com.example.easyshopping.Fragments.StoreFragment;
import com.example.easyshopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

public class ShoppingActivity extends AppCompatActivity {
    BottomNavigationView BottomNV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Initview();
        FCMToken();

        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();


        BottomNV.setVisibility(View.VISIBLE);

        BottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemid = item.getItemId();

                if (itemid == R.id.home_menu) {

                    loadfrag(new HomeFragment(), 1, "home", true);

                } else if (itemid == R.id.store_menu) {

                    loadfrag(new StoreFragment(), 0, "store", false);

                } else if (itemid == R.id.cart_menu) {

                    loadfrag(new CartFragment(), 0, "cart", false);

                } else if (itemid == R.id.profile_menu) {

                    loadfrag(new ProfileFragment(), 0, "profile", false);

                }
                return true;
            }
        });
        BottomNV.setSelectedItemId(R.id.home_menu);
    }

    private void FCMToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = token;
                        Log.d("Tokken", msg);

                    }
                });
    }

    private void Initview() {
        BottomNV = findViewById(R.id.bottomNV);
    }


    public void loadfrag(Fragment fragment, int flag, String Tag, boolean clearBackStack) {
        FragmentManager fm = getSupportFragmentManager();

        // Clear the backstack when navigating to HomeFragment
        if (clearBackStack) {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        FragmentTransaction ft = fm.beginTransaction();

        // Decide whether to add or replace the fragment
        if (flag == 0)
            ft.add(R.id.frag_container, fragment);
        else
            ft.replace(R.id.frag_container, fragment);

        // Add to backstack only if it's not HomeFragment (to avoid stacking home multiple times)
        if (!clearBackStack) {
            ft.addToBackStack(null);
        }

        ft.commit();
    }


}