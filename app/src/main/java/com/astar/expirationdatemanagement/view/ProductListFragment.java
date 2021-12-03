package com.astar.expirationdatemanagement.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astar.expirationdatemanagement.DBInfo;
import com.astar.expirationdatemanagement.MainActivity;
import com.astar.expirationdatemanagement.dao.ProductDao;
import com.astar.expirationdatemanagement.databinding.FragmentProductListBinding;
import com.astar.expirationdatemanagement.model.Product;

import java.util.ArrayList;
import java.util.Comparator;

public class ProductListFragment extends Fragment {

    MainActivity mainActivity;
    FragmentProductListBinding binding;
    ProductAdapter productAdapter;
    ProductDao productDao = DBInfo.appDatabase.productDao();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity)
            mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductListBinding.inflate(inflater, container, false);

        ArrayList<Product> products = (ArrayList<Product>) productDao.getAll();

        productAdapter = new ProductAdapter(mainActivity);
        products.sort((product, t1) -> {
            if (product.getProductName().compareTo(t1.getProductName()) < 0) {
                return -1;
            } else if (product.getProductName().compareTo(t1.getProductName()) == 0) {
                return 0;
            } else {
                return 1;
            }
        });
        productAdapter.productList = products;
        binding.rvProductList.setAdapter(productAdapter);
        binding.rvProductList.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.btProductRegister.setOnClickListener(v -> mainActivity.changeFragment(1));

        binding.btProductSearch.setOnClickListener(v -> mainActivity.changeFragment(2));

        return binding.getRoot();
    }

    public void refreshProductList() {
        ArrayList<Product> productList = (ArrayList<Product>) productDao.getAll();
        productList.sort((product, t1) -> {
            if (product.getProductName().compareTo(t1.getProductName()) < 0) {
                return -1;
            } else if (product.getProductName().compareTo(t1.getProductName()) == 0) {
                return 0;
            } else {
                return 1;
            }
        });
        productAdapter.productList = productList;
        binding.rvProductList.setAdapter(productAdapter);
    }
}