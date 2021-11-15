package com.astar.expirationdatemanagement;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astar.expirationdatemanagement.databinding.FragmentProductSearchBinding;
import com.astar.expirationdatemanagement.model.Product;

import java.util.ArrayList;

public class ProductSearchFragment extends Fragment {

    FragmentProductSearchBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductSearchBinding.inflate(inflater, container, false);

        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("옴팡이", "110111", "이미지경로"));
        products.add(new Product("귀여워", "110126", "이미지경로"));

        ProductAdapter productAdapter = new ProductAdapter();
        productAdapter.productList = products;
        binding.rvSearchProduct.setAdapter(productAdapter);
        binding.rvSearchProduct.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }
}