package com.astar.expirationdatemanagement.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class ExpirationDate {
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    public int expirationDateId;

    @ColumnInfo
    String productBarcode;

    @ColumnInfo
    String expirationDate;

    public ExpirationDate(String productBarcode, String expirationDate) {
        this.productBarcode = productBarcode;
        this.expirationDate = expirationDate;
    }

    @NonNull
    public String getProductBarcode() {
        return productBarcode;
    }

    @NonNull
    public String getExpirationDate() {
        return expirationDate;
    }
}
