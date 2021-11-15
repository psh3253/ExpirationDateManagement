package com.astar.expirationdatemanagement.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.astar.expirationdatemanagement.model.Product;

@Database(entities = {Product.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
}
