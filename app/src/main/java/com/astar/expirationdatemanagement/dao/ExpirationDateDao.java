package com.astar.expirationdatemanagement.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.astar.expirationdatemanagement.model.ExpirationDate;

import java.util.Date;
import java.util.List;

@Dao
public interface ExpirationDateDao {
    @Query("SELECT * FROM expirationdate")
    List<ExpirationDate> getAll();

    @Query("SELECT * FROM expirationdate WHERE expirationDate >= STRFTIME('%Y년 %m월 %d일', 'now')")
    List<ExpirationDate> getCurrentExpirationList();

    @Query("SELECT * FROM expirationdate WHERE expirationDate < STRFTIME('%Y년 %m월 %d일', 'now')")
    List<ExpirationDate> getPreviousExpirationList();

    @Query("SELECT * FROM expirationdate WHERE productBarcode = :productBarcode")
    ExpirationDate getExpirationByProductBarcode(String productBarcode);

    @Query("SELECT * FROM expirationdate WHERE productBarcode = :productBarcode AND expirationDate = :expirationDate")
    ExpirationDate getExpiration(String productBarcode, String expirationDate);

    @Insert
    void insert(ExpirationDate expirationDate);

    @Delete
    void delete(ExpirationDate expirationDate);

    @Query("DELETE FROM expirationdate WHERE productBarcode = :productBarcode")
    void deleteByProductBarcode(String productBarcode);

    @Query("DELETE FROM expirationdate")
    void deleteAll();
}
