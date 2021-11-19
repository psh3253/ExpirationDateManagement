package com.astar.expirationdatemanagement.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.astar.expirationdatemanagement.model.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM product")
    List<Product> getAll();

    @Query("SELECT * FROM product WHERE productBarcode = :productBarcode")
    Product getProductByBarcode(String productBarcode);

    @Query("SELECT * FROM product WHERE productName = :productName")
    Product getProductByName(String productName);

    @Insert
    void insert(Product product);

    @Delete
    void delete(Product product);

    @Query("DELETE FROM product")
    void deleteAll();
}
