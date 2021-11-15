package com.astar.expirationdatemanagement;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astar.expirationdatemanagement.databinding.ProductRecyclerBinding;
import com.astar.expirationdatemanagement.model.Product;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<Holder> {

    ArrayList<Product> productList = new ArrayList<>();

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductRecyclerBinding binding = ProductRecyclerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new Holder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Product product = productList.get(position);
        holder.setProduct(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}

class Holder extends RecyclerView.ViewHolder {

    private final ProductRecyclerBinding binding;
    public Holder(@NonNull View itemView, ProductRecyclerBinding binding) {
        super(itemView);
        this.binding = binding;
    }

    public void setProduct(Product product) {
        binding.tvProductName.setText(product.getProductName());
        binding.tvProductBarcode.setText(product.getProductBarcode());

        Bitmap productBitmap = BitmapFactory.decodeFile(product.getProductImagePath());
        binding.ivProductImage.setImageBitmap(productBitmap);
    }
}
