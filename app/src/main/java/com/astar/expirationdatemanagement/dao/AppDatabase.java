package com.astar.expirationdatemanagement.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.astar.expirationdatemanagement.model.ExpirationDate;
import com.astar.expirationdatemanagement.model.Product;

@Database(entities = {Product.class, ExpirationDate.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
    public abstract ExpirationDateDao expirationDateDao();
}
