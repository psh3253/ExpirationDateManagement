package com.astar.expirationdatemanagement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.astar.expirationdatemanagement.dao.ExpirationDateDao;
import com.astar.expirationdatemanagement.dao.ProductDao;
import com.astar.expirationdatemanagement.databinding.FragmentExpirationDateListBinding;
import com.astar.expirationdatemanagement.model.ExpirationDate;
import com.astar.expirationdatemanagement.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ExpirationDateListFragment extends Fragment {

    MainActivity mainActivity;
    FragmentExpirationDateListBinding binding;
    ExpirationDateAdapter expirationDateAdapter;
    ArrayList<ExpirationDate> expirationDateList;
    ProductDao productDao = DBInfo.appDatabase.productDao();
    ExpirationDateDao expirationDateDao = DBInfo.appDatabase.expirationDateDao();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity)
            mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExpirationDateListBinding.inflate(inflater, container, false);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.sort_standard_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spSortStandard.setAdapter(adapter);

        expirationDateList = (ArrayList<ExpirationDate>) expirationDateDao.getAll();
        expirationDateAdapter = new ExpirationDateAdapter(mainActivity);
        expirationDateAdapter.expirationDateList = expirationDateList;
        binding.rvExpirationList.setAdapter(expirationDateAdapter);
        binding.rvExpirationList.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.btExpiratiionRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.changeFragment(3);
            }
        });

        binding.spSortStandard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Collections.sort(expirationDateList, new Comparator<ExpirationDate>() {
                        @Override
                        public int compare(ExpirationDate o1, ExpirationDate o2) {
                            String productName1 = productDao.getProductByBarcode(o1.getProductBarcode()).getProductName();
                            String productName2 = productDao.getProductByBarcode(o2.getProductBarcode()).getProductName();
                            if (productName1.compareTo(productName2) < 0)
                                return -1;
                            else if (productName1.equals(productName2))
                                if(o1.getExpirationDate().compareTo(o2.getExpirationDate()) < 0)
                                    return -1;
                                else
                                    return 0;
                            else
                                return 1;
                        }
                    });
                    expirationDateAdapter.notifyDataSetChanged();
                } else {
                    Collections.sort(expirationDateList, new Comparator<ExpirationDate>() {
                        @Override
                        public int compare(ExpirationDate o1, ExpirationDate o2) {
                            String expirationDate1 = o1.getExpirationDate();
                            String expirationDate2 = o2.getExpirationDate();
                            if (expirationDate1.compareTo(expirationDate2) < 0)
                                return -1;
                            else if (expirationDate1.equals(expirationDate2)) {
                                String productName1 = productDao.getProductByBarcode(o1.getProductBarcode()).getProductName();
                                String productName2 = productDao.getProductByBarcode(o2.getProductBarcode()).getProductName();
                                if(productName1.compareTo(productName2) < 0)
                                    return -1;
                                else
                                    return 0;
                            }
                            else
                                return 1;
                        }
                    });
                    binding.rvExpirationList.setAdapter(expirationDateAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("TestTest", "Not");
            }
        });

        return binding.getRoot();
    }

    public void refreshExpirationDateList() {
        expirationDateList = (ArrayList<ExpirationDate>) expirationDateDao.getAll();
        expirationDateAdapter.expirationDateList = expirationDateList;
        binding.rvExpirationList.setAdapter(expirationDateAdapter);
    }
}