package com.example.easyshopping.Data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.easyshopping.Model.AddressModel;
import com.example.easyshopping.Model.CartModel;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface Room_DAO {


    //Product Table................................................................
    @Query("select * from product_table")
    List<Room_Product_Model> GetAllData();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void AddProducts(ArrayList<Room_Product_Model> productModel);

    @Update
    void UpdateProduct(Room_Product_Model productModel);

    @Query("SELECT * FROM product_table ORDER BY RANDOM() LIMIT 5")
    List<Room_Product_Model> GetRandomProducts();

    @Query("select * from product_table LIMIT 50")
    List<Room_Product_Model> Get50Products();

    @Query("SELECT * FROM product_table ORDER BY id ASC LIMIT :limit OFFSET :offset")
    List<Room_Product_Model> getProductsPage(int limit, int offset);

    @Query("SELECT * FROM product_table WHERE Id BETWEEN :fitem AND :litem")
    List<Room_Product_Model> getProductdatapage(int fitem,int litem);

    @Query("SELECT * FROM product_table LIMIT :pageSize OFFSET :offset")
    List<Room_Product_Model> getProductPage(int pageSize, int offset);

    @Query("SELECT * FROM product_table WHERE Name IS :CartPrName")
    List<Room_Product_Model> GetCartProductsList(String CartPrName);

    @Query("SELECT * FROM product_table WHERE Name IS :CartPrName")
    Room_Product_Model GetCartProduct(String CartPrName);

    @Query("DELETE FROM product_table")
    void ClearProducts();

    @Query("select * from product_table WHERE cat IS :Catagory LIMIT 100")
    List<Room_Product_Model> GetCategoryList(String Catagory);

    @Query("SELECT * FROM product_table WHERE Id BETWEEN :fitem AND :litem AND cat IS :Catagory")
    List<Room_Product_Model> GetPageByCat(int fitem,int litem,String Catagory);

    @Query("SELECT * FROM product_table WHERE cat IS :Catagory LIMIT 30")
    List<Room_Product_Model> GetCat(String Catagory);

    @Query("SELECT * FROM product_table WHERE name LIKE :query")
    List<Room_Product_Model> searchByName(String query);

    @Query("SELECT * FROM product_table WHERE name LIKE :searchQuery")
    List<Room_Product_Model> searchProducts(String searchQuery);

    @Query("SELECT * FROM product_table WHERE name IS :ProductName")
    Room_Product_Model GetOrderDetailsProducts(String ProductName);

    @Query("SELECT * FROM product_table WHERE cat = :category AND CAST(price AS REAL) BETWEEN :minPrice AND :maxPrice LIMIT 100")
    List<Room_Product_Model> getFilteredProductList(String category, float minPrice, float maxPrice);

    @Query("SELECT * FROM product_table WHERE CAST(price AS REAL) BETWEEN :minPrice AND :maxPrice LIMIT 100")
    List<Room_Product_Model> getFilteredProductListPrice(float minPrice, float maxPrice);

    @Query("SELECT MAX(CAST(price AS REAL)) FROM product_table")
    float getMaxPrice();



    //User Table................................................................
    @Insert
    void AddUser(UserDataModel userDataModel);

    @Query("select * from userdata")
    List<UserDataModel> getalluserdetails();

    @Query("DELETE FROM userdata")
    void ClearUserData();

    @Query("UPDATE userdata SET UserName = :username WHERE RoomId IS :roomid")
    void Updateusername(String username, int roomid);

    @Query("UPDATE userdata SET userphotoURL = :userprofilephoto WHERE UserID IS :userid")
    void updateUserProfilePhoto(String userprofilephoto, String userid);



    // Cart Table................................................................
    @Insert
    void AddCart(CartModel cartModel);

    @Query("select * from cartlist")
    List<CartModel> GetCartList();

    @Query("DELETE FROM cartlist")
    void ClearCart();

    //AddressBook........................................................

    @Insert
    void AddAddress(AddressModel addressModel);

    @Insert
    void AddAddressList(ArrayList<AddressModel> AddBook);

    @Query("select * from AddressBook")
    List<AddressModel> GetAllAddress();

    @Query("DELETE FROM AddressBook")
    void ClearAddressBook();



}
