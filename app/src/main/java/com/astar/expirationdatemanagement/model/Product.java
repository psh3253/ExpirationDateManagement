package com.astar.expirationdatemanagement.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Product {
    @ColumnInfo
    String productName;

    @NonNull
    @PrimaryKey
    @ColumnInfo
    String productBarcode;

    @ColumnInfo
    String productImagePath;

    public Product(String productName, String productBarcode, String productImagePath) {
        this.productName = productName;
        this.productBarcode = productBarcode;
        this.productImagePath = productImagePath;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductBarcode() {
        return productBarcode;
    }

    public String getProductImagePath() {
        return productImagePath;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductBarcode(String productBarcode) {
        this.productBarcode = productBarcode;
    }

    public void setProductImagePath(String productImagePath) {
        this.productImagePath = productImagePath;
    }
}
