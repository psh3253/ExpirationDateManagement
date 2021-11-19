package com.astar.expirationdatemanagement;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.astar.expirationdatemanagement.dao.ExpirationDateDao;
import com.astar.expirationdatemanagement.databinding.FragmentExpirationDateListBinding;
import com.astar.expirationdatemanagement.model.ExpirationDate;
import com.astar.expirationdatemanagement.model.Product;

import java.util.ArrayList;


public class ExpirationDateListFragment extends Fragment {

    MainActivity mainActivity;
    FragmentExpirationDateListBinding binding;
    ExpirationDateAdapter expirationDateAdapter;
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

        ArrayList<ExpirationDate> expirationDateList = (ArrayList<ExpirationDate>) expirationDateDao.getAll();
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

        return binding.getRoot();
    }

    public void refreshExpirationDateList() {
        expirationDateAdapter.expirationDateList = (ArrayList<ExpirationDate>) expirationDateDao.getAll();
        binding.rvExpirationList.setAdapter(expirationDateAdapter);
    }
}