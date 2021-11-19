package com.astar.expirationdatemanagement;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.astar.expirationdatemanagement.dao.ProductDao;
import com.astar.expirationdatemanagement.databinding.FragmentProductSearchBinding;
import com.astar.expirationdatemanagement.model.Product;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ProductSearchFragment extends Fragment {

    FragmentProductSearchBinding binding;
    MainActivity mainActivity;
    ProductAdapter productAdapter;
    ProductDao productDao = DBInfo.appDatabase.productDao();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity)
            mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductSearchBinding.inflate(inflater, container, false);

        ArrayList<Product> products = new ArrayList<>();

        productAdapter = new ProductAdapter(mainActivity);
        productAdapter.productList = products;
        binding.rvSearchProduct.setAdapter(productAdapter);
        binding.rvSearchProduct.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.btSearchCaptureBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcodeLauncher.launch(new ScanOptions());
            }

            private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
                    result -> {
                        if (result.getContents() != null) {
                            binding.etProductNameOrBarcode.setText(result.getContents());
                        }
                    });
        });

        binding.btProductSearchSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Product> productList = new ArrayList<>();
                Product product = productDao.getProductByName(binding.etProductNameOrBarcode.getText().toString());
                if (product == null) {
                    product = productDao.getProductByBarcode(binding.etProductNameOrBarcode.getText().toString());
                    if (product == null) {
                        productAdapter.productList = new ArrayList<>();
                        binding.rvSearchProduct.setAdapter(productAdapter);
                        Toast.makeText(getContext(), "검색된 상품이 없습니다", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                productList.add(product);
                productAdapter.productList = productList;
                binding.rvSearchProduct.setAdapter(productAdapter);
            }
        });

        return binding.getRoot();
    }

    public void refreshSearch() {
        productAdapter.productList = new ArrayList<>();
        binding.rvSearchProduct.setAdapter(productAdapter);
    }
}