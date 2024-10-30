package com.example.easyshopping.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyshopping.Adapters.HomeProductAdapter;
import com.example.easyshopping.Adapters.NewCollectionAdapter;
import com.example.easyshopping.Data.Room_Helper;
import com.example.easyshopping.Data.Room_Product_Model;
import com.example.easyshopping.R;
import com.example.easyshopping.WorkManager.SyncScheduler;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {

    //Views
    RecyclerView NewCollectionRecView, ProductRecyclerView;
    HomeProductAdapter productAdapter;
    NewCollectionAdapter newCollectionAdapter;
    Room_Helper helper;
    ProgressBar progressBar;
    RelativeLayout LastPageLayout;
    GridLayoutManager gridLayoutManager;
    ImageView notifyIconFrag;
    TextView ContinueShoppingBtn, seeMoreBtn;
    SearchView SearchButton;

    //Lists
    ArrayList<Room_Product_Model> Product_list_home;
    ArrayList<Room_Product_Model> NewCollectionList;

    //Objects
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private ImageView searchIcon;

    //Variables
    private boolean isLoading = false;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        InitView(view);
        PopBackStack();
        newCollection();
        productRV();
        FirstDataLoad();
        addScrollListener();

        // Setup Toolbar
        Toolbar toolbar = view.findViewById(R.id.home_title_section);
        // Set the Toolbar as the ActionBar
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }

        // Additional setup for Toolbar (if needed)
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Home");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);  // Hide back button if needed
        }


        SearchButton.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchProducts(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchProducts(newText);
                return false;
            }
        });


        return view;
    }

    private void PopBackStack() {

        /*FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);*/

    }


    private void InitView(View view) {

        NewCollectionRecView = view.findViewById(R.id.recycler_ad);
        ProductRecyclerView = view.findViewById(R.id.home_recyclerview);
        progressBar = view.findViewById(R.id.progressbar_rv);
        notifyIconFrag = view.findViewById(R.id.notify_icon_frag);
        LastPageLayout = view.findViewById(R.id.home_dataLoading);
        ContinueShoppingBtn = view.findViewById(R.id.ContinueshoppingBtn);
        seeMoreBtn = view.findViewById(R.id.see_more);
        SearchButton = view.findViewById(R.id.search_icon_frag_home);


        Product_list_home = new ArrayList<>();
        NewCollectionList = new ArrayList<>();

        helper = Room_Helper.GetDB(getActivity());
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Products");


        notifyIconFrag.setOnClickListener(this);
        ContinueShoppingBtn.setOnClickListener(this);
        seeMoreBtn.setOnClickListener(this);
        SearchButton.setOnClickListener(this);


    }

    private void newCollection() {
        newCollectionAdapter = new NewCollectionAdapter(NewCollectionList, getActivity());
        NewCollectionRecView.setNestedScrollingEnabled(false);
        NewCollectionRecView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        NewCollectionRecView.setAdapter(newCollectionAdapter);
    }

    private void productRV() {
        ProductRecyclerView.setNestedScrollingEnabled(false);
        ProductRecyclerView.setLayoutManager(gridLayoutManager);
    }

    private void loadAllProducts(ArrayList<Room_Product_Model> product_list_home) {

        productAdapter = new HomeProductAdapter(product_list_home, getActivity());
        ProductRecyclerView.setAdapter(productAdapter);
    }

    private void searchProducts(String query) {
        String searchQuery = "%" + query + "%";
        ArrayList<Room_Product_Model> searchResults = (ArrayList<Room_Product_Model>) helper.room_dao().searchProducts(searchQuery);
        productAdapter.setProductList(searchResults);
    }

    @Override
    public void onClick(View view) {
        int ID = view.getId();

        if (ID == R.id.notify_icon_frag) {

            NotificationFragment fragment = new NotificationFragment();
            LoadFragment(fragment);

        } else if (ID == R.id.ContinueshoppingBtn) {

            StoreFragment fragment = new StoreFragment();
            LoadFragment(fragment);

        } else if (ID == R.id.see_more) {

            StoreFragment fragment = new StoreFragment();
            LoadFragment(fragment);


        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void FirstDataLoad() {


        if (Product_list_home.isEmpty()) {

            ArrayList<Room_Product_Model> list = (ArrayList<Room_Product_Model>) helper.room_dao().Get50Products();
            ArrayList<Room_Product_Model> NewRandom = (ArrayList<Room_Product_Model>) helper.room_dao().GetRandomProducts();

            if (list == null || list.isEmpty() || NewRandom == null || NewRandom.isEmpty()) {

                AddFirebaseDB();

            } else {

                Product_list_home.addAll(list);
                NewCollectionList.addAll(NewRandom);

                loadAllProducts(Product_list_home);

                productAdapter.notifyDataSetChanged();
                newCollectionAdapter.notifyDataSetChanged();

            }

        }


    }

    private void LoadFragment(Fragment fragment){
        FragmentManager fm = (getActivity()).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frag_container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void addScrollListener() {
        ProductRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                // Check if the RecyclerView is scrolled to the bottom
                if (!ProductRecyclerView.canScrollVertically(1) && !isLoading) {

                    if (Product_list_home.size() >= 100){
                        LastPageLayout.setVisibility(View.VISIBLE);
                    }else
                        LoadMoreData();
                }

            }
        });
    }

    private void LoadMoreData() {
        int pageSize = 20;  // Number of products to load per page
        int offset = Product_list_home.size();  // Offset is the current number of products in the list

        // Fetch the next 20 products based on offset
        List<Room_Product_Model> productdatapage = helper.room_dao().getProductPage(pageSize, offset);

        // Check if there are products to add
        if (productdatapage != null && !productdatapage.isEmpty()) {
            Product_list_home.addAll(productdatapage);  // Add new products to the list
            productAdapter.notifyDataSetChanged();  // Notify adapter about data changes
        } else {
            // Optionally handle the case where no more products are available
            Toast.makeText(getContext(), "No more products to load", Toast.LENGTH_SHORT).show();
        }
    }

    private void AddFirebaseDB() {
        SyncScheduler scheduler = new SyncScheduler();
        scheduler.ScheduleFirebaseSync();
    }


}