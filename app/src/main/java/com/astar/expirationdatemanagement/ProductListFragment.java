package com.astar.expirationdatemanagement;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.astar.expirationdatemanagement.dao.ProductDao;
import com.astar.expirationdatemanagement.databinding.FragmentProductListBinding;
import com.astar.expirationdatemanagement.model.Product;

import java.util.ArrayList;

public class ProductListFragment extends Fragment {

    MainActivity mainActivity;
    FragmentProductListBinding binding;
    ProductDao productDao = Info.appDatabase.productDao();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof MainActivity)
            mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductListBinding.inflate(inflater, container, false);

        ArrayList<Product> products = (ArrayList<Product>) productDao.getAll();

        ProductAdapter productAdapter = new ProductAdapter();
        productAdapter.productList = products;
        binding.rvListProduct.setAdapter(productAdapter);
        binding.rvListProduct.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.btProductRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.changeFragment(1);
            }
        });

        binding.btProductSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.changeFragment(2);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<Product> products = (ArrayList<Product>) productDao.getAll();

        ProductAdapter productAdapter = new ProductAdapter();
        productAdapter.productList = products;
        binding.rvListProduct.setAdapter(productAdapter);
    }

    public void refreshProductList() {
        ArrayList<Product> products = (ArrayList<Product>) productDao.getAll();

        ProductAdapter productAdapter = new ProductAdapter();
        productAdapter.productList = products;
        binding.rvListProduct.setAdapter(productAdapter);
    }
}