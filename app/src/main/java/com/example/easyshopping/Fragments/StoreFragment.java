package com.example.easyshopping.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyshopping.Adapters.StoreProductAdapter;
import com.example.easyshopping.Data.Room_Helper;
import com.example.easyshopping.Data.Room_Product_Model;
import com.example.easyshopping.Model.storeCategoryModel;
import com.example.easyshopping.R;
import com.example.easyshopping.Utils.Constants;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreFragment extends Fragment implements View.OnClickListener {


    ArrayList<storeCategoryModel> Product_cat_list;
    StoreProductAdapter product_adapter;
    RecyclerView Product_Rview;
    ArrayList<Room_Product_Model> Product_list_store;
    ArrayList<Room_Product_Model> Product_list_final;

    Room_Helper helper;
    HorizontalScrollView horizontalScrollView;
    ProgressBar progressBarStore;


    ImageView All_cat,Chair_cat,Stool_cat,Dresser_cat,Table_cat,Sofa_cat;

    String SelectedCat = "All_cat";
    private boolean isLoading = false;

    MaterialCardView card_filter;
    SearchView home_search_view;
    ImageView storeBackBtn;



    public StoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        getActivity().findViewById(R.id.bottomNV).setVisibility(View.VISIBLE);
        InitView(view);

        Products_list();
        FirstLoad();

        addScrollListener();

        initializeCategories();

        SearchQuery();




        return view;
    }

    private void SearchQuery() {
        home_search_view.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
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
    }

    private void searchProducts(String query) {
        String searchQuery = "%" + query + "%";
        ArrayList<Room_Product_Model> searchResults = (ArrayList<Room_Product_Model>) helper.room_dao().searchProducts(searchQuery);
        if (searchResults != null){
            if (!searchResults.isEmpty())
                product_adapter.setProductList(searchResults);
        }else {
            Toast.makeText(getActivity(), "Oops No Products Found", Toast.LENGTH_SHORT).show();
        }

    }

    private void InitView(View view) {
        Product_Rview = view.findViewById(R.id.store_recyclerview);

        progressBarStore = view.findViewById(R.id.progressbar_rv_store);
        horizontalScrollView = view.findViewById(R.id.horizontalScrollView);

        All_cat = view.findViewById(R.id.All_cat);
        Chair_cat = view.findViewById(R.id.Chair_cat);
        Stool_cat = view.findViewById(R.id.Stool_cat);
        Dresser_cat = view.findViewById(R.id.Dresser_cat);
        Table_cat = view.findViewById(R.id.Table_cat);
        Sofa_cat = view.findViewById(R.id.Sofa_cat);
        card_filter = view.findViewById(R.id.store_filter_card);
        home_search_view = view.findViewById(R.id.store_search_icon_frag);
        storeBackBtn = view.findViewById(R.id.store_back_img);

        All_cat.setOnClickListener(this);
        Chair_cat.setOnClickListener(this);
        Stool_cat.setOnClickListener(this);
        Dresser_cat.setOnClickListener(this);
        Table_cat.setOnClickListener(this);
        Sofa_cat.setOnClickListener(this);
        card_filter.setOnClickListener(this);
        home_search_view.setOnClickListener(this);
        storeBackBtn.setOnClickListener(this);



        Product_cat_list = new ArrayList<>();
        Product_list_store = new ArrayList<>();
        Product_list_final = new ArrayList<>();

        helper = Room_Helper.GetDB(getActivity());






        All_cat.setBackgroundResource(R.drawable.circle_cat_selected);

        DrawableCompat.setTint(
                DrawableCompat.wrap( All_cat.getDrawable()),
                ContextCompat.getColor(getActivity(), R.color.white)
        );


    }

    private void Products_list() {

        Product_Rview.setNestedScrollingEnabled(false);
        product_adapter = new StoreProductAdapter(Product_list_store, getActivity());
        Product_Rview.setAdapter(product_adapter);
        Product_Rview.setLayoutManager(new GridLayoutManager(getActivity(), 2));


    }

    private void addScrollListener() {
        Product_Rview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                // Check if the RecyclerView is scrolled to the bottom
                if (!Product_Rview.canScrollVertically(1) && !isLoading) {

                        LoadMoreData(SelectedCat);
                }

            }
        });
    }

    private void LoadMoreData(String selectedCat) {
        int pageSize = 20;  // Number of products to load per page
        int offset = Product_list_store.size();  // Offset is the current number of products in the list

        // Fetch the next 20 products based on offset
        List<Room_Product_Model> productdatapage = new ArrayList<>();

        if (selectedCat.equals("All_cat")) {
            // Fetch next 20 products from the Room database without category filter
            productdatapage = helper.room_dao().getProductPage(pageSize, offset);
        }

        // Check if there are products to add
        if (productdatapage != null && !productdatapage.isEmpty()) {

            Product_list_store.addAll(productdatapage);  // Add new products to the list
            product_adapter.notifyDataSetChanged();  // Notify adapter about data changes

        } else {
            // Optionally handle the case where no more products are available
            Toast.makeText(getContext(), "No more products to load", Toast.LENGTH_SHORT).show();
        }
    }

    private void FirstLoad() {

        if (Product_list_store.isEmpty()){
            ArrayList<Room_Product_Model> arrayLi = (ArrayList<Room_Product_Model>) helper.room_dao().Get50Products();
            Product_list_store.addAll(arrayLi);
            product_adapter.notifyDataSetChanged();

        }

    }

    @Override
    public void onClick(View view) {
        int ID = view.getId();
        if (ID == R.id.All_cat) {
            LoadCat("All_cat");
            SelectedCat = "All_cat";
        } else if (ID == R.id.Chair_cat) {
            LoadCat("Chair_cat");
            SelectedCat = "Chair";
        } else if (ID == R.id.Stool_cat) {
            LoadCat("Stool_cat");
            SelectedCat = "Stools";
        } else if (ID == R.id.Dresser_cat) {
            LoadCat("Dresser_cat");
            SelectedCat = "Dresser";
        } else if (ID == R.id.Table_cat) {
            LoadCat("Table_cat");
            SelectedCat = "Office Table";
        } else if (ID == R.id.Sofa_cat) {
            LoadCat("Sofa_cat");
            SelectedCat = "Sofas";
        }else if (ID == R.id.store_filter_card) {

            FilterDailog(new FilterDialogListener() {
                @Override
                public void onFilterApplied(ArrayList<String> filterValueArray) {

                    if (filterValueArray != null && !filterValueArray.isEmpty()) {
                        String categoryStr = filterValueArray.get(0);
                        float minPrice = Float.parseFloat(filterValueArray.get(1));
                        float maxPrice = Float.parseFloat(filterValueArray.get(2));

                        // Fetch and update the product list with filtered values

                        if (categoryStr.equals("All_cat")){

                            Product_list_store.clear();
                            Product_list_store.addAll(helper.room_dao().getFilteredProductListPrice(minPrice, maxPrice));
                            product_adapter.notifyDataSetChanged();

                        }else {
                            Product_list_store.clear();
                            Product_list_store.addAll(helper.room_dao().getFilteredProductList(categoryStr, minPrice, maxPrice));
                            product_adapter.notifyDataSetChanged();
                        }


                    }
                }
            });

        }else if (ID == R.id.store_back_img) {

            getFragmentManager().popBackStack(); // Go back to the previous fragment
        }
    }

    public interface FilterDialogListener {
        void onFilterApplied(ArrayList<String> filterValueArray);
    }

    private ArrayList<String>  FilterDailog(FilterDialogListener listener) {
        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(R.layout.filter_dailog_layout);

        RadioButton all_filter_btn,chair_filter_btn,stool_filter_btn,Dresser_filter_btn,Table_filter_btn,Sofa_filter_btn;
        AppCompatButton apply_btn;
        RangeSlider continuousRangeSlider;
        RadioGroup filterRadioGroup;
        TextView minValueTxt,maxValueTxt;

        ArrayList<String> filterValueArray = new ArrayList<>();

        filterRadioGroup = dialog.findViewById(R.id.filterRadioGroup1);

        all_filter_btn = dialog.findViewById(R.id.all_filter_btn);
        chair_filter_btn = dialog.findViewById(R.id.chair_filter_btn);
        stool_filter_btn = dialog.findViewById(R.id.stool_filter_btn);
        Dresser_filter_btn = dialog.findViewById(R.id.Dresser_filter_btn);
        Table_filter_btn = dialog.findViewById(R.id.Table_filter_btn);
        Sofa_filter_btn = dialog.findViewById(R.id.Sofa_filter_btn);
        minValueTxt = dialog.findViewById(R.id.minvalue_filter_txt);
        maxValueTxt = dialog.findViewById(R.id.maxvalue_filter_txt);

        apply_btn = dialog.findViewById(R.id.apply_filter_btn);

        continuousRangeSlider = dialog.findViewById(R.id.continuousRangeSlider);

        List<Float> values = continuousRangeSlider.getValues();
        float minValue = values.get(0);
        float maxValue = values.get(1);

        filterValueArray.add(0,Constants.CAT_ALLCATEGORY);
        filterValueArray.add(1, String.valueOf(minValue));
        filterValueArray.add(2, String.valueOf(maxValue));

        minValueTxt.setText( String.valueOf(minValue));
        maxValueTxt.setText(String.valueOf(maxValue));

        filterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.all_filter_btn) {

                    filterValueArray.set(0,Constants.CAT_ALLCATEGORY);

                } else if (checkedId == R.id.chair_filter_btn) {

                    filterValueArray.set(0,Constants.CAT_CHAIR);

                } else if (checkedId == R.id.stool_filter_btn) {

                    filterValueArray.set(0,Constants.CAT_STOOL);

                } else if (checkedId == R.id.Dresser_filter_btn) {

                    filterValueArray.set(0,Constants.CAT_DRESSER);

                } else if (checkedId == R.id.Table_filter_btn) {

                    filterValueArray.set(0,Constants.CAT_TABLE);

                } else if (checkedId == R.id.Sofa_filter_btn) {

                    filterValueArray.set(0,Constants.CAT_SOFACHAIR);

                }
            }
        });

        continuousRangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {

                List<Float> updatedValues = slider.getValues();
                float updatedMinValue = updatedValues.get(0);
                float updatedMaxValue = updatedValues.get(1);


                minValueTxt.setText(String.valueOf(updatedMinValue));
                maxValueTxt.setText(String.valueOf(updatedMaxValue));

                filterValueArray.add(1, String.valueOf(updatedMinValue));
                filterValueArray.add(2, String.valueOf(updatedMaxValue));

            }
        });

       apply_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               dialog.dismiss();
               if (listener != null) {
                   listener.onFilterApplied(filterValueArray);
               }

           }
       });



        dialog.show();


       return filterValueArray;

    }

    // Load Products By Category....

    // Data class to represent each category
    class Category {
        ImageView button;
        String productConstant;

        public Category(ImageView button, String productConstant) {
            this.button = button;
            this.productConstant = productConstant;
        }
    }

    // Map to hold all categories
    private Map<String, Category> categoryMap;

    private void initializeCategories() {
        // Initialize the categories
        categoryMap = new HashMap<>();
        categoryMap.put("All_cat", new Category(All_cat, null)); // null as it loads all products
        categoryMap.put("Chair_cat", new Category(Chair_cat, Constants.CAT_CHAIR));
        categoryMap.put("Stool_cat", new Category(Stool_cat, Constants.CAT_STOOL));
        categoryMap.put("Dresser_cat", new Category(Dresser_cat, Constants.CAT_DRESSER));
        categoryMap.put("Table_cat", new Category(Table_cat, Constants.CAT_TABLE));
        categoryMap.put("Sofa_cat", new Category(Sofa_cat, Constants.CAT_SOFACHAIR));
    }

    private void LoadCat(String catName) {
        // Reset all category buttons to the default state
        resetAllCategoryButtons();

        // Set the selected category button's background and tint
        setSelectedCategory(catName);

        // Load products based on the selected category
        loadProductsByCategory(catName);
    }

    // Helper method to reset all category buttons
    private void resetAllCategoryButtons() {
        int defaultBackground = R.drawable.circle;
        int defaultTintColor = ContextCompat.getColor(getActivity(), R.color.logogrey);

        // Reset all buttons dynamically
        for (Category category : categoryMap.values()) {
            setCategoryButtonState(category.button, defaultBackground, defaultTintColor);
        }
    }

    // Helper method to set background and tint of a category button
    private void setCategoryButtonState(ImageView categoryButton, int backgroundResource, int tintColor) {
        categoryButton.setBackgroundResource(backgroundResource);
        DrawableCompat.setTint(DrawableCompat.wrap(categoryButton.getDrawable()), tintColor);
    }

    // Helper method to highlight the selected category button
    private void setSelectedCategory(String catName) {
        int selectedBackground = R.drawable.circle_cat_selected;
        int selectedTintColor = ContextCompat.getColor(getActivity(), R.color.white);

        Category selectedCategory = categoryMap.get(catName);
        if (selectedCategory != null) {
            setCategoryButtonState(selectedCategory.button, selectedBackground, selectedTintColor);
        }
    }

    // Helper method to load products based on the selected category
    private void loadProductsByCategory(String catName) {
        Product_list_store.clear();

        Category selectedCategory = categoryMap.get(catName);
        if (selectedCategory != null) {
            if (selectedCategory.productConstant == null) {
                // Load all products
                Product_list_store.addAll(helper.room_dao().Get50Products());
            } else {
                // Load products by category
                Product_list_store.addAll(helper.room_dao().GetCategoryList(selectedCategory.productConstant));
            }
        }

        product_adapter.notifyDataSetChanged();
    }


    /*private void LoadCat(String CatName) {

        String Cat = CatName;

        All_cat.setBackgroundResource(R.drawable.circle);
        Chair_cat.setBackgroundResource(R.drawable.circle);
        Stool_cat.setBackgroundResource(R.drawable.circle);
        Dresser_cat.setBackgroundResource(R.drawable.circle);
        Table_cat.setBackgroundResource(R.drawable.circle);
        Sofa_cat.setBackgroundResource(R.drawable.circle);

        DrawableCompat.setTint(
                DrawableCompat.wrap( All_cat.getDrawable()),
                ContextCompat.getColor(getActivity(), R.color.logogrey)
        );

        DrawableCompat.setTint(
                DrawableCompat.wrap( Chair_cat.getDrawable()),
                ContextCompat.getColor(getActivity(), R.color.logogrey)
        );

        DrawableCompat.setTint(
                DrawableCompat.wrap( Stool_cat.getDrawable()),
                ContextCompat.getColor(getActivity(), R.color.logogrey)
        );

        DrawableCompat.setTint(
                DrawableCompat.wrap( Dresser_cat.getDrawable()),
                ContextCompat.getColor(getActivity(), R.color.logogrey)
        );

        DrawableCompat.setTint(
                DrawableCompat.wrap( Table_cat.getDrawable()),
                ContextCompat.getColor(getActivity(), R.color.logogrey)
        );

        DrawableCompat.setTint(
                DrawableCompat.wrap( Sofa_cat.getDrawable()),
                ContextCompat.getColor(getActivity(), R.color.logogrey)
        );

        switch (Cat){

            case "All_cat" :
                All_cat.setBackgroundResource(R.drawable.circle_cat_selected);

                DrawableCompat.setTint(
                        DrawableCompat.wrap( All_cat.getDrawable()),
                        ContextCompat.getColor(getActivity(), R.color.white)
                );

                Product_list_store.clear();
                Product_list_store.addAll(helper.room_dao().Get50Products());
                product_adapter.notifyDataSetChanged();



                break;

            case "Chair_cat" :
                Chair_cat.setBackgroundResource(R.drawable.circle_cat_selected);

                DrawableCompat.setTint(
                        DrawableCompat.wrap( Chair_cat.getDrawable()),
                        ContextCompat.getColor(getActivity(), R.color.white)
                );


                Product_list_store.clear();
                Product_list_store.addAll(helper.room_dao().GetCategoryList(Constants.CAT_CHAIR));
                product_adapter.notifyDataSetChanged();

                break;

            case "Stool_cat" :
                Stool_cat.setBackgroundResource(R.drawable.circle_cat_selected);

                DrawableCompat.setTint(
                        DrawableCompat.wrap( Stool_cat.getDrawable()),
                        ContextCompat.getColor(getActivity(), R.color.white)
                );

                Product_list_store.clear();
                Product_list_store.addAll(helper.room_dao().GetCategoryList(Constants.CAT_CHAIR));
                product_adapter.notifyDataSetChanged();


                break;

            case "Dresser_cat" :
                Dresser_cat.setBackgroundResource(R.drawable.circle_cat_selected);

                DrawableCompat.setTint(
                        DrawableCompat.wrap( Dresser_cat.getDrawable()),
                        ContextCompat.getColor(getActivity(), R.color.white)
                );

                Product_list_store.clear();
                Product_list_store.addAll(helper.room_dao().GetCategoryList(Constants.CAT_CHAIR));
                product_adapter.notifyDataSetChanged();


                break;

            case "Table_cat" :
                Table_cat.setBackgroundResource(R.drawable.circle_cat_selected);

                DrawableCompat.setTint(
                        DrawableCompat.wrap( Table_cat.getDrawable()),
                        ContextCompat.getColor(getActivity(), R.color.white)
                );

                Product_list_store.clear();
                Product_list_store.addAll(helper.room_dao().GetCategoryList(Constants.CAT_CHAIR));
                product_adapter.notifyDataSetChanged();


                break;

            case "Sofa_cat" :
                Sofa_cat.setBackgroundResource(R.drawable.circle_cat_selected);

                DrawableCompat.setTint(
                        DrawableCompat.wrap( Sofa_cat.getDrawable()),
                        ContextCompat.getColor(getActivity(), R.color.white)
                );

                Product_list_store.clear();
                Product_list_store.addAll(helper.room_dao().GetCategoryList(Constants.CAT_CHAIR));
                product_adapter.notifyDataSetChanged();


                break;


        }

    }*/


}