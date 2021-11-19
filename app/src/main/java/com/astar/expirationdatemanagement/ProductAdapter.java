package com.astar.expirationdatemanagement;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astar.expirationdatemanagement.dao.ProductDao;
import com.astar.expirationdatemanagement.databinding.ProductRecyclerBinding;
import com.astar.expirationdatemanagement.model.Product;

import java.io.File;
import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductHolder> {

    MainActivity mainActivity;
    ProductDao productDao = DBInfo.appDatabase.productDao();
    ArrayList<Product> productList = new ArrayList<>();

    public ProductAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductRecyclerBinding binding = ProductRecyclerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder productHolder, int position) {
        Product product = productList.get(position);
        productHolder.setProduct(product);
        productHolder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add("삭제").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        productDao.delete(product);
                        File imageFile = new File(product.getProductImagePath());
                        if(imageFile.exists())
                            imageFile.delete();
                        mainActivity.refreshProductList();
                        mainActivity.refreshSearch();
                        return true;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}

class ProductHolder extends RecyclerView.ViewHolder {

    private final ProductRecyclerBinding binding;

    public ProductHolder(@NonNull View itemView, ProductRecyclerBinding binding) {
        super(itemView);
        this.binding = binding;
    }

    public void setProduct(Product product) {
        binding.tvProductProductName.setText(product.getProductName());
        binding.tvProductProductBarcode.setText(product.getProductBarcode());

        Bitmap productBitmap = BitmapFactory.decodeFile(product.getProductImagePath());
        binding.ivProductImage.setImageBitmap(productBitmap);
    }

    public ProductRecyclerBinding getBinding() {
        return binding;
    }
}
